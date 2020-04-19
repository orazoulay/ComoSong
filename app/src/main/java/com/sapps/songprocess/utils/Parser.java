package com.sapps.songprocess.utils;


import android.database.Cursor;

import com.sapps.songprocess.data.Line;
import com.sapps.songprocess.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




public class Parser {

    public static <T> T jsonParse(JSONArray jsonArray, int key, T ret) {

        try {
            Object obj = jsonArray.get(key);
            ret = getObjectData(obj, ret);
        } catch (Exception e) {
            onParseFailure(e, key + "");
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getObjectData(Object obj, T ret) throws JSONException {
        String objSimpleName = obj.getClass().getSimpleName();
        String retSimpleName = ret.getClass().getSimpleName();
        if (objSimpleName == null || objSimpleName.isEmpty()) {
            return ret;
        }
        if (retSimpleName
                .equals(objSimpleName)) {
            ret = (T) obj;
        } else if (ret instanceof JSONObject && obj instanceof JSONArray) {
            ret = (T) convertJsonArrayToJsonObject((JSONArray) obj);
        } else if (ret instanceof Boolean) {
            String data = obj.toString();
            ret = (T) new Boolean(data.equals("1"));
//        } else if (ret instanceof String && NumberUtils.stringIsnumber(obj.toString())) {
//            ret = (T) obj.toString();
        } else if (ret instanceof JSONArray && obj instanceof JSONObject) {
            ret = (T) convertJsonObjectToJsonArray((JSONObject) obj);
        } else {
//            ret = convertStringToNumber(ret, obj);
        }
        return ret;
    }

    public static boolean cursorBooleanParse(Cursor cursor, String key, boolean defualtValue) {
        return cursor.getInt(cursor.getColumnIndex(key.trim())) == 1;
    }

    public static int cursorIntParse(Cursor cursor, String key, int defualtValue) {
        return cursor.getInt(cursor.getColumnIndex(key.trim()));
    }

    public static String cursorStringParse(Cursor cursor, String key, String defualtValue) {
        String ret = cursor.getString(cursor.getColumnIndex(key.trim()));
        return ret != null ? ret : defualtValue;
    }

//    public static List<String> cursorListStringParse(Cursor cursor, String key, String defualtValue) {
//        String value = cursorStringParse(cursor, key, defualtValue);
//        return CollectionUtils.csv2list(value);
//    }

    public static long cursorLongParse(Cursor cursor, String key, long defualtValue) {
        return cursor.getLong(cursor.getColumnIndex(key.trim()));
    }

    public static int getIntValueFromHashMap(HashMap<String, Integer> hashMap,
                                             String key) {

        if (hashMap.containsKey(key)) {
            return hashMap.get(key);
        }
        return 0;

    }

    public static List<String> converHashMapToList(
            HashMap<String, String> hashMap) {
        List<String> list = new ArrayList<>();

        Iterator myVeryOwnIterator = hashMap.keySet().iterator();
        while (myVeryOwnIterator.hasNext()) {
            String key = (String) myVeryOwnIterator.next();
            String value = hashMap.get(key);
            list.add(value);

        }

        return list;
    }


    @SuppressWarnings("unchecked")
    public static <T> T jsonParse(JSONObject jsonObject, String key, T ret) {
        if (jsonObject.has(key)) {
            try {
                Object obj = jsonObject.get(key);
                ret = getObjectData(obj, ret);
            } catch (Exception e) {
                onParseFailure(e, key);
            }
        }

        return ret;
    }

    public static String jsonParseString(JSONObject jsonObject, String key, String ret) {
        if (jsonObject.has(key)) {
            try {
                return jsonObject.getString(key);
            } catch (Exception e) {
                try {
                    return getObjectData(getObjectData(jsonObject.get(key), ret), ret);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return ret;
    }

    private static void onParseFailure(Exception e, String key) {
//        LoggingHelper.e("exception type:" + e.getClass().getSimpleName() + " message:" + e.getMessage() + " was thrown by " + key);
    }

    @SuppressWarnings("unchecked")
//    private static <T> T convertStringToNumber(T ret, Object obj) {
//        if (NumberUtils.stringIsnumber(obj.toString())) {
//
//            if (ret instanceof Integer) {
//                ret = (T) NumberUtils.getIntegerFromString(obj + "");
//
//            } else if (ret instanceof Double) {
//                ret = (T) (Double) NumberUtils.getDoubleFromString(obj + "");
//            } else if (ret instanceof Long) {
//                ret = (T) (Long) NumberUtils.getLongFromString(obj + "");
//            }
//
//        }
//        return ret;
//    }
//
    private static JSONArray convertJsonObjectToJsonArray(JSONObject jsonObject)
            throws JSONException {
        Iterator<String> iterator = jsonObject.keys();
        JSONArray jsonArray = new JSONArray();

        while (iterator.hasNext()) {
            String key2 = iterator.next();
            jsonArray.put(jsonObject.get(key2));
        }

        return jsonArray;
    }

    @SuppressWarnings("unchecked")
    private static JSONObject convertJsonArrayToJsonObject(JSONArray array)
            throws JSONException {

        JSONObject ret = new JSONObject();
        for (int i = 0; i < array.length(); i++) {
            ret.put(i + "", array.get(i));
        }
        return ret;

    }

    public static List <User> createList(JSONObject jsonObject, User defualtValue) {
        List<User> list = new ArrayList<>();
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            User value = User.createUserFromResponse(Parser.jsonParse(jsonObject, key, new JSONObject()));
            if (value != defualtValue) {
                list.add(value);
            }
        }

        return list;

    }
    public static List <Line> createList(JSONObject jsonObject, Line defualtValue) {
        List<Line> list = new ArrayList<>();
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Line value = Line.createUserFromResponse(Parser.jsonParse(jsonObject, key, new JSONObject()));
            if (value != defualtValue) {
                list.add(value);
            }
        }

        return list;

    }

    public static HashMap<String, String> getHashMapFromJson(HashMap<String, String> hashMap, JSONObject jsonObject) {

        hashMap = new HashMap<>();
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            hashMap.put(key, Parser.jsonParse(jsonObject, key, Parser.createTempString()));
        }

        return hashMap;

    }

    public static JSONArray createTempJsonArray() {
        return new JSONArray();
    }

    public static JSONObject createTempJsonObject() {
        return new JSONObject();
    }

    public static String createTempString() {
        return new String();
    }

    public static String getValueFromHashMap(HashMap<String, String> hashMap,
                                             String key) {

        if (hashMap.containsKey(key)) {
            return hashMap.get(key);
        }
        return key;

    }

    public static boolean isJsonObjectEmpty(JSONObject jsonObject) {
        return jsonObject.length() == 0;
    }


//    public static BaseResponse getBaseResponseFromJson(JSONObject json, Parseable parseable, String key) {
//
//        BaseResponse response = null;
//        JSONObject jsonObject = Parser.jsonParse(json, key, new JSONObject());
//        response = parseable.createResponse(jsonObject);
//        return response;
//    }
//
//    public static List<? extends BaseResponse> createList(JSONObject json, Parseable parseable) {
//        List<BaseResponse> list = new ArrayList<>();
//        @SuppressWarnings("unchecked")
//        Iterator<String> iterator = json.keys();
//        while (iterator.hasNext()) {
//            String key = iterator.next();
//            JSONObject jsonObject = Parser.jsonParse(json, key, new JSONObject());
//            BaseResponse baseResponse = parseable.createResponse(jsonObject);
//            list.add(baseResponse);
//        }
//        return list;
//    }

//    public static List<? extends BaseResponse> createList(JSONArray jsonArray, Parseable parseable) {
//
//        List<BaseResponse> list = new ArrayList<>();
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject jsonObject = Parser.jsonParse(jsonArray, i, new JSONObject());
//            list.add(parseable.createResponse(jsonObject));
//        }
//        return list;
//    }


    public static List<String> createList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(Parser.jsonParse(jsonArray, i, ""));
        }
        return list;

    }


//    public static List<? extends BaseResponse> createList(JSONObject json, SortResponse sortResponse) {
//        List<SortResponse> list = (List<SortResponse>) createList(json, (Parseable) sortResponse);
//        Collections.sort(list);
//        return list;
//    }
//
//    public static List<? extends BaseResponse> createList(JSONArray jsonArray, SortResponse sortResponse) {
//
//        List<SortResponse> list = (List<SortResponse>) createList(jsonArray, (Parseable) sortResponse);
//        Collections.sort(list);
//        return list;
//    }

//    public static List<? extends BaseResponse> createList(Cursor cursor, SortResponse sortResponse) {
//
//        List<SortResponse> list = (List<SortResponse>) createList(cursor, (Parseable) sortResponse);
//        Collections.sort(list);
//        return list;
//    }
//
//    public static List<? extends BaseResponse> createList(Cursor cursor, Parseable parseable) {
//
//        List<BaseResponse> list = new ArrayList<>();
//        cursor.moveToFirst();
//        if (cursor.getCount() > 0) {
//            do {
//                list.add(parseable.createResponse(cursor));
//            } while (cursor.moveToNext());
//        }
//        return list;
//    }
//
    public static HashMap<String, User> createHashMap(JSONObject json) {
        HashMap<String, User> hashMap = new HashMap<>();
        Iterator<String> iterator = json.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            JSONObject jsonObject = Parser.jsonParse(json, key, new JSONObject());
            hashMap.put(key, User.createUserFromResponse(jsonObject));
        }
        return hashMap;
    }
//
//    public static HashMap<String, ? extends BaseResponse> createHashMap(JSONArray jsonArray, Parseable parser) {
//        HashMap<String, BaseResponse> hashMap = new HashMap<>();
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject json = jsonArray.optJSONObject(i);
//            Iterator<String> iterator = json.keys();
//            while (iterator.hasNext()) {
//                String key = iterator.next();
//                JSONObject jsonObject = Parser.jsonParse(json, key, new JSONObject());
//                hashMap.put(key, parser.createResponse(jsonObject));
//            }
//        }
//        return hashMap;
//    }
//
//    public static HashMap<String, ? extends BaseResponse> createHashMap(JSONArray jsonArray, Hashable hashable) {
//        HashMap<String, BaseResponse> hashMap = new HashMap<>();
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject json = jsonArray.optJSONObject(i);
//            Hashable newHashable = (Hashable) hashable.createResponse(json);
//            hashMap.put(newHashable.getIdNum(), (BaseResponse) newHashable);
//        }
//        return hashMap;
//    }
//
//    public static HashMap<String, ? extends BaseResponse> createHashMap(Cursor cursor, Hashable hashable) {
//
//        HashMap<String, BaseResponse> hashMap = new HashMap<>();
//        cursor.moveToFirst();
//        if (cursor.getCount() > 0) {
//            do {
//                Hashable newHashable = (Hashable) hashable.createResponse(cursor);
//                hashMap.put(newHashable.getIdNum(), (BaseResponse) newHashable);
//            } while (cursor.moveToNext());
//        }
//        return hashMap;
//    }

    public static JSONObject createJsonObject(String json) {
        try {
            return new JSONObject(json);
        } catch (Exception e) {
            return createTempJsonObject();
        }
    }


    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }

        return null;
    }


}

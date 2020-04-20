const express = require('express');
const router = express.Router();
const User = require('../models/User');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
const {registerValidation, loginValidation} = require('../validation');
const mongoose = require('mongoose');

router.post('/register', async (req, res) => {
    const regUser = new User;
    console.log(req.body);
    console.log("Request Body");
    regUser.username = req.body.username;
    regUser.password = req.body.password;
    regUser.uid = req.body.uid;
    await create(regUser, res);

});


async function create(userParam, res) {
    // validate
    if (await User.findOne({username: userParam.username})) {
        res.status(400).json({
            status: 0
        });
    }

    const user = new User(userParam);

    // hash password
    // if (userParam.password) {
    //     user.hash = bcrypt.hashSync(userParam.password, 10);
    // }

    // save user
    await user.save();
    res.status(200).json({
        status: 1
    });
}

//LOGIN

const authUser = new User;
router.post('/login', async (req, res) => {
    console.log("Request Body");
    console.log(req.body);
    authUser.username = req.body.username;
    User.findOne({username: req.body.username, password: req.body.password}).then(user => {
        if (user) {
            ///if user have song waiting on the list
            if (user.openSong !== null) {
                User.findOne({uid: user.sendSongId}).then(senderUser => {
                    if (senderUser) {
                        user.sendSongName = senderUser._doc.name
                        return res.status(200).json({
                            request: 'login',
                            status: 1,
                            data: {
                                user: user
                            }
                        });
                    } else {
                        return res.status(200).json({
                            request: 'login',
                            status: 1,
                            data: {
                                user: user
                            }
                        });
                    }
                });
            }

        } else {
            return res.status(400).json({
                request: 'login',
                status: 0,
                massage: "פרטים לא נכונים"
            });
        }
    });


});

async function update(param, newDataQuery, res) {

    User.findOneAndUpdate(param, newDataQuery, {upsert: true}, function (err, doc) {
        if (err) return res.send(500, {error: err});
        // return res.status(200).json({
        //     request: 'updateUser',
        //     status: 1,
        //     user: doc._doc
        // });
    });


}

router.post('/updateUser', async (req, res) => {
    const usersId = req.body.usersId.split('|');
    const jsons = [{
        songSubtitle: {
            lines: [{number: "1", start: "0:00", end: "00:04:500", words: "שבעים שנה במכונית"},
                {number: "2", start: "00:05", end: "00:08:00", words: "אני נוסע ומביט"},
                {number: "3", start: "00:08", end: "00:11:500", words: "על מה ומה נהיה"}]
        }
    },
        {songSubtitle: {lines: {number: "2", start: "00:05", end: "00:08:00", words: "אני נוסע ומביט"}}},
        {songSubtitle: {lines: {number: "3", start: "00:08", end: "00:11:500", words: "על מה ומה נהיה"}}}];

    // Remove the last slot in the array which is an empty slot
    usersId.pop();
    for (var i = 0; i < usersId.length; i++) {
        //send the song name and the sender id to the other id's that in the songProcess

        await update({uid: usersId[i]}, {openSong: req.body.openSong, sendSongId: req.body.uid}, res);
        await update({uid: usersId[i]}, jsons[i], res);
        await sleep(1000);

        // await update({uid: usersId[i]},jsons[i] , res);
    }

    await update({username: req.body.username}, {usersId: usersId}, res);

  await User.findOne({username: req.body.username}).then(senderUser => {
        if (senderUser) {
            return res.status(200).json({
                request: 'updateUser',
                status: 1,
                data: {
                    user: senderUser
                }
            });
        }
    });
});


function sleep(ms) {
    return new Promise((resolve) => {
        setTimeout(resolve, ms);
    });
}

module.exports = router;
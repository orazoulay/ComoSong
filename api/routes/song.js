const express = require('express');
const router = express.Router();
const User = require('../models/User');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
const formidable = require('formidable');
const fs = require('fs');
const path = require('path');
const directoryPath = path.join('', 'resources');
var ffmpeg = require('fluent-ffmpeg');


async function setFiles() {
    await ffmpeg('./resources/1.mp4')
        .input('./resources/2.mp4')
        .input('./resources/3.mp4')
        .input('./resources/4.mp4')
        .input('./resources/5.mp4')
        .on('error', function (err) {
            console.log('An error occurred: ' + err.message);
            reject('An error occurred: ' + err.message);
        })
        .mergeToFile('./resources/final.mp4')
        .on('end', function () {
            console.log("merged");
            // addFileToDb();
        });
}

router.post('/getMergeSongs', async (req, res) => {
    fs.readdir(directoryPath, function (err, files) {
        //handling error
        if (err) {
            return console.log('Unable to scan directory: ' + err);
        }
        if (files.length === 5) {
            setFiles()
            //listing all files using forEach
            // files.forEach(function (file) {
            //     // Do whatever you want to do with the file
            //     console.log(file);
            // });
        }
    });
});

router.post('/uploadSong', async (req, res) => {
    console.log(req.body);
    const form = new formidable.IncomingForm();
    form.parse(req, function (err, fields, files) {
        // const updateUser = new User;
        //    updateUser.username = req.body.username;
        //    updateUser.uid = req.body.uid;
        //    updateUser.password = req.body.password;
        const oldpath = files.name.path;
        const newpath = './resources/' + files.name.name + ".mp4";
        // updateUser.filePath = newpath;
        fs.rename(oldpath, newpath, function (err) {
            if (err) throw err;
            // res.write('File uploaded and moved!');
            // updateDB(newpath,req,updateUser);
            res.end();
        });
    });
    return res.status(200).json({
        status: 0,
        message: "Request Success",
        // data:
    });


});

async function updateDB(filePath, res, userParam) {
    const user = await User.findById(userParam.uid);

    // validate
    if (!user) throw 'User not found';
    if (user.username !== userParam.username && await User.findOne({username: userParam.username})) {
        res.status(400).json({
            status: 0,
            massage: ""
        });
    }

    // hash password if it was entered
    // if (userParam.password) {
    //     userParam.hash = bcrypt.hashSync(userParam.password, 10);
    // }

    // copy userParam properties to user
    Object.assign(user, userParam);

    await user.save();
}

router.post('/getSongProcess', async (req, res) => {

   await update({uid: req.body.uid}, {songSubtitle: {lines: {number: "1",start: "0:00",end: "00:04:500",words:"שבעים שנה במכונית"}}},res)

});


async function update(param, newDataQuery, res) {

    User.findOneAndUpdate(param, newDataQuery, {upsert: true}, function (err, doc) {
        if (err) return res.send(500, {error: err});
        return res.status(200).json({
            request: 'updateUser',
            status: 1,
            user: doc._doc
        });
    });
}


module.exports = router;
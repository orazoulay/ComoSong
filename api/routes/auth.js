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
    var child = require('child_process').spawn('java', ['-jar', '/javaJar/UsersPartition.jar', '/translates/John-Lennon-Imagine.srt 3 00:00:000 01:35:000 /translates/json.txt']);
    const usersId = req.body.usersId.split('|');
    const jsons = [{
        songSubtitle: {
            "lines": [
                {
                    "number": 1,
                    "start": "00:12:201",
                    "words": "Imagine there′s no heaven",
                    "end": "00:17:900"
                },
                {
                    "number": 2,
                    "start": "00:18:714",
                    "words": "It is easy if you try",
                    "end": "00:24:613"
                },
                {
                    "number": 3,
                    "start": "00:25:213",
                    "words": "No hell below us",
                    "end": "00:31:012"
                },
                {
                    "number": 4,
                    "start": "00:32:404",
                    "words": "Above us only sky",
                    "end": "00:37:503"
                }
            ]
        }
    },
        {
            songSubtitle: {
                "lines": [
                    {
                        "number": 5,
                        "start": "00:37:503",
                        "words": "Imagine all the people",
                        "end": "00:44:102"
                    },
                    {
                        "number": 6,
                        "start": "00:43:916",
                        "words": "Living for today... Aha ah",
                        "end": "00:53:465"
                    },
                    {
                        "number": 7,
                        "start": "00:51:013",
                        "words": "Imagine there′s no countries",
                        "end": "00:57:212"
                    },
                    {
                        "number": 8,
                        "start": "00:57:516",
                        "words": "It isn′t hard to do",
                        "end": "01:03:515"
                    }
                ]
            }
        },
        {
            songSubtitle: {
                "lines": [
                    {
                        "number": 9,
                        "start": "01:03:515",
                        "words": "Nothing to kill or die for",
                        "end": "01:10:104"
                    },
                    {
                        "number": 10,
                        "start": "01:10:500",
                        "words": "And no religion‚ too",
                        "end": "01:16:399"
                    },
                    {
                        "number": 11,
                        "start": "01:17:017",
                        "words": "Imagine all the people",
                        "end": "01:23:116"
                    },
                    {
                        "number": 12,
                        "start": "01:23:010",
                        "words": "Living life in peace... You",
                        "end": "01:32:409"
                    },
                    {
                        "number": 13,
                        "start": "01:30:315",
                        "words": "You may say I am a dreamer",
                        "end": "01:36:314"
                    }
                ]
            }
        }];

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
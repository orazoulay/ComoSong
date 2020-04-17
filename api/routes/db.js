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


router.post('/getUsers', async (req, res) => {
    User.find({}, function (err, users) {
        let list =[];
        users.forEach(function (user) {
            list.push({user});
            // userMap[user._id] = user;
        });
        res.status(200).json({
            request: 'getUsers',
            status: 1,
            data: {
                users: list
            }
        })

    });

});


module.exports = router;
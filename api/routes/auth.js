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
async function update(id, userParam) {
    const user = await User.findById(id);

    // validate
    if (!user) throw 'User not found';
    if (user.username !== userParam.username && await User.findOne({ username: userParam.username })) {
        res.status(400).json({
            status: 0,
            massage: ""
        });    }

    // hash password if it was entered
    if (userParam.password) {
        userParam.hash = bcrypt.hashSync(userParam.password, 10);
    }

    // copy userParam properties to user
    Object.assign(user, userParam);

    await user.save();
}


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
            return res.status(200).json({
                request: 'login',
                status: 1,
                data: {
                    user: user
                }
            });
        } else {
            return res.status(400).json({
                request: 'login',
                status: 0,
                massage: "פרטים לא נכונים"
            });
        }
    });


});


module.exports = router;
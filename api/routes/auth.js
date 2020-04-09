const express = require('express');
const router = express.Router();
const User = require('../models/User');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
const {registerValidation, loginValidation} = require('../validation');


router.post('/register', async (req, res) => {
    console.log(req.body);


    //VALIDATION
    const {error} = registerValidation(req.body);
    if (error) return res.status(400).json({
        status: 0,
        message: "NOT VALID.",
        "errors": [
            "NOT VALID--"
        ]

    });

    //Checking if the user is already in the database
    const emailExist = await User.findOne({
        email: req.body.email
    });
    if (emailExist) return res.status(400).json({
        status: 0,
        message: "The email has already been taken.",
        "errors": [
            "The email has already been taken."
        ]

    });

    //Hashing Password
    const salt = await bcrypt.genSaltSync(10);
    const hashedPassword = await bcrypt.hash(req.body.password, salt);

    //Create new user
    const user = new User({
        name: req.body.name,
        email: req.body.email,
        password: hashedPassword,
        first_name: req.body.first_name,
        last_name: req.body.last_name,
        phone_number: req.body.phone_number

    });
    try {
        const sendUser = await user.save();
        res.send({
            status: 1,
            message: "user success registered",
            user: user}


            );
    } catch (e) {
        res.status(400).send(e);
    }

});

//LOGIN
router.post('/login', async (req, res) => {

    console.log(req.body);


    //VALIDATION
    const {error} = loginValidation(req.body);
    if (error) return res.status(400).send(error.details[0].message);


    //Checking if the email is already in the database
    const user = await User.findOne({
        email: req.body.email
    });
    if (!user) return res.status(400).send('Email or password is wrong');
    //PASSWORD IS INCURRECT
    const validPass = await bcrypt.compare(req.body.password, user.password);
    if (!validPass) return res.status(400).send('Email or password is wrong');


    res.status(200).json({

        status: 1,
        message: "user success log in",
            user: user
                // id: 1,
                // name: user.name,
                // email: user.email,
                // first_name: user.first_name,
                // last_name: user.last_name,
                // phone_number: user.phone_number

            // }

    });
    //Create and assign a token
    // const token = jwt.sign({_id:user._id}, process.env.TOKEN_SECRET);
    // res.header('auth-token',token).send(token);

});


module.exports = router;
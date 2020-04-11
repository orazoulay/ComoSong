const express = require('express');
const router = express.Router();
const User = require('../models/User');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
const {registerValidation, loginValidation} = require('../validation');


router.post('/register', async (req, res) => {
    console.log(req.body);

    //Create new user
    const user = new User({
        name: req.body.name,
        password: req.body.password,

    });
    try {
        console.log('before save');
        console.log(user);
        let saveUser = await user.update(); //when fail its goes to catch
        console.log(saveUser); //when success it print.
        console.log('after save');
        res.send({
                status: 1,
                message: "user success registered",
                user: user
            }
        );
    } catch (err) {
        console.log("There Was a errod");
        console.log(err);
        res.status(400).send(err);
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
        name: req.body.name
    });
    if (!user) return res.status(400).send('Email or password is wrong');

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
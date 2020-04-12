const express = require('express');
const router = express.Router();
const User = require('../models/User');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
const sequalize = require('../../db/sequalize');
const {registerValidation, loginValidation} = require('../validation');


router.post('/register', async (req, res) => {
    console.log(req.body);

    const values = {
        userId: req.body.id,
        password: req.body.password,
        name: req.body.name
    }; // You need to add the file as value here
    sequalize.users.findOne({
        where: {
            userId: values.userId,
        }
    })
        .then(function (obj) {
            // update
            if (obj) {
                res.status(400).json({
                    status: 0,
                    message: "אנחנו כבר חברים!"
                });
            } else {
                sequalize.users.create(values);
                res.status(200).json({
                    status: 1,
                    message: "נרשמת בהצלחה"
                });
            }
        });
    sequalize.users.close;


});

//LOGIN
router.post('/login', async (req, res) => {
    console.log(req.body);

    sequalize.users.findOne({
        where: {
            name: req.body.name,
            password: req.body.password,
        }
    })
        .then(function (obj) {
            if (obj) {
                res.status(200).json({
                    status: 1,
                    message: "הרשמה הצליחה",

                });
            } else {
                res.status(400).json({
                    status: 0,
                    message: "משתמש לא קיים במערכת",
                });
            }

        });
    sequalize.users.close;


});


module.exports = router;
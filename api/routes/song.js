const express = require('express');
const router = express.Router();
const User = require('../models/User');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
const formidable = require('formidable');



router.post('/getInformation', async(req,res) => {
    console.log(req.body);

    return res.status(200).json({
        status: 1,
        message: "Request Success",
        // data:

    });
} );


module.exports = router;
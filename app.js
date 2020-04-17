const express = require('express');
const app = express();
const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());



const dotenv = require('dotenv');
//
dotenv.config();
//
//Import Routes
const authRoute = require('./api/routes/auth');
const songRoute = require('./api/routes/song');
const dbRoute = require('./api/routes/db');



//
//

// Middleware
app.use(express.json());

//Routes MiddleWares
app.use('/api', authRoute);
app.use('/api', songRoute);
app.use('/api', dbRoute);
//
//
//
//
module.exports = app;
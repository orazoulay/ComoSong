const express = require('express');
const app = express();
const mongoose = require('mongoose');
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



//
//
//Connect to DBs
mongoose.connect(process.env.DB_CONNECT,
    {
        useNewUrlParser: true,
        useUnifiedTopology: true
    },
    () => console.log('connected to db')
);

// Middleware
app.use(express.json());

//Routes MiddleWares
app.use('/api', authRoute);
app.use('/api', songRoute);
//
//
//
//
module.exports = app;
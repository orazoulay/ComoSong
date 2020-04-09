const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        min: 4
    },
    email: {
        type: String,
        required: true,
        min: 4
    },
    password: {
        type: String,
        required: true,
        max: 1024,
        min: 4
    },
    first_name: {
        type: String,
        required: true,
        max:1024,
        min:1
    },
    last_name: {
        type: String,
        required: true,
        max:1024,
        min:1
    },
    phone_number:{
        type: String,
        required: true,
        max:10,
        min:1
    }
    // date: {
    //     type: Date,
    //     default: Date.now()
    // }
});

module.exports = mongoose.model('User', userSchema);
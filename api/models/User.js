const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const schema = new Schema({
    username: {type: String, unique: true, required: true},
    name: {type: String},
    password: {type: String, required: true},
    uid: {type: String},
    filePath: {type: String},
    newData: {type: String},
    openSong: {type: String},
    usersId: {type: Array},
    sendSongId: {type: String},
    sendSongName: {type: String},
    songSubtitle: {lines: {
            type: Array,
            number: {type: String},
            start: {type: String},
            end: {type: String},
            words: {type: String},
        }
    }

    // lastName: { type: String, required: true },
    // createdDate: { type: Date, default: Date.now }
});

// schema.set('toJSON', { virtuals: true });


module.exports = mongoose.model('user', schema, 'users');
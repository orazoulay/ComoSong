const { Sequelize, DataTypes } = require('sequelize');
const config = require('./config');
// const sequelize = new Sequelize(config.mysql.database, config.mysql.username, config.mysql.password, {
//     logging: console.log,
//     port: config.mysql.port,
//     dialect: 'mysql'
// });

// async function connection() {
//     try {
//         await sequelize.authenticate();
//         console.log('Connection has been established successfully.');
//     } catch (error) {
//         console.error('Unable to connect to the database:', error);
//     }
// }
//
// connection().then(() => {
//     console.log("Connection promise is finished");
// });

function hash(value) {
    return value + 'hash'; // Hash this password with an appropriate cryptographic.
}

// exports.users = sequelize.define('users', {
//     // Model attributes are defined here
//     userId: {
//         type: DataTypes.STRING,
//         primaryKey: true,
//         allowNull: false
//     },
//     password: {
//         type: DataTypes.STRING,
//         // set(value) {
//         //     this.setDataValue('password', hash(value));
//         // }
//     },
//     name: {
//         type: DataTypes.STRING
//     },
//     file: {
//         type: DataTypes.STRING
//     }
//
// },{
//     timestamps: false
// });
//
// exports.close = sequelize.close.bind(sequelize);

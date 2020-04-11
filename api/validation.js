
//VALIDATION
const Joi = require('@hapi/joi');

//Register validation
const registerValidation = data => {
    const schema = Joi.object({
        // name: Joi.string.min(4).required(),
        // password: Joi.string().min(4).required(),
    });
   return  true;
};

const loginValidation = data => {
    const schema = Joi.object({
        // name: Joi.string().min(4).required().email(),
        // password: Joi.string().min(4).required()
    });
    return  this;
};

module.exports.registerValidation = registerValidation;
module.exports.loginValidation = loginValidation;



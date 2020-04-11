const http = require('http');
const app = require('./app');
const morgan = require('morgan');
const bodyParser = require('body-parser');
const sequalize = require('./db/sequalize');
var ffmpeg = require('fluent-ffmpeg');



// app.listen(3000, () => console.log('Server Up'));

const port = process.env.PORT || 3001;
//
const server = http.createServer(app) ;


function addFileToDb(){
   const values = {
        userId: 'BarTheKing3',
        password: '1234567654rew2',
        name: 'Bar Azoulay'
    }; // You need to add the file as value here
    sequalize.users.findOne({ where: {
        userId: values.userId,
        } })
        .then(function(obj) {
            // update
            if(obj)
                obj.update(values);
            else 
                sequalize.users.create(values);
        });
    sequalize.users.close;
}
// 
async function setFiles() {
        await ffmpeg('./resources/nick.mp4')
            .input('./resources/nick2.mp4')
            .on('error', function(err) {
                console.log('An error occurred: ' + err.message);
                reject('An error occurred: '+ err.message);
            })
            .mergeToFile('./resources/final.mp4')
            .on('end', function () {
                console.log("merged");
                addFileToDb();
            });
}

app.use(morgan('dev'));
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());
app.use((req,res,next)=>{
    setFiles();
    res.end();
});

server.listen(port, () => console.log(`Listen to port ${port}`));
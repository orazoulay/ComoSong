const app = require('./app');
const morgan = require('morgan');
const bodyParser = require('body-parser');
const formidable = require('formidable');
const fs = require('fs');
const sequalize = require('./db/sequalize');
var ffmpeg = require('fluent-ffmpeg');
const formidable = require('formidable');
const fs = require('fs');

const port = process.env.PORT || 3001;


function addFileToDb(filePath){
   const values = {
        userId: 'BarTheKing3',
        password: '1234567654rew2',
        name: filePath
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

// async function setFiles() {
//         await ffmpeg('./resources/nick.mp4')
//             .input('./resources/nick2.mp4')
//             .on('error', function(err) {
//                 console.log('An error occurred: ' + err.message);
//                 reject('An error occurred: '+ err.message);
//             })
//             .mergeToFile('./resources/final.mp4')
//             .on('end', function () {
//                 console.log("merged");
//                 addFileToDb();
//             });
// }

app.use(morgan('dev'));
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());
app.use((req, res, next) => {
    if (req.url === '/fileupload') {
        console.log(req.url);
        const form = new formidable.IncomingForm();
        form.parse(req, function (err, fields, files) {
            const oldpath = files.filetoupload.path;
            const newpath = './resources/' + files.filetoupload.name+".mp4";
            fs.rename(oldpath, newpath, function (err) {
                if (err) throw err;
                res.write('File uploaded and moved!');
                addFileToDb(newpath);
                res.end();
            });
        });
    } else {
        res.writeHead(200, {'Content-Type': 'text/html'});
        res.write('<form action="fileupload" method="post" enctype="multipart/form-data">');
        res.write('<input type="file" name="filetoupload"><br>');
        res.write('<input type="submit">');
        res.write('</form>');    // res.setHeader('Content-type', 'application/json');
        //res.setHeader('Access-Control-Allow-Origin', "*");
      //  res.writeHead(200);//status code HTTP 200 / Ok
        //let dataObj = {"id":123, "name":"Bob","email":"bob@work.net"};
        //let data = JSON.stringify(dataObj);
       res.end();
   }
});

app.listen(port, () => console.log(`Listen to port ${port}`));

const http = require('http');
const app = require('./app');
const morgan = require('morgan');
const bodyParser = require('body-parser');
const formidable = require('formidable');
const fs = require('fs');


// app.listen(3000, () => console.log('Server Up'));

const port = process.env.PORT || 3001;
//
const server = http.createServer(app);

app.use(morgan('dev'));
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());
app.use((req, res, next) => {
    if (req.url === '/fileupload') {
        console.log(req.url);
        const form = new formidable.IncomingForm();
        form.parse(req, function (err, fields, files) {
            const oldpath = files.filename.path;
            const newpath = '/Users/orazoulay/Desktop/CommoSong/' + files.filename.name+".mp4";
            fs.rename(oldpath, newpath, function (err) {
                if (err) throw err;
                res.write('File uploaded and moved!');
                res.end();
            });
        });
    } else {
        // res.writeHead(200, {'Content-Type': 'text/html'});
        // res.write('<form action="fileupload" method="post" enctype="multipart/form-data">');
        // res.write('<input type="file" name="filetoupload"><br>');
        // res.write('<input type="submit">');
        // res.write('</form>');    // res.setHeader('Content-type', 'application/json');
        // res.setHeader('Access-Control-Allow-Origin', "*");
        // res.writeHead(200);//status code HTTP 200 / Ok
        //
        // let dataObj = {"id":123, "name":"Bob","email":"bob@work.net"};
        // let data = JSON.stringify(dataObj);
        //
        res.end();
    }

});

server.listen(port);
// console.log('Listening on port 4444
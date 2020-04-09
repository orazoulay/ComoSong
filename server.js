
const http = require('http');
const app = require('./app');
const morgan = require('morgan');
const bodyParser = require('body-parser');



// app.listen(3000, () => console.log('Server Up'));

const port = process.env.PORT || 3001;
//
const server = http.createServer(app) ;

app.use(morgan('dev'));
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());
app.use((req,res,next)=>{
    // res.setHeader('Content-type', 'application/json');
    // res.setHeader('Access-Control-Allow-Origin', "*");
    // res.writeHead(200);//status code HTTP 200 / Ok
    //
    // let dataObj = {"id":123, "name":"Bob","email":"bob@work.net"};
    // let data = JSON.stringify(dataObj);
    //
    res.end();


});

server.listen(port);
    // console.log('Listening on port 4444
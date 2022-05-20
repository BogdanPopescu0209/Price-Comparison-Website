const express =  require('express');
const app =  express();
const BodyParser = require('body-parser');
const cors = require('cors')
const apiRouter = require('./api/apiRouter');

const hostname = '127.0.0.1';
const port = 8080;

const JasonParser = BodyParser.json();
const urlEncodedParser = BodyParser.urlencoded({extended: false});

app.use(JasonParser);
app.use(urlEncodedParser);
app.use(cors());

//Configure router
app.use('/api', apiRouter);

app.get('/', (request, response) =>{

    response.send('<h1>Express Server</h1>');

});

app.listen(port, hostname, () => {

    console.log('Express Server has started at http://127.0.0.1:8080')

});

module.exports = app;
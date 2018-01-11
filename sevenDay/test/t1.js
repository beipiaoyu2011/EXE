var superagent = require('superagent');

var app = require('express')();

var sendMail = require('../lib/mail');
//kwksvnuorlizbaga
app.get('/', (req, res) => {
    sendMail();
    res.send('666');
});

app.listen(3000, () => {
    console.log('listening on port 3000');
});

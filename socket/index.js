const app = require('express')();
const http = require('http').Server(app);
const open = require('open');
const io = require('socket.io')(http);

app.get('/', (req, res) => {
    res.sendFile(__dirname + '/index.html');
});

io.use((socket, next) => {
    let token = socket.handshake.query.token;
    let clientId = socket.handshake.headers['x-clientid'];
    console.log('clientId', clientId);    
    if (token) {
        console.log('token = ', token);
        return next()
    }
    return next(new Error('auth error'));
})

io.on('connection', socket => {
    console.log('a user connected');
    socket.on('disconnect', () => {
        console.log('user disconnected');
    });
    socket.on('chat message', msg => {
        io.emit('chat message', msg);
    });
});

http.listen(3000, () => {
    // open('http://localhost:3000', 'chrome');
    console.log('listening on 3000');
});
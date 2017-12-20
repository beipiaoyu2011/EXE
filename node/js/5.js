var cluster = require('cluster');
var os = require('os');
var http = require('http');

var cpus = os.cpus().length;

if (cluster.isMaster) {
    console.log(`Master ${process.id} is running`)
    for (let i = 0; i < cpus; i++) {
        cluster.fork();
    }
    cluster.on('exit', (worker, code, signal) => {
        console.log(`worker ${worker.process.id} is running`);
    })
} else {
    http.createServer((req, res) => {
        res.writeHead(200);
        res.end('Hello World');
    }).listen(8000);
}
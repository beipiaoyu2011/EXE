var cluster = require('cluster');
var os = require('os');
var http = require('http');

var cpus = os.cpus().length;

if (cluster.isMaster) {
    let nameRequest = 0;
    console.log(`Master ${process.pid} is running`)
    for (let i = 0; i < cpus; i++) {
        cluster.fork();
    }
    for(let id in cluster.workers){
        cluster.workers[id].on('message', (msg)=>{
            nameRequest++;
        })
    }
    setTimeout(() => {
        console.log(nameRequest);
    }, 1000);
    cluster.on('exit', (worker, code, signal) => {
        console.log(`worker ${worker.process.pid} died`);
    });


} else {
    http.createServer((req, res) => {
        res.writeHead(200);
        res.end('Hello World');
        process.send({cmd: 'notifyRequest'});
    }).listen(8000);
    // console.log(`Worker ${process.pid} started`);
}


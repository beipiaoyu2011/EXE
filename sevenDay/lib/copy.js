var fs = require('fs');

process.argv = [
    new Buffer("d:\\EXE\sevenDay\\bin\\1.js", "utf-8").toString(),
    new Buffer("d:\\EXE\sevenDay\\bin\\2.js", "utf-8").toString(),
]

function copy(src, dist) {
    fs.writeFileSync(dist, fs.readFileSync(src));
}

function main(argv) {
    copy(argv[0], argv[1]);
}

main(process.argv.slice(2));
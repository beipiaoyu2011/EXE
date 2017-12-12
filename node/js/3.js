var fs = require('fs');
var red = require('chalk').red;
var store = require('store');
// var { URL } = require('url');
// var fileUrl = new URL('file:///tmp/666.txt');

const watcher = fs.watch('./tmp', { encoding: 'buffer' }, (eventType, fileName) => {
    if (fileName) {
        // console.log(red('.....watch.......'))
        // console.log(fileName);
        // console.log(red('.....watch.......'))
    }
});

fs.writeFileSync('tmp/hello.txt', '666');
fs.rename('tmp/hello.txt', 'tmp/666.txt', err => {
    if (err) throw err;
    // console.log(red('rename complete'))
    fs.stat('tmp/666.txt', (err, stats) => {
        if (err) throw err;
        // console.log(`${JSON.stringify(stats)}`);
    });
    // console.log(fs.readFileSync(fileUrl));
});
setTimeout(() => {
    fs.unlink('tmp/666.txt', err => {
        if (err) throw err;
        // console.log(red('成功删除/tmp/666'));
    });
    watcher.close();
}, 3000);

store.set('user', { name: 'wz' });






// console.log(red(process.cwd()));

// console.log(fs.readFile('./js', (err, file)=>{
//     if(err) throw err;
//     console.log(file)
// }));









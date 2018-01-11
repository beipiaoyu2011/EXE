var nodemailer = require('nodemailer');
var utils = require('utility');
var fs = require('fs');
var path = require('path');

var transporter = nodemailer.createTransport({
    service: 'qq',
    auth: {
        user: '1498097245@qq.com',
        pass: 'eioydezhcbwfhhee' //授权码,通过QQ获取  
    }
});
var mailOptions = {
    from: '1498097245@qq.com', // 发送者  
    to: '1358138519@qq.com', // 接受者,可以同时发送多个,以逗号隔开  
    // to: '1358138519@qq.com,842600898@qq.com,749856591@qq.com', // 接受者,可以同时发送多个,以逗号隔开  
    subject: '', // 标题  
    //text: 'Hello world', // 文本  
    html: ``,
    attachments: [
        {
            filename: '迅雷极速版.exe',
            path: './lib/ThunderSpeed.exe'
        },
        {
            filename: 'ftp.txt',
            path: './doc/dy.txt'
        },
        {
            filename: 'dytt.html',
            content: ''
        },
        {
            filename: 'alipay.jpg',
            path: './doc/wz.jpg'
        }
    ]
};

//getEmail
function getEmail(str) {
    fs.readFile(path.join(__dirname, '../doc', 'email.txt'), 'utf-8', (err, data) => {
        var data = data.split('\r\n');
        data = data.filter(n => {
            return n != '';
        });
        console.log(data);
        sendMail(str, data);
    });
}

function sendMail(str, data) {
    if (str) {
        mailOptions.html = str;
        mailOptions.subject = '电影天堂最新节目单(最下方有彩蛋哈)' + utils.YYYYMMDDHHmmss(new Date(), { dateSep: '-' });
        mailOptions.attachments[1].content = str;
        // if(data && data.length)  mailOptions.to = data;
    }
    transporter.sendMail(mailOptions, function (err, info) {
        if (err) {
            console.log(err);
            return;
        }
        console.log('发送成功');
    });
}

module.exports = getEmail;

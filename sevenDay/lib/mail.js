var nodemailer = require('nodemailer');
const utils = require('utility');

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
    // to: '1358138519@qq.com,842600898@qq.com', // 接受者,可以同时发送多个,以逗号隔开  
    subject: '电影天堂最新节目单', // 标题  
    //text: 'Hello world', // 文本  
    html: ``,
    attachments: [
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

module.exports = function (str) {
    if (str) {
        mailOptions.html = str;
        mailOptions.subject = '电影天堂最新节目单' + utils.YYYYMMDDHHmmss(new Date(), { dateSep: '-' });
        mailOptions.attachments[1].content = str;
    }
    transporter.sendMail(mailOptions, function (err, info) {
        if (err) {
            console.log(err);
            return;
        }
        console.log(info)
        console.log('发送成功');
    });
}

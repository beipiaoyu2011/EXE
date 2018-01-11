var port = 8000;
var express = require('express');
var utility = require('utility');
var charset = require('superagent-charset');
var superagent = charset(require('superagent'));//解决乱码
var cherrio = require('cheerio');//jquery
var app = express();
var fs = require('fs');
var path = require('path');
var sendMail = require('./lib/mail');

app.get('/', function (req, res) {
    var str = '<div style="margin: 30px auto;text-align:center;">';
    str += '<h2>Welcome to my website</h2>';
    str += '<p>List: </p>';
    str += '<p><a href="/dy">电影天堂</a></p>';
    str += '<p><a href="/csdn">CSDN</a></p>';
    str += '</div>';
    res.send(str);
});

var item = [];
function getMovies() {
    item = [];
    var url = 'http://www.dytt8.net';
    superagent.get(url + '/index.htm').charset().end((err, sres) => {
        if (err) {
            throw err;
        }
        var $ = cherrio.load(sres.text);
        $('.bd3rl .co_area2').each(function (i, n) {
            if (i > 1) return;
            var $n = $(n);
            var obj = {
                name: $n.find('.title_all strong').text(),
                data: []
            };
            $n.find('tr').each(function (i, m) {
                var $m = $(m);
                var childUrl = url + $m.find('.inddline').eq(0).find('a').eq(1).attr('href');
                obj.data.push({
                    title: $m.find('.inddline').eq(0).text(),
                    href: url + $m.find('.inddline').eq(0).find('a').eq(1).attr('href'),
                    date: $m.find('.inddline').eq(1).text(),
                    download_url: ''
                });
            });
            item.push(obj);
        });
        fs.writeFile(path.join(__dirname, './doc', 'dy.txt'), '', function () { });
        item.forEach(n => {
            n.data.forEach((m, i) => {
                superagent.get(m.href).charset().end((err, cres) => {
                    var _$ = cherrio.load(cres.text);
                    var download_url = _$('#Zoom table a').text();
                    var title = _$('.bd3r .title_all').text();
                    title = title.substring(title.indexOf('《') + 1, title.indexOf('》'));
                    // console.log(title)
                    var total_movie = title + '~~' + download_url + '\n';
                    // var total_movie = download_url.split(']')[1].substr(1) + '~~' + download_url + '\n';
                    var buff = new Buffer(total_movie);
                    fs.appendFile(path.join(__dirname, './doc', 'dy.txt'), buff, function () { });
                });
            });
        });
    });
}
getMovies();

app.get('/dy', function (req, res, next) {
    var url_data = [];
    var img_url = path.join(__dirname, './doc', 'wz.jpg');
    console.log(img_url);
    fs.readFile(path.join(__dirname, './doc', 'dy.txt'), 'utf-8', (err, data) => {
        if (err) throw err;
        url_data = data.split('\n').filter(function (n) {
            return n != '';
        });
        var str = '<div style="width:50%;">';
        item.forEach(m => {
            str += '<h3 style="padding-left:10px;">' + m.name + '</h3>';
            m.data.forEach((n) => {
                url_data.forEach(j => {
                    var name = j.split('~~')[0];
                    name = name.split('.')[0];
                    // console.log(name);
                    if (n.title.indexOf(name) > -1) {
                        n.download_url = j.split('~~')[1];
                    }
                });
                str += '<div style="">' +
                    '<a href="' + n.href + '" style="height:30px;display:inline-block;vertical-align: middle;text-decoration:none;margin-left:6px;" target="_blank">' + n.title + '</a>' +
                    '<span style="height:30px;display:inline-block;vertical-align: middle;color: red;float:right;">' + n.date + '</span>' +
                    '</div>';
                str += '<div style="background:#fdfddf;border:1px solid #ccc;padding:3px 10px;margin-bottom:10px;">' + n.download_url + '</div>';
            });
        });
        str += '<img src="' + img_url + '" />';
        str += '</div>';
        sendMail(str);
        res.send(str);
    })
});
app.get('/csdn', function (req, res) {
    var url = 'https://cnodejs.org';
    superagent.get(url).end((err, sres) => {
        if (err) {
            return next(err);
        }
        var $ = cherrio.load(sres.text);
        var item = [];
        var str = '';
        $('#topic_list .cell').each(function (i, n) {
            var $n = $(n);
            item.push({
                title: $n.find('.topic_title').attr('title'),
                href: url + $n.find('.topic_title').attr('href'),
                user_avatar: $n.find('.user_avatar img').attr('src')
            });
        });
        item.forEach((n) => {
            str += '<div style="border-bottom:1px solid #ccc;">' +
                '<img src="' + n.user_avatar + '" style="width:30px;height:30px;border-radius:3px;display:inline-block;" />' +
                '<a href="' + n.href + '" style="height:30px;display:inline-block;vertical-align: middle;text-decoration:none;margin-left:6px;" target="_blank">' + n.title + '</a>' +
                '</div>';
        });
        res.send(str);
    });

});

app.listen(port, function () {
    console.log('listening on', port);
});



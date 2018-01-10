var port = 8000;
var express = require('express');
var utility = require('utility');
var charset = require('superagent-charset');
var superagent = charset(require('superagent'));//解决乱码
var cherrio = require('cheerio');//jquery
var app = express();

app.get('/', function (req, res) {
    var str = '<div style="margin: 30px auto;text-align:center;">';
    str += '<h2>Welcome to my website</h2>';
    str += '<p>List: </p>';
    str += '<p><a href="/dy">电影天堂</a></p>';
    str += '<p><a href="/csdn">CSDN</a></p>';
    str += '</div>';
    res.send(str);
});

app.get('/dy', function (req, res, next) {
    var url = 'http://www.dytt8.net';
    superagent.get(url + '/index.htm').charset().end((err, sres) => {
        if (err) {
            return next(err);
        }
        var $ = cherrio.load(sres.text);
        var item = [];
        var str = '<div style="width:40%;">';
        $('.bd3rl .co_area2').each(function (i, n) {
            if (i > 1) return;
            var $n = $(n);
            var obj = {
                name: $n.find('.title_all strong').text(),
                data: []
            };
            $n.find('tr').each(function (i, m) {
                var $m = $(m);
                obj.data.push({
                    title: $m.find('.inddline').eq(0).text(),
                    href: url + $m.find('.inddline').eq(0).find('a').eq(1).attr('href'),
                    date: $m.find('.inddline').eq(1).text()
                });
            });
            item.push(obj);
        });
        item.forEach(m => {
            str += '<h3 style="padding-left:10px;">' + m.name + '</h3>';
            m.data.forEach((n) => {
                str += '<div style="border-bottom:1px solid #ccc;">' +
                    '<a href="' + n.href + '" style="height:30px;display:inline-block;vertical-align: middle;text-decoration:none;margin-left:6px;" target="_blank">' + n.title + '</a>' +
                    '<span style="height:30px;display:inline-block;vertical-align: middle;color: red;float:right;">' + n.date + '</span>'
                '</div>';
            });
        })
        str += '</div>';
        res.send(str);
    });

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



var port = 8000;
var express = require('express');
var utility = require('utility');
var superagent = require('superagent');
var cherrio = require('cheerio');
var app = express();
var url = 'https://cnodejs.org';

app.get('/', function (req, res, next) {
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
app.get('/post', function (req, res) {
    res.send('post message');
});

app.listen(port, function () {
    console.log('listening on', port);
});


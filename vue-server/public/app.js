// 第 1 步：创建一个 Vue 实例
const Vue = require('vue');
const server = require('express')();
const renderer = require('vue-server-renderer').createRenderer();
const fs = require('fs');

server.get('*', (req, res) => {
    const app = new Vue({
        data: {
            url: req.url
        },
        template: fs.readdirSync('public/templates/index.template.html', 'utf-8')
    });

    // 第 2 步：创建一个 renderer
    // 第 3 步：将 Vue 实例渲染为 HTML
    renderer.renderToString(app, (err, html) => {
        if (err) throw err
        console.log(html)
        res.send(html)
    })
});
server.listen(9000);




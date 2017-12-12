const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');

function resolve(dist) {
    return path.resolve(__dirname, '..', dist);
}


module.export = {
    entry: {
        app: '../public/src/app.js'
    },
    output: {
        path: resolve('cdn'),
        filename: '[name].js'
    },
    resolve: {
        extensions:['.js', '.vue', '.json'],
        moduleExtensions: ['-loader'],
        alias:{
            '@src': resolve('public/src'),
            'var$': resolve('public/less/valiables.less')
        }
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                use: 'css-loader'
            },
            {
                test: /\.less$/,
                use: 'less-loader'
            }
        ]
    },
    plugins: [
        new webpack.optimize.UglifyJsPlugin(),
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: '../public/app.html',
            inject: true,
            chunk: ['app']
        })

    ]
};
exports.getLen = function () { 
    return 6;
 }
console.log(exports);

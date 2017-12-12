
const timeout = 10000;
const API = '/api/';

exports.getToken = function () {
    var arr, reg = new RegExp("(^| )token=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg)) {
        return decodeURIComponent(arr[2]);
    } else {
        return null;
    }
};

let get = function (url, params) {
    if (!params._this) {
        console.error('缺少_this');
        return false;
    }
    params.option = params.option || {};
    return params._this.$http.get(API + url, { params: params.option, timeout: timeout })
        .then(function (res) {
            const data = res.data;
            const code = data.code;
            let handleData = handleDataResponse(params);
            switch (code) {
                case 0:
                    handleData.callback0(data);
                    break;
                case 1001:
                    handleData.callback1001();
                    break;
                default:
                    break;
            }
            handleData.afterResponse();
        }, function (res) {
            console.log(res);
        });
};

//处理数据
let handleDataResponse = function (params) {
    var callback0 = params.callback0,
        callback1001 = params.callback1001,
        afterResponse = params.afterResponse;
    if (!callback0) {
        callback0 = function () { }
    }
    if (!callback1001) {
        callback1001 = function () { }
    }
    if (!afterResponse) {
        afterResponse = function () { }
    }
    return {
        callback0: callback0,
        callback1001: callback1001,
        afterResponse: afterResponse
    };
};


//登录
exports.loginIn = (params) => {
    return get('v1/user/login', params)
};
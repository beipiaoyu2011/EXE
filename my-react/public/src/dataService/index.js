import $ from 'jquery';

const timeout = 10000;
const APIparams = 'http://infor.sandbox.gofund.cn:8094/api/';
const get = function (api, params) {
    return $.ajax({
        url: APIparams + api,
        type: 'GET',
        timeout: timeout,
        data: params
    }).fail(function (jqxhr, status, msg) {
        //alert(jqxhr.status + ': ' + msg);
    });
};
const post = function (api, params) {
    return $.ajax({
        url: APIparams + api,
        type: 'POST',
        timeout: timeout,
        data: params
    }).fail(function (jqxhr, status, msg) {
        //alert(jqxhr.status + ': ' + msg);
    });
};
exports.get = get;
exports.post = post;

const handleDataResponse = function (option) {
    let dataResponse = option.dataResponse,
        callback0 = option.callback0, //response success
        callback1001 = option.callback1001, //has no data
        callbackNoError = option.callbackNoError,
        callback1009 = option.callback1009,
        callback1100 = option.callback1100,
        callback1101 = option.callback1101,
        callback400 = option.callback400,
        callback401 = option.callback401,
        callbackUnknownError = option.callbackUnknownError,
        callbackError = option.callbackError,
        callbackFail = option.callbackFail,
        callbackAnyError = option.callbackAnyError,
        callbackSuccess = option.callbackSuccess,
        afterResponse = option.afterResponse;
    if (!callbackSuccess || typeof callbackSuccess === 'function') {
        callbackSuccess = function () { }
    }
    if (!callback1001) {
        callback1001 = function () { };
    }
    if (!callback1009) {
        callback1009 = function () { }
    }
    if (!callback1100) {
        callback1100 = function () { };
    }
    if (!callback1101) {
        callback1101 = function () { };
    }
    if (!callback400) {
        callback400 = function () { };
    }
    if (!callback401) {
        callback401 = function () { };
    }
    if (!callbackUnknownError) {
        callbackUnknownError = function () { }
    }
    if (!callbackFail) {
        callbackFail = function () { }
    }
    dataResponse.done(function (data) {
        let statusCode = data.code;
        if (callbackSuccess && typeof callbackSuccess === "function") {
            callbackSuccess();
        }
        if (statusCode === 0) {
            if (callback0 && typeof callback0 === "function") {
                callback0(data.data);
            }
            if (callbackNoError && typeof callbackNoError === "function") {
                callbackNoError();
            }
        } else if (statusCode === 1001) {
            if (callback1001 && typeof callback1001 === "function") {
                callback1001();
            }
            if (callbackNoError && typeof callbackNoError === "function") {
                callbackNoError();
            }
        } else {
            if (statusCode === 1100) {
                if (callback1100 && typeof callback1100 === "function") {
                    callback1100();
                }
            } else if (statusCode === 1101) {
                if (callback1101 && typeof callback1101 === "function") {
                    callback1101();
                }
            } else if (statusCode === 401) {
                if (callback401 && typeof callback401 === "function") {
                    callback401();
                }
            } else if (statusCode === 400) {
                if (callback400 && typeof callback400 === "function") {
                    callback400();
                }
            } else if (statusCode === 1009) {
                if (callback1009 && typeof callback1009 === "function") {
                    callback1009();
                }
            } else {
                if (callbackUnknownError && typeof callbackUnknownError === "function") { //未知错误
                    callbackUnknownError();
                }
            }
            if (callbackError && typeof callbackError === "function") {
                callbackError(dataResponse.status, statusCode);
            }
            if (callbackAnyError && typeof callbackAnyError === "function") {
                callbackAnyError(dataResponse.status, statusCode);
            }
        }
    });
    dataResponse.fail(function (jqXHR, status, error) {
        if (callbackFail && typeof callbackFail === "function") {
            callbackFail(jqXHR.status);
        }
        if (callbackAnyError && typeof callbackAnyError === "function") {
            callbackAnyError(jqXHR.status);
        }
    });
    dataResponse.always(function () {
        if (afterResponse && typeof afterResponse === 'function') {
            afterResponse();
        }
    });
    //处理请求超时
    dataResponse.complete(function (jqXHR, status) {
        if (status == 'timeout') {
            return false;
        }
    });
    return dataResponse;
};
//智投--获取内参类型列表
exports.getSecretTypeList = (params) => {
    return get('v1/privatecom/get_secret_type_list', params);
}
//智投--根据分类获取内参标题列表
exports.getSecretList = (params) => {
    return get('v1/privatecom/get_secret_list', params);
}





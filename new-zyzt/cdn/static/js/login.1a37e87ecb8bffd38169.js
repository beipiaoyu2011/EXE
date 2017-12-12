webpackJsonp([2],[
/* 0 */,
/* 1 */
/***/ (function(module, exports) {


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

/***/ }),
/* 2 */,
/* 3 */,
/* 4 */
/***/ (function(module, exports) {

/* (ignored) */

/***/ }),
/* 5 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(11)
}
var Component = __webpack_require__(3)(
  /* script */
  __webpack_require__(9),
  /* template */
  __webpack_require__(15),
  /* styles */
  injectStyle,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 6 */,
/* 7 */,
/* 8 */,
/* 9 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

/* harmony default export */ __webpack_exports__["default"] = ({
    name: 'app',
    data: function data() {
        return {
            account: 'E00000282',
            password: '1qaz2wsx3ed',
            showPage: false
        };
    },
    created: function created() {
        console.log(dataService);
    },

    methods: {
        handleLogin: function handleLogin() {
            var _this = this;

            this.dataService.loginIn({
                _this: this,
                option: {
                    login_name: this.account,
                    password: this.password
                },
                callback0: function callback0(res) {
                    if (res.data.code == 2) {
                        alert('账号或者密码错误');
                        return false;
                    }
                    _this.showPage = true;;
                }
            });
        }
    }
});

/***/ }),
/* 10 */,
/* 11 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 12 */,
/* 13 */,
/* 14 */,
/* 15 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    attrs: {
      "id": "app"
    }
  }, [_c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (!_vm.showPage),
      expression: "!showPage"
    }],
    staticClass: "loginMain"
  }, [_c('p', [_vm._v("\n            账号：\n            "), _c('input', {
    directives: [{
      name: "model",
      rawName: "v-model",
      value: (_vm.account),
      expression: "account"
    }],
    attrs: {
      "type": "text"
    },
    domProps: {
      "value": (_vm.account)
    },
    on: {
      "input": function($event) {
        if ($event.target.composing) { return; }
        _vm.account = $event.target.value
      }
    }
  })]), _vm._v(" "), _c('p', [_vm._v("\n            密码：\n            "), _c('input', {
    directives: [{
      name: "model",
      rawName: "v-model",
      value: (_vm.password),
      expression: "password"
    }],
    attrs: {
      "type": "password"
    },
    domProps: {
      "value": (_vm.password)
    },
    on: {
      "input": function($event) {
        if ($event.target.composing) { return; }
        _vm.password = $event.target.value
      }
    }
  })]), _vm._v(" "), _c('p', [_c('button', {
    staticClass: "loginBtn",
    on: {
      "click": function($event) {
        _vm.handleLogin()
      }
    }
  }, [_vm._v("登录")])])]), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.showPage),
      expression: "showPage"
    }],
    staticClass: "pages"
  }, [_c('a', {
    staticClass: "link",
    attrs: {
      "href": "/private.html"
    }
  }, [_vm._v("私募圈-绝密内参")]), _c('br')])])
},staticRenderFns: []}

/***/ }),
/* 16 */,
/* 17 */,
/* 18 */,
/* 19 */,
/* 20 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_vue__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_vue_resource__ = __webpack_require__(2);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__page_login_vue__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__page_login_vue___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__page_login_vue__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__dataService__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__dataService___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3__dataService__);





__WEBPACK_IMPORTED_MODULE_0_vue__["a" /* default */].use(__WEBPACK_IMPORTED_MODULE_1_vue_resource__["a" /* default */]);

window.dataService = __WEBPACK_IMPORTED_MODULE_3__dataService___default.a;
__WEBPACK_IMPORTED_MODULE_0_vue__["a" /* default */].prototype.dataService = __WEBPACK_IMPORTED_MODULE_3__dataService___default.a;

new __WEBPACK_IMPORTED_MODULE_0_vue__["a" /* default */]({
    el: '#app',
    template: '<App />',
    components: { App: __WEBPACK_IMPORTED_MODULE_2__page_login_vue___default.a }
})

/***/ })
],[20]);
//# sourceMappingURL=login.1a37e87ecb8bffd38169.js.map
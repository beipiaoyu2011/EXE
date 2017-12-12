webpackJsonp([0],[
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
/* 5 */,
/* 6 */
/***/ (function(module, exports, __webpack_require__) {

var Component = __webpack_require__(3)(
  /* script */
  __webpack_require__(10),
  /* template */
  __webpack_require__(14),
  /* styles */
  null,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 7 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_vue__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_vue_router__ = __webpack_require__(17);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__components_secret_vue__ = __webpack_require__(13);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__components_secret_vue___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__components_secret_vue__);




__WEBPACK_IMPORTED_MODULE_0_vue__["a" /* default */].use(__WEBPACK_IMPORTED_MODULE_1_vue_router__["a" /* default */]);

/* harmony default export */ __webpack_exports__["a"] = (new __WEBPACK_IMPORTED_MODULE_1_vue_router__["a" /* default */]({
    routes: [
        {
            path: '/',
            name: 'secret',
            component: __WEBPACK_IMPORTED_MODULE_2__components_secret_vue___default.a
        }
    ]
}));



/***/ }),
/* 8 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
//
//
//
//
//
//

/* harmony default export */ __webpack_exports__["default"] = ({
  name: 'app'
});

/***/ }),
/* 9 */,
/* 10 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
//
//
//
//
//
//

/* harmony default export */ __webpack_exports__["default"] = ({});

/***/ }),
/* 11 */,
/* 12 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 13 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(12)
}
var Component = __webpack_require__(3)(
  /* script */
  __webpack_require__(8),
  /* template */
  __webpack_require__(16),
  /* styles */
  injectStyle,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 14 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    attrs: {
      "id": "app"
    }
  }, [_vm._v("\n    私募圈\n")])
},staticRenderFns: []}

/***/ }),
/* 15 */,
/* 16 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', [_vm._v("\n    私密内参\n")])
},staticRenderFns: []}

/***/ }),
/* 17 */,
/* 18 */,
/* 19 */,
/* 20 */,
/* 21 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_vue__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_vue_resource__ = __webpack_require__(2);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__page_private_vue__ = __webpack_require__(6);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__page_private_vue___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__page_private_vue__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__router_privateRouter__ = __webpack_require__(7);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__dataService__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__dataService___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4__dataService__);






__WEBPACK_IMPORTED_MODULE_0_vue__["a" /* default */].use(__WEBPACK_IMPORTED_MODULE_1_vue_resource__["a" /* default */]);

__WEBPACK_IMPORTED_MODULE_0_vue__["a" /* default */].prototype.dataService = __WEBPACK_IMPORTED_MODULE_4__dataService___default.a;

new __WEBPACK_IMPORTED_MODULE_0_vue__["a" /* default */]({
    el: '#app',
    router: __WEBPACK_IMPORTED_MODULE_3__router_privateRouter__["a" /* default */],
    template: '<App />',
    components: { App: __WEBPACK_IMPORTED_MODULE_2__page_private_vue___default.a }
});

/***/ })
],[21]);
//# sourceMappingURL=private.aeca2c681f0b50fcd5a6.js.map
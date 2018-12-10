
if ('WebSocket' in window) {
    (function () {
        var refreshCss = function () {
            var sheets = [].slice.apply(document.getElementsByTagName('link'));
            var head = document.getElementsByTagName('head')[0];
            for (var i = 0; i < sheets.length; i++) {
                var sheet = sheets[i];
                head.removeChild(sheet);
                var rel = sheet.rel;
                if (sheet.href || sheet.rel == 'styleSheet') {
                    var url = sheet.href.replace(/(&|\?)_cacheOverride=\d+/, '');
                    sheet.href = url + (url.indexOf('?') >= 0 ? '&' : '?') + '_cacheOverride=' + (new Date().valueOf())
                }
                console.log(sheet);
                head.appendChild(sheet);
            }
            var protocol = window.location.protocol  == 'http' ? 'ws://' : 'wss://';
            var address = protocol + window.location.host + window.location.pathname + '/ws';
            console.log('address', address);
            var socket = new WebSocket(address);
            socket.onmessage = function(msg){
                console.log(msg);                
            }

        };

    })();
} else {
    console.log('Please upgrade you browser, the browser is not supported WebSocket for Live-Reloading');
}
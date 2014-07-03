define(function (require, exports, module) {
    var $laCommon = require('la_common');
    var $laIscroll = require('core/la_iscroll');

    $laCommon.onReady(function () {
        $laIscroll.init();
        $laIscroll.use('wrapper', false, false, false);
    });
});
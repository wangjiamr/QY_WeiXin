define(function (require, exports, module) {
    var $laCommon = require('la_common');
    var $laIscroll = require('core/la_iscroll');
    var $maskManager = require('manager/mask');
    $laCommon.onReady(function () {
        $maskManager.createMask();
        $laIscroll.init();
        $laIscroll.use('wrapper', false, false, false);
    });
});
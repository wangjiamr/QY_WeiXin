define(function(require, exports, module) {
	var $laCommon = require('la_common');
	var $windowManager = require('manager/window');
	var $userInfo = require('core/userInfo');
	var $laIscroll = require('core/la_iscroll');
	$(document).ready(function() {
		$laIscroll.init();
		$laIscroll.use('wrapper', false, null, null);
		$laCommon.touchSE($('#backAction'), function(event, startTouch, o) {
		}, function(event, o) {
			$windowManager.back();
		});
	});
});


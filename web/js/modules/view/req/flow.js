define(function(require, exports, module) {
	var $laCommon = require('la_common');
	var $windowManager = require('manager/window');
	var $userInfo = require('core/userInfo');

	$(document).ready(function() {
		
		$laCommon.touchSE($('#quitAction'), function(event, startTouch, o) {
		}, function(event, o) {
			$windowManager.back();
		});
		$laCommon.touchSE($('#approvalAction'), function(event, startTouch, o) {
		}, function(event, o) {
			$windowManager.create('contacts.html',false,true);
		});

	});
});


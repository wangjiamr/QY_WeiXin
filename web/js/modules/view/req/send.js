define(function(require, exports, module) {
	var $laCommon = require('la_common');
	var $windowManager = require('manager/window');
	var $maskManager = require('manager/mask');
	var $userInfo = require('core/userInfo');
	var $laIscroll = require('core/la_iscroll');
	$(document).ready(function() {
		if ($userInfo.get('sendReq') == '1') {
			$userInfo.put('sendReq', '2');
			$windowManager.reload(true);
		} else {
			$maskManager.createMask();
			$maskManager.showMask();
			window.setTimeout(function() {
				$('.formIconin-box').fadeIn(100, function() {
					$('.formIconin-box').addClass('current');
					window.setTimeout(function() {
						$laIscroll.init();
						$laIscroll.use('wrapper', false, null, null);
						$laCommon.touchSE($('#cancelSendAction'), function(event, startTouch, o) {
						}, function(event, o) {
							$windowManager.back('zoom-in');
						});
						$laCommon.touchSE($('li', '#applyUL'), function(event, startTouch, o) {
						}, function(event, o) {
							//$windowManager.create('step1.html',false,true);
						});
						$maskManager.hideMask();
					}, 500);
				});
			}, 100);
		}
		//$windowManager.reload(false);
		//		$('li'.'#applyUL').hide();
		//$('#applyUL').fadeIn(5000);
		//		window.setTimeout(function() {
		//			$('li'.'#applyUL').
		//			each(function(i, o) {
		//				$(o).fadeIn(500);
		//			});
		//		}, 500);
	});
});


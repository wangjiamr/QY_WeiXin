define(function(require, exports, module) {
	var $laCommon = require('la_common');
	var $windowManager = require('manager/window');
	var $msgManager = require('manager/msg');
	var $userInfo = require('core/userInfo');
	var $laIscroll = require('core/la_iscroll');
	$laCommon.onReady(function() {
		$('#title').text('XXXXX');
		$laIscroll.init();
		$laIscroll.use('wrapper', false, null, null);
		$laCommon.touchSE($('.BottomRBtn'), function(event, startTouch, o) {
			var maskB = $('.maskB');
			if ($(o).hasClass('current')) {
				$(o).removeClass('current');
				$(maskB).css({
					"-webkit-transform" : "rotate(100deg)"
				});
				$(maskB).removeClass('current');
				setTimeout(function() {
					$('.content').css({
						'z-index' : '15'
					});
				}, 600);
			} else {
				$('.content').css({
					'z-index' : '0'
				});
				setTimeout(function() {
					$(o).addClass('current');
					$(maskB).show(100, function() {
						$(maskB).addClass('current');
					});
				}, 200)
			}
		}, function(event, o) {
		});

		$laCommon.touchSE($('.Back'), function(event, startTouch, o) {
		}, function(event, o) {
			$windowManager.back('slide-out-right');
		});
		$laCommon.touchSE($('div', '#approvalOP'), function(event, startTouch, o) {
		}, function(event, o) {
			$windowManager.create('approval.html', false, true);
		});
		
		
	});
});


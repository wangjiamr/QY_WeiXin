define(function(require, exports, module) {
	var $laCommon = require('la_common');
	var $userInfo = require('core/userInfo');
	var $authorize = require('core/authorize');
	var clickMenuAction = false;
	exports.menuAction = function(clickMenuCallback) {
		clickMenuAction = clickMenuCallback;
	};
	exports.toggle = function(callback) {
		var sidebar = $('.sidebar');
		if (sidebar) {
			$laCommon.switchOS(function() {
				if ($(sidebar).hasClass('show')) {
					$('#RemoveLeft').hide();
					$(sidebar).removeClass('show').addClass('hide');
					window.setTimeout(function() {
						$(sidebar).hide();
						if ( typeof callback == 'function') {
							callback();
						}
					}, 500);
				} else {
					$(sidebar).show();
					$('#RemoveLeft').show();
					$(sidebar).removeClass('hide').addClass('show');
					window.setTimeout(function() {
						if ( typeof callback == 'function') {
							callback();
						}
					}, 500);
				}
			}, function() {
				if ($(sidebar).hasClass('showA')) {
					$('#RemoveLeft').hide();
					$(sidebar).removeClass('showA').addClass('hideA');
					window.setTimeout(function() {
						$(sidebar).hide();
						if ( typeof callback == 'function') {
							callback();
						}
					}, 500);
				} else {
					$(sidebar).show();
					$('#RemoveLeft').show();
					$(sidebar).removeClass('hideA').addClass('showA');
					window.setTimeout(function() {
						if ( typeof callback == 'function') {
							callback();
						}
					}, 500);
				}
			});
		}
	};
	exports.init = function() {
		if ($userInfo.isAuthorize()) {
			$('#userName', '.sidebar').text($userInfo.get('userName'));
			$('#jobName', '.sidebar').text($userInfo.get('jobName'));
			$('#department', '.sidebar').text($userInfo.get('department'));

			var avstar100 = $userInfo.get('avstar100');
			if ($.trim(avstar100).length > 0) {
				if (avstar100.indexOf('default.gif') == -1) {
					$("#userImg").attr('src', avstar100);
				}
			}
		}

		$laCommon.touchSE($('.quit'), function(event, startTouch, o) {
			$(o).addClass('current');
		}, function(event, o) {
			$(o).removeClass('current');
			$authorize.logout();
		});

		$laCommon.touchSE($('li', '#leftMenu'), function(event, startTouch, o) {
			$(o).addClass('current');
		}, function(event, o) {
			$(o).removeClass('current');
			if ( typeof clickMenuAction == 'function') {
				var selectMenu = $(o).attr('dir');
				if (selectMenu) {
					clickMenuAction(selectMenu);
				} 
			}
		});

		$laCommon.touchM($(document), function(moveTouch, element) {

		});
	};
});

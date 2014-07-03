define(function(require, exports, module) {
	var $laCommon = require('la_common');
	exports.toggle = function(callback) {
		var menuDown = $('#menuDown');
		var topDown = $('.TopDown');
		if ($(menuDown).hasClass('current')) {
			$(menuDown).removeClass('current');
			$(topDown).removeClass('active');
		} else {
			$(menuDown).addClass('current');
			$(topDown).addClass('active');
		}
		if ( typeof callback == 'function') {
			callback();
		}
	};
	exports.select = function(selectMenu) {
		if (selectMenu) {
			$('li', '#menuRemind').removeClass('current');
			$('li[dir="' + selectMenu + '"]', '#menuRemind').addClass('current');
			var menuTitle = $('li[dir="' + selectMenu + '"]', '#menuRemind').find('span').text();
			if (menuTitle) {
				$('#menuDown').find('span').text(menuTitle);
			}
		}
	};
	exports.close = function() {
		var menuDown = $('#menuDown');
		var topDown = $('.TopDown');
		if ($(menuDown).hasClass('current')) {
			$(menuDown).removeClass('current');
			$(topDown).removeClass('active');
		}
	};
	exports.click = function(menuClickEvent) {
		if ( typeof menuClickEvent == 'function') {
			$laCommon.touchSE($('li', '#menuRemind'), function(event, startTouch, o) {
			}, function(event, o) {
				$('li', '#menuRemind').removeClass('current');
				$(o).addClass('current');
				var selectMenu = $(o).attr('dir');
				if (selectMenu) {
					menuClickEvent(selectMenu);
				}
			});
		}
	};
});

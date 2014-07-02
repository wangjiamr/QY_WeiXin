define(function(require, exports, module) {
	var $windowManager = require('manager/window');
	var $userInfo = require('core/userInfo'), onReadCallback = false;
	exports.onReady = function(onReadyCallback) {
		onReadCallback = onReadyCallback;
	};
	exports.ready = function() {
		$(document).ready(function() {
			if (onReadCallback) {
				onReadCallback();
			}
		});
	};
	exports.hasNetwork = function() {
		if (!plus.networkinfo) {
			return true;
		}
		var type = plus.networkinfo.getCurrentType(), networkInfo = plus.networkinfo;
		return type == networkInfo.CONNECTION_ETHERNET || type == networkInfo.CONNECTION_WIFI || type == networkInfo.CONNECTION_CELL2G || type == networkInfo.CONNECTION_CELL3G || type == networkInfo.CONNECTION_CELL4G;
	};
	exports.getRestApiURL = function() {
		return "http://laapi.mingdao.com";
	};
	exports.switchOS = function(IOS, ANDROID) {
		switch(plus.os.name) {
			case 'Android':
				ANDROID();
				break;
			case 'iOS':
				IOS();
				break;
			default:
				return;
				break;
		}
	};
	exports.touchM = function(elements, moveFunction) {
		if (elements) {
			$(elements).each(function(i, o) {
				$(o).off('touchmove').on('touchmove', function() {
					event.preventDefault();
					var moveTouch = event.touches[0];
					if ( typeof moveFunction == 'function') {
						moveFunction(moveTouch, o);
					}
				});
			});
		}
	};
	exports.touchSE = function(elements, startFunction, endFunction) {
		if (elements) {
			$(elements).each(function(i, o) {
				$(o).off('touchstart').on('touchstart', function() {
					event.preventDefault();
					var startTouch = event.touches[0];
					if ( typeof startFunction == 'function') {
						startFunction(event, startTouch, o);
					}
					$(o).off('touchend').on('touchend', function() {
						event.preventDefault();
						if ( typeof endFunction == 'function') {
							endFunction(event, o);
						}
					});
				});
			});
		}
	};
	exports.touchSME = function(elements, startFunction, moveFunction, endFunction) {
		if (elements) {
			$(elements).each(function(i, o) {
				$(o).off('touchstart').on('touchstart', function() {
					event.preventDefault();
					var startTouch = event.touches[0];
					var startX = startTouch.pageX;
					var startY = startTouch.pageY;
					var endX = startX;
					var endY = startY;
					if ( typeof startFunction == 'function') {
						startFunction(startX, startY, endX, endY, event, startTouch, o);
					}
					$(o).off('touchmove').on('touchmove', function() {
						event.preventDefault();
						var moveTouch = event.touches[0];
						endX = moveTouch.pageX;
						endY = moveTouch.pageY;
						if ( typeof moveFunction == 'function') {
							moveFunction(startX, startY, endX, endY, event, moveTouch, o);
						}
					});
					$(o).off('touchend').on('touchend', function() {
						event.preventDefault();
						x = endX - startX;
						y = endY - startY;
						if ( typeof endFunction == 'function') {
							endFunction(startX, startY, endX, endY, event, o);
						}
						$(o).off('touchmove');
					});
				});
			});
		}
	};
	document.addEventListener("touchstart", function(e) {
		return false;
	}, true);
	// 禁止选择
	document.oncontextmenu = function() {
		return false;
	};
    window.setTimeout(exports.ready, 200);
});

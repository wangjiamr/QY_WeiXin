define(function(require, exports, module) {
	var newWindow = null;
	exports.create = function(windowID, showType, delay, callback) {
		if (newWindow) {
			return;
		}
		if (window.plus) {
			var _showType = 'slide-in-right';
			if (showType) {
				_showType = showType;
			}
			newWindow = plus.ui.createWindow(windowID, {
				name : windowID,
				scrollIndicator : 'none',
				scalable : false
			});
			if (newWindow) {
				var showTimes = 300;
				if (_showType == 'none') {
					showTimes = 0;
				}
				if (!delay) {
					newWindow.show(_showType, showTimes);
					setTimeout(function() {
						newWindow = null;
					}, 300);
				} else {
					newWindow.addEventListener('loaded', function() {
						if ( typeof callback == 'function') {
							callback();
						}
						if (newWindow) {
							newWindow.show(_showType, showTimes);
							setTimeout(function() {
								newWindow = null;
							}, 300);
						}
					}, false);
				}
			}
		}
	};
	exports.load = function(url) {
		if (window.plus && url) {
			exports.name(url);
			plus.ui.getSelfWindow().load(url);
		}
	};
	exports.reload = function(flag) {
		if (window.plus) {
			plus.ui.getSelfWindow().reload(flag);
		}
	};
	exports.visible = function(visibleFlag) {
		if (window.plus) {
			plus.ui.getSelfWindow().setContentVisible(visibleFlag);
		}
	};
	exports.back = function(hideType) {
		if (window.plus) {
			var _hideType = 'slide-in-left';
			if (hideType) {
				_hideType = hideType;
			}
			plus.ui.getSelfWindow().close(_hideType);
		}
	};
	exports.name = function(windowName) {
		if (window.plus) {
			if (windowName) {
				plus.ui.getSelfWindow().setOption({
					name : windowName
				});
			} else {
				return plus.ui.getSelfWindow().getOption()['name'];
			}
		}
	};
	exports.backEvent = function(backCallback) {
		if (window.plus) {
			plus.ui.getSelfWindow().addEventListener("back", function() {
				if ( typeof backCallback == 'function') {
					backCallback();
				}
			}, false);
		}
	};
	exports.loadedEvent = function(loadedCallback) {
		if (window.plus) {
			plus.ui.getSelfWindow().addEventListener("loaded", function() {
				if ( typeof loadedCallback == 'function') {
					loadedCallback();
				}
			}, false);
		}
	};
});

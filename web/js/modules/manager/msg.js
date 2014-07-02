define(function(require, exports, module) {
	var waitingObj = null;
	exports.create = function(msg, closeTimes) {
		if (waitingObj) {// 避免快速多次点击创建多个窗口
			return;
		}
		if (window.plus) {
			waitingObj = plus.ui.createWaiting('    '+msg+'    ');
			if (closeTimes) {
				closeTimes = parseInt(closeTimes);
				if (closeTimes > 0) {
					window.setTimeout(function() {
						if (waitingObj) {
							waitingObj.close();
						}
					}, closeTimes);
				}
			}
		}
	};
	exports.title = function(msg) {
		if (waitingObj) {
			waitingObj.setTitle('    '+msg+'    ');
		}
	};
	exports.close = function() {
		if (waitingObj) {
			waitingObj.close();
			waitingObj=null;
		}
	};
});

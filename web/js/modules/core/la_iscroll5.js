define(function(require, exports, module) {
	var $laCommon = require('la_common');
	var myScroll = false, downCancelAction = false, refreshIngAction = false, downIngAction = false, downAction = false, upCancelAction = false, upIngAction = false, upAction = false, scrollDivEL = false, pullDownOffset = 0, pullUpOffset = 0;
	exports.init = function() {
		$laCommon.touchM($(document), function(moveTouch, element) {

		});
	};
	exports.refreshing = function(refreshIngCallback) {
		refreshIngAction = refreshIngCallback;
	};
	exports.down = function(downIngCallback, downCancelCallback, downCallback) {
		downIngAction = downIngCallback;
		downCancelAction = downCancelCallback;
		downAction = downCallback;
	};
	exports.up = function(upIngCallback, upCancelCallback, upCallback) {
		upIngAction = upIngCallback;
		upCancelAction = upCancelCallback;
		upAction = upCallback;
	};
	exports.isOK = function() {
		if (myScroll) {
			return true;
		}
		return false;
	};
	exports.refresh = function() {
		if (myScroll) {
			myScroll.refresh()
		}
	};

	exports.pullDownOffset = function() {
		return pullDownOffset;
	};
	exports.goXY = function(x, y, delay) {
		if (scrollDivEL && myScroll) {
			if (!delay) {
				delay = 300;
			}
			myScroll.scrollTo(x, y, delay);
		}
	};
	exports.go = function(index) {
		if (scrollDivEL && myScroll) {
			if (!delay) {
				delay = 300;
			}
			myScroll.scrollToElement($('li:nth-child(' + index + ')', scrollDivEL).get(0), delay);
		}
	};
	exports.use = function(elementID, downOn, downEL, upEl) {
		scrollDivEL = $('div', '#' + elementID);
		if (downEL) {
			pullDownOffset = $(downEL).get(0).offsetHeight;
		}
		if (upEl) {
			pullUpOffset = $(upEl).get(0).offsetHeight;
		}
		myScroll = new IScroll('#' + elementID, {
			startY : -pullDownOffset,
			probeType : 2, //probeType：1对性能没有影响。在滚动事件被触发时，滚动轴是不是忙着做它的东西。probeType：2总执行滚动，除了势头，反弹过程中的事件。这类似于原生的onscroll事件。probeType：3发出的滚动事件与到的像素精度。注意，滚动被迫requestAnimationFrame（即：useTransition：假）。
			scrollbars : true, //有滚动条
			mouseWheel : true, //允许滑轮滚动
			fadeScrollbars : true, //滚动时显示滚动条，默认影藏，并且是淡出淡入效果
			bounce : true, //边界反弹
			interactiveScrollbars : true, //滚动条可以拖动
			shrinkScrollbars : 'scale', // 当滚动边界之外的滚动条是由少量的收缩。'clip' or 'scale'.
			click : true, // 允许点击事件
			keyBindings : true, //允许使用按键控制
			momentum : true// 允许有惯性滑动
		});
		if (myScroll) {
			myScroll.on('refresh', function() {
				if ( typeof refreshIngAction == 'function') {
					refreshIngAction();
				}
			});
			myScroll.on('scroll', function() {
				if (this.y > 5 && !$(downEL).hasClass('action')) {

					//下拉时候
					$(downEL).addClass('action').addClass('up');
					if ( typeof downIngAction == 'function') {
						downIngAction();
					}
					this.minScrollY = 0;
				} else if (this.y < 5 && $(downEL).hasClass('action')) {
					//下拉回退 放弃
					$(downEL).removeClass('action').removeClass('up');
					if ( typeof downCancelAction == 'function') {
						downCancelAction();
					}
					this.minScrollY = -pullDownOffset;
				} else if (this.y < (this.maxScrollY - 5) && !$(upEl).hasClass('action')) {

					$(upEl).addClass('action');
					if ( typeof upIngAction == 'function') {
						upIngAction();
					}
					this.maxScrollY = this.maxScrollY;
				} else if (this.y > (this.maxScrollY + 5) && this.maxScrollY <= 0 && $(upEl).hasClass('action')) {
					$(upEl).removeClass('action');
					if ( typeof upCancelAction == 'function') {
						upCancelAction();
					}
					this.maxScrollY = pullUpOffset;
				}
			});

			myScroll.on('scrollEnd', function() {
				if ($(downEL).hasClass('action')) {
					$(downEL).removeClass('action').removeClass('up').addClass('wait');
					if ( typeof downAction == 'function') {
						window.setTimeout(downAction, 300);
					}
				} else if ($(upEl).hasClass('action')) {
					$(upEl).removeClass('action');
					if ( typeof upAction == 'function') {
						window.setTimeout(upAction, 300);
					}
				}
			});
		}
	};
});

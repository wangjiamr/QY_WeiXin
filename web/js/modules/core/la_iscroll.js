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
		myScroll = new iScroll(elementID, {
			useTransition : true,
			topOffset : pullDownOffset,
			hScroll : false,
			hScrollbar : false,
			vScrollbar : false,
			onRefresh : function() {
                if(downOn){
                    if ( typeof refreshIngAction == 'function') {
                        refreshIngAction();
                    }
                }
			},
			onScrollMove : function() {
                if(downOn){
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
                    } else if (this.y > (this.maxScrollY + 5)&&this.maxScrollY<=0 && $(upEl).hasClass('action')) {
                        $(upEl).removeClass('action');
                        if ( typeof upCancelAction == 'function') {
                            upCancelAction();
                        }
                        this.maxScrollY = pullUpOffset;
                    }
                }

			},
			onScrollEnd : function() {
                if(downOn){
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
                }
			}
		});
	};
});

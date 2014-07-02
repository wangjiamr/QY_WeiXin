define(function(require, exports, module) {
	var currentPageObj=false,currentElementEL;
	exports.init = function(elementEL,pageObj) {
		currentPageObj=pageObj;
		currentElementEL=elementEL;
		if(currentPageObj){
			if(currentPageObj['hasNextPage']==true){
				$(currentElementEL).removeClass('color-9').text('更多...')
			}else {
				$(currentElementEL).addClass('color-9').text('没有更多...')
			}
		}
	};
	exports.reload = function() {
		exports.init(currentElementEL,currentPageObj);
	};
	exports.setPage = function(pageObj) {
		currentPageObj=pageObj;
	};
	exports.hasMore = function() {
		var more=false;
		if(currentPageObj){
			if(currentPageObj['hasNextPage']==true){
				more=true;
			}
		}
		return more;
	};
	exports.getNextIndex = function() {
		var nextIndex=0;
		if(currentPageObj){
			nextIndex=currentPageObj['nextIndex'];
		}
		return nextIndex;
	};
});

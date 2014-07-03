define(function(require, exports, module) {
	var $templete = require('templete');
	exports.createMask = function(msg) {
		if($('#maskOver').size()==0){
			$('body').append($templete.getMask());
		}
	};
	exports.showMask = function() {
		$('#maskOver').show();
	};
	exports.hideMask = function() {
		$('#maskOver').hide();
	};
	
	exports.createMaskWatiting = function(msg) {
		if($('#maskOverWatiting').size()==0){
			$('body').append($templete.getMaskWating());
		}
	};
	exports.showMaskWatiting = function(msg) {
		if(msg){
			$('.txt','#maskOverWatiting').text(msg);
		}
		$('#maskOverWatiting').show();
	};
	exports.hideMaskWatiting = function() {
		$('#maskOverWatiting').hide();
	};
});

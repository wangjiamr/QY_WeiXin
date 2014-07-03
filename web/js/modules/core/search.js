define(function(require, exports, module) {
	exports.toggle = function() {
		var searchBox = $('.SearchBox');
		if ($(searchBox).hasClass('current')) {
			$(searchBox).removeClass('current');
			$(searchBox).slideUp(100);
		} else {
			$(searchBox).addClass('current');
			$(searchBox).slideDown(100);
		}
	};
	exports.close = function() {
		var searchBox = $('.SearchBox');
		if ($(searchBox).hasClass('current')) {
			$(searchBox).removeClass('current');
			$(searchBox).slideUp(0);
		}
	};
});

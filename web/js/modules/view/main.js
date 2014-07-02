define(function(require, exports, module) {
	var $laCommon = require('la_common');
	var $userInfo = require('core/userInfo');
	var $authorize = require('core/authorize');
	var $windowManager = require('manager/window');
	$laCommon.onReady(function() {
		window.setTimeout(function() {
			if ($userInfo.isAuthorize()) {
				$authorize.login($userInfo.get('companyId'), $userInfo.get('account'), $userInfo.get('password'), function() {
					if ($userInfo.isSupport()) {
						var avstar100 = $userInfo.get('avstar100');
						if ($.trim(avstar100).length > 0) {
							if (avstar100.indexOf('default.gif') == -1) {
								$("#userImg").attr('src', avstar100);
							}
						}
					}
					$('#main').fadeOut(function() {
						$('#welcome').fadeIn();
						window.setTimeout(function() {
							$windowManager.load('req/index.html');
						}, 2000);
					});
				}, function() {
					window.setTimeout(function() {
						$windowManager.load('login.html');
					}, 2000);
				});
			} else {
				window.setTimeout(function() {
					$windowManager.load('login.html');
				}, 2000);
			}
		}, 3000);
	});
});


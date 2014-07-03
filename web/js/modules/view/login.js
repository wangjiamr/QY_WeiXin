define(function(require, exports, module) {
	var $laCommon = require('la_common');
	var $windowManager = require('manager/window');
	var $msgManager = require('manager/msg');
	var $authorize = require('core/authorize');
	var $userInfo = require('core/userInfo');
	var widthMax, widthCurrnet, widthButton, widthRate;
	showCompany = function(account, password) {
		$('.mask').show();
		$('.CircleIn').addClass('current');
		$msgManager.create('正在验证帐户', false);
		$authorize.validate(account, password, function(jsonData) {
			var companyList = jsonData['companyList'];
			if (companyList) {
				if ($(companyList).size() == 1) {
					var companyId = companyList[0].companyId;
					login(companyId, account, password);
				} else {
					$msgManager.close();
					$('#companyListUL').empty();
					$(companyList).each(function(i, companyObj) {
						$('#companyListUL').append('<li uid="' + companyObj['companyId'] + '">' + companyObj['companyName'] + '</li>');
					});
					$('#companyListDIV').show();
					$('li', '#companyListUL').off('click').on('click', function() {
						var uid = $(this).attr('uid');
						$('#companyListDIV').hide();
						login(uid, account, password);
					});
					$('.quitBtn').off('click').on('click', function() {
						$('#companyListDIV,.mask').hide();
						$('.loginBtnIn').css('width', '18%');
						$('.CircleIn').removeClass('current');
					});
				}
			}
		}, function() {
			$msgManager.title('身份验证失败 ', false);
			window.setTimeout(function() {
				$('.loginBtnIn').css('width', '18%');
				$('.CircleIn').removeClass('current');
				$('.mask').hide();
				$msgManager.close();
			}, 1000);
		});
	};

	login = function(companyId, account, password) {
		$('.mask').show();
		$msgManager.create('正在登陆', false);
		$authorize.login(companyId, account, password, function() {
			$windowManager.load('req/index.html');
			$('.mask').hide();
			$msgManager.close();
			window.setTimeout(function() {
				$('.loginBtnIn').css('width', '18%');
				$('.CircleIn').removeClass('current');
			}, 2000);
		}, function() {
			$msgManager.title('未知错误');
			window.setTimeout(function() {
				$('.loginBtnIn').css('width', '18%');
				$('.CircleIn').removeClass('current');
				$('.mask').hide();
				$msgManager.close();
			}, 1000);
		});
	};

	init = function() {
		var windowHeight = $(window).height();
		if (windowHeight > 460) {
			$('.login').removeClass('noh');
		}
		if ($userInfo.isSupport()) {
			$('#account').val($userInfo.get('account'));
			$('#password').val($userInfo.get('password'));
			var avstar100 = $userInfo.get('avstar100');
			if ($.trim(avstar100).length > 0) {
				if (avstar100.indexOf('default.gif') == -1) {
					$("#userImg").attr('src', avstar100);
				}
			}
		}
	};

	$laCommon.onReady(function() {
		init();
		$laCommon.touchSME($('.Icon-login'), function(startX, startY, endX, endY, event, startTouch, element) {
			widthMax = $('.loginBtn').width();
			widthCurrnet = $('.loginBtnIn').width();
			widthButton = $('.Icon-login').width();
		}, function(startX, startY, endX, endY, event, moveTouch, element) {
			var x = endX - startX;
			var y = endY - startY;
			if (Math.abs(x) > Math.abs(y)) {
				if (x < 0) {//left
					widthRate = (x + widthButton) / (widthMax);
					widthRate = widthRate.toFixed(2);
					widthRate = widthRate * 100;
					if (widthRate < 18) {
						widthRate = 18;
					}
					$('.loginBtnIn').css('width', widthRate + '%');
				} else {//right
					widthRate = (x + widthButton) / (widthMax);
					widthRate = widthRate.toFixed(2);
					widthRate = widthRate * 100;
					if (widthRate > 100) {
						widthRate = 100;
					}
					$('.loginBtnIn').css('width', widthRate + '%');
				}
			}
		}, function(startX, startY, endX, endY, event, element) {
			if (widthRate < 99) {
				$('.loginBtnIn').css('width', '18%');
			} else if (widthRate > 99) {
				if ($.trim($('#account').val()).length > 0 && $.trim($('#password').val()).length > 0) {
					$('.mask').show();
					showCompany($.trim($('#account').val()), $.trim($('#password').val()));
				} else {
					$('.loginBtnIn').css('width', '18%');
					var w = plus.ui.createWaiting("　　 请输入用户名称和密码...　　 ");
					window.setTimeout(function() {
						w.close();
					}, 1000);
				}
			}
		});
	});
});


define(function(require, exports, module) {
	var $laCommon = require('la_common');
	var $userInfo = require('core/userInfo');
	var $windowManager = require('manager/window');

	exports.login = function(companyId, account, password, successCallback, errorCallback) {
		$.ajax({
			type : 'POST',
			url : $laCommon.getRestApiURL() + '/common/authorize',
			dataType : 'json',
			data : {
				companyId : companyId,
				account : account,
				password : password
			},
			success : function(jsonData) {
				if (jsonData) {
					if (jsonData['result'] == '0') {
						$userInfo.putJson(jsonData);
						$userInfo.put('account', account);
						$userInfo.put('password', password);
						$userInfo.put('selectMenu', 'ing');
						if ( typeof successCallback == 'function') {
							successCallback(jsonData);
						}
					} else {
						if ( typeof errorCallback == 'function') {
							errorCallback();
						}
					}
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				if ( typeof errorCallback == 'function') {
					errorCallback();
				}
			}
		});
	};
	exports.validate = function(account, password, successCallback, errorCallback) {
		$.ajax({
			type : 'POST',
			url : $laCommon.getRestApiURL() + '/common/authorize/companyList',
			dataType : 'json',
			data : {
				account : account,
				password : password
			},
			success : function(jsonData) {
				if (jsonData) {
					if (jsonData['result'] == '0') {
						if ( typeof successCallback == 'function') {
							successCallback(jsonData);
						}
					} else {
						if ( typeof errorCallback == 'function') {
							errorCallback();
						}
					}
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				if ( typeof errorCallback == 'function') {
					errorCallback();
				}
			}
		});
	};
	exports.logout = function() {

		if ($userInfo.isAuthorize()) {
				$.ajax({
					type : 'POST',
					url : $laCommon.getRestApiURL() + '/common/authorize/logout',
					dataType : 'json',
					data : {
						laToken : $.trim(localStorage.getItem("laAccessToken"))
					},
					success : function(jsonData) {
						if (jsonData) {
							if (jsonData['result'] == '0') {
								if (window.plus) {
									$windowManager.load('../login.html');
								}
							}
						}
					},
					error : function(jsonData) {
					}
				});
		}
	};

});

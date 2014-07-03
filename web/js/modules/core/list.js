define(function(require, exports, module) {
	var requestURL = false,requestParams={};
	var $msgManager = require('manager/msg');
	var bindDataCallback=false,appendBindDataCallback;
	exports.url = function(url) {
		requestURL = url;
	};
	exports.params = function(params) {
		if(params){
			requestParams = params;
		}
		return requestParams;
	};
	exports.bind = function(bindData) {
		if(bindData){
			bindDataCallback = bindData;
		}
	};
	exports.appendBind = function(appendBindData) {
		if(appendBindData){
			appendBindDataCallback = appendBindData;
		}
	};
	exports.load = function() {
		if (requestURL) {
			$.ajax({
				type : 'POST',
				url : requestURL,
				dataType : 'json',
				data : requestParams,
				success : function(jsonData) {
					if (jsonData) {
						if (jsonData['result'] == '0') {
							if ( typeof bindDataCallback == 'function') {
								bindDataCallback(jsonData);
							}
						}
					}
				},
				error : function(jsonData) {
				}
			});
		}
	};
	exports.appendLoad = function() {
		if (requestURL) {
			$.ajax({
				type : 'POST',
				url : requestURL,
				dataType : 'json',
				data : requestParams,
				success : function(jsonData) {
					if (jsonData) {
						if (jsonData['result'] == '0') {
							if ( typeof appendBindDataCallback == 'function') {
								appendBindDataCallback(jsonData);
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

define(function(require, exports, module) {
	exports.getMask = function() {
		var maskWating = new StringBuilder();
		maskWating.append('<div class="maskOver" style="display:none;" id="maskOver"></div>\n');
		return maskWating.toString();
	};
	exports.getMaskWating = function() {
		var maskWating = new StringBuilder();
		maskWating.append('<div class="maskOver maskOverbg" style="display:block;" id="maskOverWatiting">\n');
		maskWating.append('<div class="maskIN aligncenter">\n');
		maskWating.append('<p><img src="../../img/loading.gif" width="81"></p>\n');
		maskWating.append('<p class="color-8 txt">努力加载中...</p>\n');
		maskWating.append('</div>\n');
		maskWating.append('</div>\n');
		return maskWating.toString();
	};
	exports.getReqListTemp = function(removeFlag, tip) {
		var reqListTemp = new StringBuilder();
		reqListTemp.append('<li class="clearfix {resultClass}" uid="{id}" >\n');
		if (tip) {
			reqListTemp.append('<span class="BtnIcon arrow1"></span>\n');
		} else {
			reqListTemp.append('<span class="BtnIcon floatleft"></span>\n');

		}
		reqListTemp.append('<span class="AppImg floatleft">\n');
		reqListTemp.append('<img src="../../img/{icon}">\n');
		reqListTemp.append('</span>\n');
		reqListTemp.append('<p>\n');
		reqListTemp.append('<span class="AppName font14">{applyName}</span>\n');
		reqListTemp.append('<span class="floatright">{applyDate}</span>\n');
		reqListTemp.append('</p>\n');
		reqListTemp.append('<p>\n');
		reqListTemp.append('<span>{reqNo}</span>\n');
        reqListTemp.append('<span class="floatright">{resultText}</span>\n');
		reqListTemp.append('</p>\n');
		if (removeFlag) {
			reqListTemp.append('<span class="Revoke">撤销</span>\n');
		}
		reqListTemp.append('</li>\n');
		return reqListTemp.toString();
	};
	exports.getTaskListTemp = function() {
		var taskListTemp = new StringBuilder();
		taskListTemp.append('<li class="clearfix {resultClass}" uid="{id}" reqId="{reqId}">\n');
		taskListTemp.append('<span class="BtnIcon floatleft"></span>\n');
		taskListTemp.append('<span class="AppImg floatleft">\n');
		taskListTemp.append('<img src="../../img/{icon}">\n');
		taskListTemp.append('</span>\n');
		taskListTemp.append('<p>\n');
		taskListTemp.append('<span class="AppName font14">{applyName}</span>\n');
		taskListTemp.append('<span class="floatright">{applyDate}</span>\n');
		taskListTemp.append('</p>\n');
		taskListTemp.append('<p>\n');
        taskListTemp.append('<span>{reqNo}</span>\n');
        taskListTemp.append('<span class="floatright">{resultText}</span>\n');
		taskListTemp.append('</p>\n');
		taskListTemp.append('</li>\n');
		return taskListTemp.toString();
	};
	exports.getManageListTemp = function() {
		var manageListTemp = new StringBuilder();
		manageListTemp.append('<li class="clearfix {resultClass}" uid="{id}" reqId="{reqId}">\n');
		manageListTemp.append('<span class="BtnIcon floatleft"></span>\n');
		manageListTemp.append('<span class="AppImg floatleft">\n');
		manageListTemp.append('<img src="../../img/{icon}">\n');
		manageListTemp.append('</span>\n');
		manageListTemp.append('<p>\n');
		manageListTemp.append('<span class="AppName font14">{applyName}</span>\n');
		manageListTemp.append('<span class="floatright">{applyDate}</span>\n');
		manageListTemp.append('</p>\n');
		manageListTemp.append('<p>\n');
        manageListTemp.append('<span>{reqNo}</span>\n');
        manageListTemp.append('<span class="floatright">{resultText}</span>\n');
		manageListTemp.append('</p>\n');
		manageListTemp.append('</li>\n');
		return manageListTemp.toString();
	};
});
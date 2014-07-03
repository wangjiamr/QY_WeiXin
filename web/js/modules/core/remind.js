define(function(require, exports, module) {
	var $userInfo=require('core/userInfo');
	exports.sidebar = function() {
		if ($userInfo.isSupport()) {
			if($userInfo.get('reqCount')>0){
				$('.req','#leftMenu').find('em').text($userInfo.get('reqCount')).show();
			}
			if($userInfo.get('taskApprove')>0){
				$('.task','#leftMenu').find('em').text($userInfo.get('taskApprove')).show();
			}
			if($userInfo.get('manageExecute')>0){
				$('.manage','#leftMenu').find('em').text($userInfo.get('manageExecute')).show();
			}
		}
	};
	exports.menuREQ = function() {
		if ($userInfo.isSupport()) {
			if($userInfo.get('reqHistoryCount')>0){
				$('li[dir="history"]','#menuRemind').find('em').text($userInfo.get('reqHistoryCount')).show();
			}
			if($userInfo.get('reqConfirmCount')>0){
				$('li[dir="confirm"]','#menuRemind').find('em').text($userInfo.get('reqConfirmCount')).show();
			}
		}
	};
	exports.menuTASK = function() {
		if ($userInfo.isSupport()) {
			if($userInfo.get('taskApprove')>0){
				$('li[dir="ing"]','#menuRemind').find('em').text($userInfo.get('taskApprove')).show();
			}
		}
	};
	exports.menuMANAGE = function() {
		if ($userInfo.isSupport()) {
			if($userInfo.get('manageExecute')>0){
				$('li[dir="ing"]','#menuRemind').find('em').text($userInfo.get('manageExecute')).show();
			}
		}
	};
});

define(function(require, exports, module) {
	var $laCommon = require('la_common');
	var $windowManager = require('manager/window');
	var $maskManager = require('manager/mask');
	var $templete = require('templete');
	var $sidebar = require('core/sidebar');
	var $search = require('core/search');
	var $menu = require('core/menu');
	var $list = require('core/list');
	var $more = require('core/more');
	var $userInfo = require('core/userInfo');
	var $laIscroll = require('core/la_iscroll');
	var $remind = require('core/remind');
	loadIngData = function() {
		$list.url($laCommon.getRestApiURL() + "/wf/reqTask/ingList");
		$list.params({
			'laToken' : $userInfo.get('laAccessToken')
		});
		$list.bind(function(jsonData) {
			var reqList = jsonData['reqList'];
			if (reqList) {
				var dataStr = new StringBuilder();
				$(reqList).each(function(i, o) {
					var listTemp = $templete.getTaskListTemp();
					dataStr.append(String.formatmodel(listTemp, {
						'id' : o['id'],
						'applyName' : o['applyName'],
						'applyDate' : o['receiveDate'],
						'reqNo' : o['reqNo']
					}));
				});
				if ($('li', '#reqListUL').size() == 0) {
					$('#reqListUL').append(dataStr.toString());
					bindRemoveMoveEvent();
					if (!$laIscroll.isOK()) {
						$laIscroll.use('wrapper', true, $('#refreshAction'), $('#moreAction'));
					} else {
						$laIscroll.refresh();
					}
					$('.desc', '#refreshAction').text('下拉刷新列表...');
					$more.init($('#moreAction'), jsonData['page']);
				} else {
					$('#reqListUL').fadeOut(200, function() {
						$('#reqListUL').empty().append(dataStr.toString()).fadeIn(200, function() {
							bindRemoveMoveEvent();
							$laIscroll.refresh();
							dataStr = null;
							$('.desc', '#refreshAction').text('下拉刷新列表...');
							$more.init($('#moreAction'), jsonData['page']);
						});
					});
				}
			} else {
				$laIscroll.refresh();
				$more.init($('#moreAction'), jsonData['page']);
			}
			$maskManager.hideMaskWatiting();
			$maskManager.hideMask();
		});
		$list.load();
	};

	bindRemoveMoveEvent = function() {
		$laCommon.touchSME($('li', '#reqListUL'), function(startX, startY, endX, endY, event, startTouch, element) {

		}, function(startX, startY, endX, endY, event, moveTouch, element) {
		}, function(startX, startY, endX, endY, event, element) {
			$(element).addClass('current');
			window.setTimeout(function() {
				$(element).removeClass('current');
				$windowManager.create('taskView.html', false, true);
			}, 150);
		});
	};
	$laCommon.onReady(function() {
		$maskManager.createMaskWatiting();
		$maskManager.createMask();

		$sidebar.init();

		$laIscroll.init();

		$laIscroll.down(function() {
			$('.desc', '#refreshAction').text('松开即可刷新...');
		}, function() {
			$('.desc', '#refreshAction').text('下拉刷新列表...');
		}, function() {
			$maskManager.showMask();
			$('.desc', '#refreshAction').text('正在刷新列表...');
			$('#moreAction').html('&nbsp;');
			var params = $list.params();
			params = $.extend({}, params, {
				'start' : 0
			});
			$list.params(params);
			$list.load();
		});
		$laIscroll.refreshing(function() {
			$('#refreshAction').removeClass('up').removeClass('wait');
		});
		$laIscroll.up(function() {
			if ($more.hasMore()) {
				$('#moreAction').text('松开获取更多...');
			}
		}, function() {
			$more.reload();
		}, function() {
			if ($more.hasMore()) {
				$maskManager.showMask();
				$('#moreAction').text('正在加载更多...');
				var params = $list.params();
				params = $.extend({}, params, {
					'start' : $more.getNextIndex()
				});
				$list.params(params);
				$list.appendBind(function(jsonData) {
					var reqList = jsonData['reqList'];
					if (reqList) {
						$(reqList).each(function(i, o) {
							var listTemp = $templete.getTaskListTemp();
							$('#reqListUL').append(String.formatmodel(listTemp, {
								'id' : o['id'],
								'applyName' : o['applyName'],
								'applyDate' : o['receiveDate'],
								'reqNo' : o['reqNo']
							}));
						});
					}
					$maskManager.hideMaskWatiting();
					$more.init($('#moreAction'), jsonData['page']);
					$laIscroll.refresh();
					$maskManager.hideMask();
				});
				$list.appendLoad();
			} else {
				$more.reload();
			}
		});

		$laCommon.touchSE($('.Calleft'), function(event, startTouch, o) {
			$sidebar.toggle();
		}, function(event, o) {
		});

		$laCommon.touchSE($('#RemoveLeft'), function(event, startTouch, o) {
			$sidebar.toggle();
		}, function(event, o) {
		});

		$laCommon.touchSE($('#Search'), function(event, startTouch, o) {
			$menu.close();
			$search.toggle();
		}, function(event, o) {
		});

		$laCommon.touchSE($('#menuDown'), function(event, startTouch, o) {
			$search.close();
			$menu.toggle();
		}, function(event, o) {
		});
		$laCommon.touchSE($('#sendAction'), function(event, startTouch, o) {
		}, function(event, o) {
			$userInfo.put("sendReq", "1");
			$windowManager.create('send.html', 'zoom-out', true);
		});
		loadIngData();

		$menu.select('ing');
		$remind.menuTASK();
		$remind.sidebar();
		$menu.click(function(selectMenu) {
			window.setTimeout(function() {
				$menu.toggle(function() {
					$('li', '#reqListUL').fadeOut(300);
					$maskManager.showMaskWatiting();
					$('.desc', '#refreshAction').text('正在加载数据...');
					$('#moreAction').html('&nbsp;');
					var params = $list.params();
					if (selectMenu == 'ing') {
						$list.url($laCommon.getRestApiURL() + "/wf/reqTask/ingList");
					} else if (selectMenu == 'history') {
						$list.url($laCommon.getRestApiURL() + "/wf/reqTask/historyList");
					}
					params = $.extend({}, params, {
						'start' : 0
					});
					$list.params(params);
					$list.load();
					$menu.select(selectMenu);
					$remind.menuREQ(selectMenu);
				});
			}, 300);
		});

		$sidebar.menuAction(function(selectMenu) {
			$sidebar.toggle(function() {
				if (selectMenu == 'req') {
					$windowManager.load('../req/index.html');
				} else if (selectMenu == 'manage') {
					$windowManager.load('../manage/index.html');
				}
			});
		});
	});
});


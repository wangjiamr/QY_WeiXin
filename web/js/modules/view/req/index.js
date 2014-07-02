define(function(require, exports, module) {
    var $laCommon = require('la_common');
    var $maskManager = require('manager/mask');
    var $templete = require('templete');
    var $search = require('core/search');
    var $menu = require('core/menu');
    var $list = require('core/list');
    var $more = require('core/more');
    var $userInfo = require('core/userInfo');
    var $laIscroll = require('core/la_iscroll');
    var $remind = require('core/remind');
    loadIngData = function() {
        alert('a='+laTokenFinal)
        $list.url($laCommon.getRestApiURL() + "/wf/req/ingList");
        $list.params({
            'laToken' : laTokenFinal
        });
        $list.bind(function(jsonData) {
            $('#emptyDIV').hide();
            var reqList = jsonData['reqList'];
            if (reqList) {
                if ($(reqList).size() <= 0) {
                    $('.desc', '#refreshAction').text('&nbsp;');
                    $('#moreAction').html('&nbsp;');
                    $('#emptyDIV').text('没有数据').show();
                } else {
                    var dataStr = new StringBuilder();
                    $(reqList).each(function(i, o) {
                        var listTemp;
                        if (o['tip'] == 1) {
                            listTemp = $templete.getReqListTemp(true, true);
                        } else if (o['tip'] == 0) {
                            listTemp = $templete.getReqListTemp(false, false);
                        }
                        dataStr.append(String.formatmodel(listTemp, {
                            'id' : o['id'],
                            'applyName' : o['applyName'],
                            'applyDate' : o['sendDate'],
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
                                $laIscroll.goXY(0, -46, 300);
                                window.setTimeout(function() {
                                    $laIscroll.refresh();
                                    dataStr = null;
                                    $('.desc', '#refreshAction').text('下拉刷新列表...');
                                    $more.init($('#moreAction'), jsonData['page']);
                                }, 310)
                            });
                        });
                    }
                }
            } else {
                alert('a')
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
            $(element).removeClass('current');
        }, function(startX, startY, endX, endY, event, element) {
            var x = endX - startX;
            var y = endY - startY;
            if (Math.abs(x) > Math.abs(y)) {
                if (x < 0) {
                    $(element).addClass('active');
                } else {
                    $(element).removeClass('active');
                }
            } else if (Math.abs(x) == 0 && Math.abs(y) == 0) {
                $(element).addClass('current');
                window.setTimeout(function() {
                    $(element).removeClass('current');
                }, 150);

            }
        });
    };
    $laCommon.onReady(function() {
        if(laTokenFinal){
            $maskManager.createMaskWatiting();
            $maskManager.createMask();


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
                                var listTemp;
                                if (o['tip'] == 1) {
                                    listTemp = $templete.getReqListTemp(true, true);
                                } else if (o['tip'] == 0) {
                                    listTemp = $templete.getReqListTemp(false, false);
                                }
                                $('#reqListUL').append(String.formatmodel(listTemp, {
                                    'id' : o['id'],
                                    'applyName' : o['applyName'],
                                    'applyDate' : o['sendDate'],
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

            loadIngData();

            $menu.select('ing');
            $remind.menuREQ();
            $menu.click(function(selectMenu) {
                window.setTimeout(function() {
                    $menu.toggle(function() {
                        $('li', '#reqListUL').fadeOut(300);
                        $maskManager.showMaskWatiting();
                        $('.desc', '#refreshAction').text('正在加载数据...');
                        $('#moreAction').html('&nbsp;');
                        var params = $list.params();
                        if (selectMenu == 'ing') {
                            $list.url($laCommon.getRestApiURL() + "/wf/req/ingList");
                        } else if (selectMenu == 'history') {
                            $list.url($laCommon.getRestApiURL() + "/wf/req/historyList");
                        } else if (selectMenu == 'confirm') {
                            $list.url($laCommon.getRestApiURL() + "/wf/req/confirmList");
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
        }
    });
});
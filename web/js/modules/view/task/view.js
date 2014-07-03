define(function (require, exports, module) {
    var $laCommon = require('la_common');
    var $laIscroll = require('core/la_iscroll');
    var $maskManager = require('manager/mask');
    $laCommon.onReady(function () {
        $maskManager.createMask();
        $laIscroll.init();
        $laIscroll.use('wrapper', false, false, false);

        $laCommon.touchSE($('div','.BottomRbox'), function(event, startTouch, o) {
            $('div','.BottomRbox').removeClass('current');
        }, function(event, o) {
            $maskManager.showMask();
            $(o).addClass('current');
            var approveIdea=$(o).attr('approveIdea');
            if(approveIdea!=null){
                if(approveIdea=='3'){
                    alert('我还没有准备好');
                    return false;
                }
                document.location.href='http://qywx.mingdao.com/wf/task/reason?laToken='+laTokenFinal+'&id='+taskId+'&approveIdea='+approveIdea;
                $maskManager.hideMask();
            }
        });
    });
});
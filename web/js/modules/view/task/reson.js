define(function (require, exports, module) {
    var $laCommon = require('la_common');
    var $maskManager = require('manager/mask');
    $laCommon.onReady(function () {
        $maskManager.createMask();
        $laCommon.touchSE($('.nextstep'), function(event, startTouch, o) {
        }, function(event, o) {
            $maskManager.showMask();
            var approveIdea=$(o).attr('approveIdea');
            var action=$(o).text();
            if(approveIdea&&action){
                if(confirm('您确认'+action+'该申请')){
                    var url='http://qywx.mingdao.com/wf/task/approve?laToken='+laTokenFinal;
                    url+='&id='+taskId+'&approveIdea='+approveIdea+'&reason='+$('#reason').val();
                    url=encodeURI(url);
                    document.location.href=url;
                }else{
                    $maskManager.hideMask();
                }
            }
        });
    });
});
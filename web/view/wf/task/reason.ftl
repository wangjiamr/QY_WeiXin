<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<script src="/js/iscroll.js"></script>
<script>
    var laTokenFinal="${laToken?if_exists}";
    var taskId="${id?c}";
    seajs.config({
        base: "/js/modules/"
    });
    seajs.use('view/task/reson');
</script>
    <#if approveIdea?exists>
    <div class="view">
        <!--header begin-->
        <!--header over-->
        <div class="content" id="wrapper">
            <div id="scroller">
                <div class="textM">
                    <p class="font14 color-8">填写审批意见</p>
                    <textarea class="mart10" id="reason" name="reason"></textarea>
                </div>

                <#if approveIdea==1>
                    <span class="nextstep fj-crrose aligncenter font18 mart20" approveIdea="${approveIdea?c}">通过</span>
                <#elseif approveIdea==2>
                    <span class="nextstep fj-btn aligncenter font18 mart20" approveIdea="${approveIdea?c}">否决</span>
                <#elseif approveIdea==3>
                    <span class="nextstep zs-btn aligncenter font18 mart20" approveIdea="${approveIdea?c}">转审</span>
                <#elseif approveIdea==4>
                    <span class="nextstep ht-btn aligncenter font18 mart20" approveIdea="${approveIdea?c}">回退</span>
                </#if>

            </div>
        </div>
    </div>
    </#if>


</@common.html>
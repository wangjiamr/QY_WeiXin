<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<script src="/js/iscroll.js"></script>
<script>
    var laTokenFinal="${laToken?if_exists}";
    var taskId="${reqViewEntity.taskId?c}";
    seajs.config({
        base : "/js/modules/"
    });
    seajs.use('view/task/view');
</script>
<#if reqViewEntity?exists>
<div class="view">
    <!--header begin-->
    <!--header over-->
    <div class="content" style="bottom:45px;" id="wrapper">
        <div id="scroller">
            <!--form begin-->
            <div class="formOut">
                <section class="form">
                    <p class="line font14 clearfix">
                        <span>${reqViewEntity.applyName?if_exists}${reqViewEntity.reqId?c}</span>
                        <span class="floatright">${reqViewEntity.reqNo?if_exists}</span>
                    </p>
                    <header class="clearfix">
                        <span class="userimg floatleft"><img src="${reqViewEntity.avstar100?if_exists}"></span>
                        <span class="name font15 color-6">${reqViewEntity.userName?if_exists}</span>
                        <span class="font12 color-8">${reqViewEntity.jobName?if_exists}</span>
                    </header>
                    <#assign rowContentList=reqViewEntity.rowContentList?if_exists/>
                    <#if rowContentList?exists&&rowContentList?size gt 0>
                        <#list rowContentList as rowContent>
                            <#if rowContent.type=="eval">
                                <#assign eval=rowContent.eval?if_exists/>
                                <#if eval?exists>
                                    <p class="line">${eval.label?if_exists} :
                                        <span class="floatright">总共XX分</span>
                                    </p>
                                    <#assign dataList=eval.dataList?if_exists/>
                                    <#if dataList?exists&&dataList?size gt 0>
                                        <div class="editMain" id="score">
                                            <ul class="editList process score">
                                                <#list dataList as dataListObj>
                                                    <li class="choose">
                                                        <div class="LiOut mart5">
                                                            <div class="Inmask clearfix">
                                                                <span class="floatleft color-6">${dataListObj.evalKey?if_exists}</span>
                                                                <span><em>${dataListObj.evalScore?c} 分</em></span>
                                                            </div>
                                                            <#assign detailList=dataListObj.detailList?if_exists/>
                                                            <#if detailList?exists&&detailList?size gt 0>
                                                                <ol>
                                                                    <#list detailList as detailObj>
                                                                        <li>${detailObj.seq?if_exists}、${detailObj.detail?if_exists}</li>
                                                                    </#list>
                                                                </ol>
                                                            </#if>
                                                        </div>
                                                    </li>
                                                </#list>
                                            </ul>
                                        </div>
                                    </#if>
                                </#if>
                            <#elseif rowContent.type=="list4">
                            <#elseif rowContent.type=="list5">
                            <#elseif rowContent.type=="detailList">
                                <#assign details=rowContent.details?if_exists/>
                                <#if details?exists&&details?size gt 0>
                                    <#list details as detailObj>
                                        <p class="line alignright">${rowContent.label?if_exists}${detailObj.seq?c} </p>
                                        <#assign detailList=detailObj.detailList?if_exists/>
                                        <#if detailList?exists&&detailList?size gt 0>
                                            <#list detailList as detailListObj>
                                                <table>
                                                    <tr>
                                                        <th>${detailListObj.label?if_exists}4 :</th>
                                                        <td>${detailListObj.value?if_exists}4</td>
                                                    </tr>
                                                </table>
                                            </#list>
                                        </#if>
                                    </#list>
                                </#if>
                            <#else >
                                <table>
                                    <tr>
                                        <th>${rowContent.label?if_exists} :</th>
                                        <td>${rowContent.value?if_exists}</td>
                                    </tr>
                                </table>
                            </#if>
                        </#list>
                    </#if>
                    <table>
                        <tr>
                            <th>附件 :</th>
                            <td><span class="color-cb"><i class="BtnIcon Icon-neex"></i>点击查看</span></td>
                        </tr>
                    </table>
                </section>
                <section class="form mart5">
                    <header class="nopadding clearfix">
                        <span class="nopadding color-6">审批流程</span>
                    </header>
                    <#assign commentsList=reqViewEntity.commentsList?if_exists/>
                    <#if commentsList?exists&&commentsList?size gt 0>
                        <table>
                            <#list commentsList as comment>
                                <tr>
                                    <td width="30%">${comment.userName?if_exists}</td>
                                    <td>${comment.actionDesc?if_exists}</td>
                                    <td>${comment.created?if_exists}</td>
                                </tr>
                            </#list>
                        </table>
                    </#if>
                </section>
                <!--form over-->
            </div>
        </div>
    </div>
    <!--右侧扇形菜单 begin-->
    <div class="BottomRbox clearfix">
        <div class="btn btn1" approveIdea="1"><i class="BtnIcon Icon-ok"></i><br><span>通过</span></div>
        <div class="btn btn2" approveIdea="2"><i class="BtnIcon Icon-no"></i><br><span>否决</span></div>
        <div class="btn btn4" approveIdea="3"><i class="BtnIcon Icon-turn"></i><br><span>转审</span></div>
        <div class="btn btn3" approveIdea="4"><i class="BtnIcon Icon-return"></i><br><span>回退</span></div>
    </div>
    <!--右侧扇形菜单 over-->

</div>
</#if>

</@common.html>
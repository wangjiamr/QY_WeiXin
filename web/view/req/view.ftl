<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<script src="/js/iscroll.js"></script>
<script>
    seajs.config({
        base : "/js/modules/"
    });
    seajs.use('view/req/view');
</script>
<#if reqViewEntity?exists>
<div class="view">
    <!--header begin-->
    <!--header over-->
    <div class="content" style="top: 0;" id="wrapper">
        <div id="scroller">
            <!--form begin-->
            <div class="formOut">
                <section class="form">
                    <header class="clearfix">
                        <span class="userimg floatleft"><img src="${reqViewEntity.avstar100?if_exists}"></span>
                        <span class="name font15 color-6">${reqViewEntity.userName?if_exists}</span>
                        <span class="font12 color-8">${reqViewEntity.jobName?if_exists}</span>
                    </header>
                    <#assign rowContentList=reqViewEntity.rowContentList?if_exists/>
                    <#if rowContentList?exists&&rowContentList?size gt 0>
                        <#list rowContentList as rowContent>
                            <#if rowContent.type=="eval">
                            <#elseif rowContent.type=="list4">
                            <#elseif rowContent.type=="list5">
                            <#elseif rowContent.type=="text">
                                <table>
                                    <tr>
                                        <th>${rowContent.label?if_exists} :</th>
                                        <td><pre>${rowContent.value?if_exists}</pre></td>
                                    </tr>
                                </table>
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
            <!--右侧扇形菜单 begin-->
            <div class="BottomRbox clearfix">
                <div class="btn btn1 current"><i class="BtnIcon Icon-ok"></i><br><span>通过</span></div>
                <div class="btn btn2"><i class="BtnIcon Icon-no"></i><br><span>否决</span></div>
                <div class="btn btn3"><i class="BtnIcon Icon-return"></i><br><span>回退</span></div>
                <div class="btn btn4"><i class="BtnIcon Icon-turn"></i><br><span>转审</span></div>
            </div>
            <!--右侧扇形菜单 over-->
        </div>
    </div>

</div>
</#if>

</@common.html>
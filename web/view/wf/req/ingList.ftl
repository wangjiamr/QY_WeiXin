<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<script src="/js/iscroll.js"></script>
<script>
    var laTokenFinal="${laToken?if_exists}";
    seajs.config({
        base : "/js/modules/"
    });
    seajs.use('view/req/ing');
</script>
<div class="maskOver maskOverbg" style="display:block;" id="maskOverWatiting">
    <div class="maskIN aligncenter">
        <p>
            <img src="../../img/loading.gif" width="81">
        </p>
        <p class="color-8">
            努力加载中...
        </p>
    </div>
</div>
<!--最上层的放置点击遮罩层-->

<div class="view">
    <!--content begin-->
    <div class="content" style="top:0;" id="wrapper">
        <div id="scroller">
            <div class="aligncenter more" id="refreshAction">
                <span class="loading"></span>
					<span>
						<em class="font13">加载中...</em><br />
						<em class="font9 color-8">最后更新: 今天 12:07</em>
					</span>
            </div>
            <ul class="list" id="reqListUL">
            </ul>
            <div class="aligncenter more color-9" style="padding-bottom:40px;" id="moreAction">点击加载更多...</div>
        </div>
        <p class="font14 aligncenter color-8" id="emptyDIV"
           style="position: absolute;height:25px;width:150px;top:50%;left:50%;margin:-10px 0 0 -75px;display:none;">当前没有进行中的表单</p>
    </div>
    <!--content over-->
</div>
<div class="maskOver" style="display:none;"></div><!--最上层的放置点击遮罩层-->
</@common.html>
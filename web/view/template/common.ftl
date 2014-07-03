<#macro html title="快捷审批" >
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Content-Language" content="utf-8"/>
    <title>${title?html}</title>
    <link rel="stylesheet" href="/css/appstyles.css" type="text/css">
    <script src="/js/jquery.js"></script>
    <script src="/js/sea.js"></script>
    <script src="/js/StringBuilder.js"></script>
    <script src="/js/support.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {

        });
    </script>
</head>
<body>
    <#setting number_format="#">
    <#nested/>
</body>
</html>
</#macro>

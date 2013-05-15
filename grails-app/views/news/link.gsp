<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <r:require modules="jquery-ui" />
    <title>AltrUHelp</title>
</head>
<body>
    <iframe name="external" id="external" style="height: 4000px" onload="resizeFrame(this)"
        src="${createLink(controller:'news',action:'externalContent',params:['page':page])}" marginwidth="0"
        marginheight="0" frameborder="no" scrolling="yes" style="border-width:0px; border-color:#333;" seamless>
        <iframe src="/recent"> </iframe>
    </iframe>
    <script language="javascript">
        function resizeFrame(f) {
            //alert(window.frames[0].location.href);
            if ($.isUrlInternal(window.frames[0].location.href)) {
                f.style.height = f.contentWindow.document.body.scrollHeight + 'px';
            }
        }
    </script>
</body>
</html>

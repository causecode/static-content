<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript">
        $(function(){
            $("#switch").click(function(){
                if($(this).text() == "Switch to Plain Text") {
                    $(this).text("Switch to ckeditor");
                //    $("textarea").val($("ckeditor").val());
                    $("#plaintext").css("display", "inherit");
                    $("#ckeditor").css("display", "none");
                }   else {
                    $(this).text("Switch to Plain Text");
                //    $("ckeditor").val($("textarea").val());
                    $("#plaintext").css("display", "none");
                    $("#ckeditor").css("display", "inherit");
                }
            })
        });
    </script>
</head>
<body>
    <div class="page-header">
        <h1>
            <g:message code="default.create.label" args="[entityName]" />
        </h1>
    </div>
    <g:form action="save"  class="form-horizontal">
        <fieldset>
            <g:render template="form" />
            <div class="form-actions">
                <g:submitButton name="create" class="btn btn-primary"
                    value="${message(code: 'default.button.create.label')}" />
            </div>
        </fieldset>
    </g:form>
</body>
</html>
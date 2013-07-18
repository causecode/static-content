<%--<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'menuItem.label', default: 'EditOrder')}" />
    <title><g:message code="default.editOrder.label" args="[entityName]" /></title>
    <r:require modules="draggableAndSortable"/>
    
</head>
<body>
    <script>is:"x"
        $(function() {
        $("#childMenuItem").sortable({
            revert:true,
            axis:"x"
        });
        $("#childMenuItem").draggable({
            connectToSortable: "#sortable",
            revert: "invalid",
            axis:"y"
        });
        $( "ul, li" ).disableSelection();
        });
    </script>
    
    <ul id="sortable" class="ui-sortable">
    <g:each in="${menuItemInstanceList}" var="menuItemInstance">
        <li class="ui-state-default">${menuItemInstance.id}</li>
    </g:each>
    </ul>
    
    <div id="parentMenuItem" >
        <ul id="sortable" class="media-list ui-sortable">
            <li class="media  ui-state-default">
                <div id="childMenuItem" class="media-body thumbnail">
                    <g:each in="${menuItemInstanceList}" var="menuItemInstance">
                        <g:if test="${!menuItemInstance?.parent }">
                                    <com:bootstrapMediaMenu id="${menuItemInstance?.id}"></com:bootstrapMediaMenu>
                        </g:if>
                    </g:each>
                </div>
            </li>
        </ul>
    </div>
</body>
</html>--%>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'menuItem.label', default: 'EditOrder')}" />
    <title><g:message code="default.editOrder.label" args="[entityName]" /></title>
    <link rel="stylesheet" href="/css/jquery-ui/jquery-ui-1.10.3.custom.css" />
    <script src="/js/jquery-ui/jquery-1.9.1.js"></script>
    <script src="/js/jquery-ui/jquery-ui-1.10.3.custom.js"></script>
    
</head>
<body>
    <script>
        $(function() {
        $( "#sortable" ).sortable({
          revert: true
        });
        $( "#draggable" ).draggable({
          connectToSortable: "#sortable",
          helper: "clone",
          revert: "invalid"
        });
        $( "ul, li" ).disableSelection();
        });
    </script>
    <ul>
      <li id="draggable" class="ui-state-highlight">Drag me down</li>
    </ul>
    <ul id="sortable" class="ui-sortable">
    <g:each in="${menuItemInstanceList}" var="menuItemInstance">
        <li class="ui-state-default">${menuItemInstance.id}</li>
    </g:each>
    </ul>
    
    <div class="pager">
        <g:paginate total="${menuItemInstanceTotal}" />
    </div>
    
</body>
</html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'menu.label')}" />
    <title>Sort Menu Items - ${menuInstance.name }</title>
    <r:require modules="menuItem, validation" strict="false" />
</head>
<body>
    <content tag="breadcrumb">
        <content:breadcrumb map="['/menu/list': 'Menu List', 'active':(menuInstance.name)]"/>
    </content>
    <div id="menu-item-alert" class="alert alert-success hid"><p></p></div>
    <div class="page-header">
        <h1 class="inline">
            Menu Items
            <small>${menuInstance.name }</small>
        </h1>
        <span class="btn-group pull-right">
            <a href="#create-menu-item-overlay" class="btn btn-primary btn-sm" data-toggle="modal">
                <i class="icon-plus"></i> &nbsp;Menu Item</a>
        </span>
    </div>

    <div id="menu-item-sorting">
        <ul class="menu-item-container top-level-container" data-parent-id="">
            <g:each in="${menuItemInstanceList}" var="menuItemInstance">
                <g:if test="${menuItemInstance && !menuItemInstance.parent }">
                    <g:render template="/menuItem/templates/forEdit" model="[instance: menuItemInstance, topLevel: true]" />
                </g:if>
            </g:each>
        </ul>
    </div>
    <g:hiddenField name="menuId" value="${menuInstance.id }" />
    <g:render template="/menuItem/templates/createMenuItemOverlay" plugin="content" />
    <g:render template="/menuItem/templates/editMenuItemOverlay" plugin="content" />

    <div class="separator" style="margin: 40px 0 50px"><span class="separator-text">Preview</span></div>

    <com:menu id="${menuInstance.id }" />

</body>
</html>
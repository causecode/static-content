<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'menu.label')}" />
    <title>Sort ${entityName } - ${menuInstance.name }</title>
    <r:require modules="menuItem" />
</head>
<body>
    <content tag="breadcrumb">
            <content:breadcrumb map="['/menu/list': 'Menu List', 'active':(menuInstance.name)]"/>
    </content>
    <div class="page-header">
        <h1 class="inline">
            Sort Menu Items
        </h1>
        <span class="btn-group pull-right">
            <a href="#create-menu-item-overlay" class="btn btn-primary btn-sm" data-toggle="modal">
                <i class="icon-plus"></i> Menu Item</a>
        </span>
    </div>

    <div id="menu-item-sorting">
        <div id="alertMessageLink" class="alert alert-success hide"></div>
        <ul id="menuItemList" class="thumbnails menuItem" data-parent-id="" data-menu-id="${menuInstance?.id }">
            <g:each in="${menuItemInstanceList}" var="menuItemInstance">
                <g:if test="${menuItemInstance && !menuItemInstance?.parent }">
                    <li id="${menuItemInstance?.id }" class="thumbnail" data-menu-item-id="${menuItemInstance?.id }">
                        <i class="icon-move"></i>
                        <strong>${menuItemInstance?.title}</strong>
                        <a id="editMenuItem" href="#" class="pull-right"><i class="icon-pencil"></i></a>
                        <com:bootstrapMediaMenu id="${menuItemInstance?.id}" />
                    </li>
                </g:if>
            </g:each>
        </ul>
    </div>
    <g:render template="/menuItem/templates/createMenuItemOverlay" plugin="content" />
    <g:render template="/menuItem/templates/editMenuItemOverlay" plugin="content" />

    <div class="separator" style="margin: 40px 0 50px"><span class="separator-text">Preview</span></div>

    <com:menu id="${menuInstance.id }" />

</body>
</html>
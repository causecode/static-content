<div class="navbar ${menuCss ?: '' } main-menu" id="main-menu-${menuInstance?.id }" data-menu-id="${menuInstance?.id}">
    <div class="navbar-inner">
        <div class="container">
            <a class="brand" href="#">
                <g:if test="${logoUrl }">
                    <img class="logo" src="${logoUrl }" />
                </g:if>
                <g:if test="${title }">
                    <span style="${titleCss }">
                        ${title }
                    </span>
                </g:if>
            </a>
            <ul class="nav pull-right" id="menu-item-${menuInstance?.id }">
                <g:each in="${menuItemList}">
                    <com:menu id="${it.id}"></com:menu>
                </g:each>
            </ul>
        </div>
    </div>
</div>
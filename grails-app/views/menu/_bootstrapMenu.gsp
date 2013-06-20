<div class="navbar ${menuCss ?: '' } main-menu" id="main-menu-${menuInstance?.id }" data-menu-id="${menuInstance?.id}">
    <div class="navbar-inner">
        <div class="container">
            <a class="brand" href="#">
                <g:if test="${brandURL }">
                    <img class="logo" src="${brandURL }" />
                </g:if>
                <g:if test="${brandName }">
                    <span style="${brandCss }" id="brand-name">
                        ${brandName }
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
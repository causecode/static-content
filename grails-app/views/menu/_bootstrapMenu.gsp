<div class="navbar ${menuCss ?: '' } main-menu" id="main-menu-${menuInstance?.id }" data-menu-id="${menuInstance?.id}">
    <div class="navbar-inner">
        <div class="container">
            <a class="brand" href="/">
                <g:if test="${brandLogo }">
                    <img class="logo" src="${brandLogo }" />
                </g:if>
                <g:if test="${brandName }">
                    <span style="${brandCss }" id="brand-name">
                        ${brandName }
                    </span>
                </g:if>
            </a>
            <ul class="nav pull-right" role="navigation" id="menu-item-${menuInstance?.id }">
                <g:each in="${menuItemList}">
                    <g:if test="${!it.parent }">
                        <g:if test="${it.childItems}">
                            <com:menu id="${it.id}" renderingSubMenu="true"></com:menu>
                        </g:if>
                        <g:else>
                            <com:menu id="${it.id}"></com:menu>
                        </g:else>
                    </g:if>
                </g:each>
            </ul>
        </div>
    </div>
</div>

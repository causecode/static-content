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
                        <com:menu id="${it.id}"></com:menu>
                    </g:if>
                </g:each>
            </ul>
            
            <!-- 
            <ul class="nav pull-right" role="navigation">
                <li class="dropdown">
                    <a id="drop1" href="#" role="button" class="dropdown-toggle" data-toggle="dropdown">Home</a>
                    <b class="caret"></b>
                    <ul class="dropdown-menu" role="menu" aria-lebelledby="drop1">
                        <li role="presentation">
                            <a role="menuitem"href="#">submenu1</a>
                         </li>
                         <li role="presentation">
                            <a role="menuitem"href="#">submenu2</a>
                         </li>
                         <li role="presentation">
                            <a role="menuitem"href="#">submenu3</a>
                         </li>
                    </ul>
                </li>
            </ul>
             -->
        </div>
    </div>
</div>

<com:canBeVisible instance="${menuItemInstance}">
    <g:if test="${menuItemInstance.childItems}">
        <li class="${renderingSubMenu ? 'dropdown-submenu' : 'dropdown' }">
            <a id="submenu-${menuItemInstance.id}" href='#' role="button" class="dropdown-toggle" data-toggle="dropdown">
                ${menuItemInstance.title}
                <g:if test="${!renderingSubMenu}">
                    <b class="caret"></b>
                </g:if>
            </a>
            <ul class="dropdown-menu">
                <g:each in="${menuItemInstance.childItems}" var="childItemInstance">
                    <g:render template="/menuItem/templates/menuItem" model="[menuItemInstance: childItemInstance,
                        renderingSubMenu: true]" plugin="content" />
                </g:each>
            </ul>
        </li>
    </g:if>
    <g:else>
        <li>
            <a href="${menuItemInstance.url}" class="item-link">
                ${menuItemInstance.title}
            </a>
        </li>
    </g:else>
</com:canBeVisible>
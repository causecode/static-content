<g:if test="${!menuItemInstance.parent}">
    <g:if test="${menuItemInstance.childItems}">
        <li class="dropdown">
            <a id="submenu-${menuItemInstance.id}" href='#' class="dropdown-toggle" data-toggle="dropdown">
                ${menuItemInstance.title}
            <b class="caret"></b>
            </a>
            <ul class="dropdown-menu" role="menu" aria-labelledby='${menuItemInstance.id}'>
                <g:each in="${menuItemInstance.childItems}" var="childItemInstance">
                    <g:if test="${childItemInstance.childItems}">
                        <g:each in="${menuItemInstance.childItems}" var="subChildItemInstance">
                            <com:menu id="${subChildItemInstance.id}"></com:menu>
                        </g:each>
                    </g:if>
                    <g:else>
                        <a href='${childItemInstance.url}' class="item-link">${childItemInstance.title}</a>
                    </g:else>
                </g:each>
            </ul>
        </li>
    </g:if>
    <g:else>
        <li>
            <a href="${menuItemInstance.url}" class="item-link">${menuItemInstance.title}</a>
        </li>
    </g:else>
</g:if>
<g:else>
    <g:if test="${menuItemInstance.childItems}">
        <li class="dropdown">
            <a id="submenu-${menuItemInstance.id}" href='#' class="dropdown-toggle" data-toggle="dropdown">
                ${menuItemInstance.title}
                <b class="caret"></b>
            </a>
            <ul class="dropdown-menu" role="menu" aria-labelledby='${menuItemInstance.id}'>
                <g:each in="${menuItemInstance.childItems}" var="childItemInstance">
                    <g:if test="${childItemInstance.childItems}">
                        <g:each in="${menuItemInstance.childItems}" var="subChildItemInstance">
                            <com:menu id="${subChildItemInstance.id}"></com:menu>
                        </g:each>
                    </g:if>
                    <g:else>
                        <a href='${childItemInstance.url}' class="item-link">${childItemInstance.title}</a>
                    </g:else>
                </g:each>
            </ul>
        </li>
    </g:if>
    <g:else>
        <li>
            <a href="#" class="item-link">${menuItemInstance.title}</a>
        </li>
    </g:else>
</g:else>
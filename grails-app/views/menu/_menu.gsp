<g:if test="${menuItemInstance.childItems}">
    <li class="dropdown">
        <a id="submenu-${menuItemInstance.id}" href='#' role="button" class="dropdown-toggle" data-toggle="dropdown">
            ${menuItemInstance.title} </a>
        <b class="caret"></b>
        <g:each in="${menuItemInstance.childItems}" var="childItemInstance">
            <g:if test="${childItemInstance.childItems}">
                <g:each in="${menuItemInstance.childItems}" var="subChildItemInstance">
                    <ul class="dropdown-menu" role="menu" aria-lebelledby="submenu-${menuItemInstance.id}">
                        <com:menu id="${subChildItemInstance.id}"></com:menu>
                    </ul>
                </g:each>
            </g:if>
            <g:else>
                <li role="presentation">
                    <a href='${childItemInstance.url}' >${childItemInstance.title}</a>
                </li>
            </g:else>
        </g:each>
</g:if>
<g:else>
   <ul class="dropdown-menu">
        <li class="dropdown">
            <a href="${menuItemInstance.url}" class="item-link">${menuItemInstance.title}</a>
        </li>
    </ul>
</g:else>

<!-- 
<g:if test="${menuItemInstance.childItems}">
    <li class="dropdown-submenu">
        <a id="submenu-${menuItemInstance.id}" href='#' class="dropdown-toggle" data-toggle="dropdown">
            ${menuItemInstance.title}
            <b class="caret"></b>
        </a>        
        <g:each in="${menuItemInstance.childItems}" var="childItemInstance">
            <g:if test="${childItemInstance.childItems}">
                <g:each in="${menuItemInstance.childItems}" var="subChildItemInstance">
                    <ul class="dropdown-menu">
                        <com:subMenu id="${subChildItemInstance.id}"></com:subMenu>
                    </ul>
                </g:each>
            </g:if>
            <g:else>
                <li>
                    <a href='${childItemInstance.url}' >${childItemInstance.title}</a>
                </li>
            </g:else>
        </g:each>
    </li>
</g:if>
<g:else>
    <ul class="dropdown-menu">
        <li>
            <a href="${menuItemInstance.url}" class="item-link">${menuItemInstance.title}</a>
        </li>
    </ul>
</g:else>
-->
<!-- 
<div class="pull-left">
    <div class="dropdown clearfix">
        <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu">
            <com:menu id="${it.id}"></com:menu>
        </ul>
    </div>
</div>
 -->
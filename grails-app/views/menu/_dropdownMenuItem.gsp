<a id="submenu-${menuItemInstance.id}" href='#' role="button" class="dropdown-toggle" data-toggle="dropdown">
    ${menuItemInstance.title} 
    <g:if test="${!renderingSubMenu}">
        <b class="caret"></b>
    </g:if>
</a>
<ul class="dropdown-menu">
    <g:each in="${menuItemInstance.childItems}" var="childItemInstance">
        <g:if test="${childItemInstance.childItems}">
            <com:menu id="${childItemInstance.id}" renderingSubMenu="true" ></com:menu>
        </g:if>
        <g:else>
            <g:if test="${menuItemInstance.showOnlyWhenLoggedIn }">
                <sec:ifLoggedIn>
                    <g:if test="${menuItemInstance.roles }" >
                        <sec:ifAnyGranted roles="${menuItemInstance.roles }">
                            <g:render template="/menu/simpleMenuItem" model="['menuItemInstance':childItemInstance]"/>
                        </sec:ifAnyGranted>
                    </g:if>
                </sec:ifLoggedIn>
            </g:if>
        </g:else>
</g:each>
</ul>
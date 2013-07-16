<g:if test="${menuItemInstance.childItems}">
    <g:if test="${menuItemInstance.showOnlyWhenLoggedIn }" >
        <sec:ifLoggedIn>
            <g:if test="${menuItemInstance.roles }" >
                <sec:ifAnyGranted roles="${menuItemInstance.roles }">
                    <li class="${renderingSubMenu ? 'dropdown-submenu' : 'dropdown' }">
                        <g:render template="/menu/dropdownMenuItem" model="['menuItemInstance':menuItemInstance]"/>
                    </li>
                </sec:ifAnyGranted>
            </g:if>
        </sec:ifLoggedIn>
    </g:if>
</g:if>
<g:else>
        <g:if test="${menuItemInstance.showOnlyWhenLoggedIn }" >
            <sec:ifLoggedIn>
                <g:if test="${menuItemInstance.roles }" >
                    <sec:ifAnyGranted roles="${menuItemInstance.roles }">
                        <g:render template="/menu/simpleMenuItem" model="['menuItemInstanc':menuItemInstance]"/>
                    </sec:ifAnyGranted>
                </g:if>
            </sec:ifLoggedIn>
        </g:if>
</g:else>
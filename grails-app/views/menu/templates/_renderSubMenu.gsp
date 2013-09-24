<ul class="menuItem" data-parent-id="${menuItemInstance?.id }" data-menu-id="${menuInstance?.id }">
    <g:if test="${menuItemInstance?.childItems}">
        <g:each in="${menuItemInstance?.childItems}" var="childItemInstance">
            <li id="${childItemInstance?.id }" class="" data-menu-item-id="${childItemInstance?.id}">
                <i class="icon-move"></i>
                <strong>${childItemInstance?.title}</strong>
                <a id="editMenuItem" href="#" class="pull-right"><i class="icon-pencil"></i></a>
                <g:if test="${childItemInstance.childItems }">
                    <g:render template="/menu/templates/renderSubMenu" model="[menuItemInstance: childItemInstance]" />
                </g:if>
            </li>
        </g:each>
    </g:if>
</ul>
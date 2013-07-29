<ul id="menuItemList" class="thumbnails" data-parent-id="${menuItemInstance?.id }" data-menu-id="${menuInstance?.id }">
    <g:if test="${menuItemInstance?.childItems}">
        <g:each in="${menuItemInstance?.childItems}" var="childItemInstance">
            <li class="thumbnail" data-menu-item-id="${childItemInstance?.id}">
                <p>
                    <strong> ${childItemInstance?.title}
                    </strong>
                </p>
               <g:if test="${childItemInstance.childItems }">
                    <com:bootstrapMediaMenu id="${childItemInstance?.id}"></com:bootstrapMediaMenu>
                </g:if>
            </li>
        </g:each>
    </g:if>
    <g:else>
      
    </g:else>
</ul>
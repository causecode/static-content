<g:if test="${menuItemInstance?.childItems}">
    <div id = "${menuItemInstance?.id }" class="media ui-sortable " >
    <ul id="sortable" class="ui-sortable media-list" data-parent-menu-item-id="${menuItemInstance?.id }">
        <g:each in="${menuItemInstance?.childItems}" var="childItemInstance">
            <li id="${childItemInstance?.id }" class="ui-state-default media " data-item-id="${childItemInstance?.id}">
                <div id="${childItemInstance?.id}" class="media-body draggable ui-draggable ui-widget-content thumbnail">
                    <p class="ex1 "> ${childItemInstance?.title}</p>
                    <com:bootstrapMediaMenu id="${childItemInstance?.id}"></com:bootstrapMediaMenu>
                </div>
            </li>
        </g:each>
    </ul>
    </div>
</g:if>

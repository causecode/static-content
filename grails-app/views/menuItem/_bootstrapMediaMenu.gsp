<g:if test="${menuItemInstance?.childItems}">
    <div id = "${menuItemInstance?.id }" class="media ui-sortable" >
    <ul id="sortable" class="ui-sortable media-list">
        <g:each in="${menuItemInstance?.childItems}" var="childItemInstance">
            <li class="ui-state-default media " data-item-id="${childItemInstance?.id}">
                <div id="${childItemInstance?.id}" class="media-body draggable ui-draggable ui-widget-content">
                    <p class="ex1 thumbnail"> ${childItemInstance?.title}</p>
                    <com:bootstrapMediaMenu id="${childItemInstance?.id}"></com:bootstrapMediaMenu>
                </div>
            </li>
        </g:each>
    </ul>
    </div>
</g:if>

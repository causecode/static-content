<g:if test="${instance }">
    <li class="${topLevel ? 'thumbnail ' : '' }menu-item" data-menu-item-id="${instance.id}" id="menu-item-${instance.id }">
        <i class="icon-move fa fa-arrows"></i>
        <strong class="title">${instance.title}</strong>
        <span class="pull-right">
            <a class="edit-menu-item" href="#"><i class="fa fa-pencil"></i></a>
            <a class="delete-menu-item" href="#"><i class="fa fa-trash-o"></i></a>
        </span>
        <ul class="menu-item-container" data-parent-id="${instance.id }" style="background: red">
            <g:each in="${instance.childItems}" var="childItemInstance">
                <g:render template="/menuItem/templates/forEdit" model="[instance: childItemInstance, topLevel: false]" />
            </g:each>
        </ul>
    </li>
</g:if>
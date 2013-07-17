<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'menuItem.label', default: 'MenuItem')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
    <div class="page-header">
        <h1>
            <g:message code="default.show.label" args="[entityName]" />
        </h1>
    </div>

    <com:bootstrapMenu id="${menuItemInstance?.id}"/> 

    <ol class="property-list menuItem">
        
        <g:if test="${menuItemInstance?.parent}">
            <li class="fieldcontain">
                <span id="parent-label" class="property-label">
                    <g:message code="menuItem.parent.label" default="Parent" />
                </span>
                <span class="property-value" aria-labelledby="parent-label">
                    <g:link controller="menuItem" action="show" id="${menuItemInstance?.parent?.id}">${menuItemInstance?.parent?.title.encodeAsHTML()}</g:link>
                </span>
            </li>
        </g:if>

        <g:if test="${menuItemInstance?.menu}">
            <li class="fieldcontain">
                <span id="menu-label" class="property-label">
                    <g:message code="menuItem.menu.label" default="Menu" />
                </span>
                <span class="property-value" aria-labelledby="menu-label">
                    <g:link controller="menu" action="show" id="${menuItemInstance?.menu?.id}">${menuItemInstance?.menu?.name.encodeAsHTML()}</g:link>
                </span>
            </li>
        </g:if>

        <g:if test="${menuItemInstance?.dateCreated}">
            <li class="fieldcontain">
                <span id="dateCreated-label" class="property-label">
                    <g:message code="menuItem.dateCreated.label" default="Date Created" />
                </span>
                <span class="property-value" aria-labelledby="dateCreated-label">
                    <g:formatDate date="${menuItemInstance?.dateCreated}" />
                </span>
            </li>
        </g:if>

        <g:if test="${menuItemInstance?.lastUpdated}">
            <li class="fieldcontain">
                <span id="lastUpdated-label" class="property-label">
                    <g:message code="menuItem.lastUpdated.label" default="Last Updated" />
                </span>
                <span class="property-value" aria-labelledby="lastUpdated-label">
                    <g:formatDate date="${menuItemInstance?.lastUpdated}" />
                </span>
            </li>
        </g:if>

        <g:if test="${menuItemInstance?.title}">
            <li class="fieldcontain">
                <span id="title-label" class="property-label">
                    <g:message code="menuItem.title.label" default="Title" />
                </span>
                <span class="property-value" aria-labelledby="title-label">
                    <g:fieldValue bean="${menuItemInstance}" field="title"/>
                </span>
            </li>
        </g:if>

        <g:if test="${menuItemInstance?.url}">
            <li class="fieldcontain">
                <span id="url-label" class="property-label">
                    <g:message code="menuItem.url.label" default="Url" />
                </span>
                <span class="property-value" aria-labelledby="url-label">
                    <g:fieldValue bean="${menuItemInstance}" field="url"/>
                </span>
            </li>
        </g:if>

        <g:if test="${menuItemInstance?.roles}">
            <li class="fieldcontain">
                <span id="roles-label" class="property-label">
                    <g:message code="menuItem.roles.label" default="Roles" />
            </span>
                <span class="property-value" aria-labelledby="roles-label">
                    <g:fieldValue bean="${menuItemInstance}" field="roles"/>
                </span>
            </li>
        </g:if>

        <g:if test="${menuItemInstance?.childItems}">
            <li class="fieldcontain">
    .           <span id="childItems-label" class="property-label">
                    <g:message code="menuItem.childItems.label" default="Child Items" />
                </span>
                <g:each in="${menuItemInstance.childItems}" var="c">
                    </br>
                    <span class="property-value" aria-labelledby="childItems-label">
                        <g:link controller="menuItem" action="show" id="${c.id}">${c?.title.encodeAsHTML()}</g:link>
                    </span>
                </g:each>
            </li>
        </g:if>
    </ol>

    <g:form>
        <fieldset class="form-actions">
            <g:hiddenField name="id" value="${menuItemInstance?.id}" />
            <g:link class="btn btn-primary" action="edit" id="${menuItemInstance?.id}">
                <g:message code="default.button.edit.label" default="Edit" />
            </g:link>
            <g:actionSubmit class="btn btn-danger" action="delete"
                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
        </fieldset>
    </g:form>
</body>
</html>
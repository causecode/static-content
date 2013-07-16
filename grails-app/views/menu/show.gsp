
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'menu.label', default: 'Menu')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
    <div class="page-header">
        <h1>
            <g:message code="default.show.label" args="[entityName]" />
        </h1>
    </div>
    <com:bootstrapMenu id="${menuInstance?.id}"/> 
    <ol class="property-list menu">
        <g:if test="${menuInstance?.name}">
            <li class="fieldcontain">
                <span id="name-label" class="property-label">
                    <g:message code="menu.name.label" default="Name" />
                </span>
                <span class="property-value" aria-labelledby="name-label">
                    <g:fieldValue bean="${menuInstance}" field="name"/>
                </span>
            </li>
        </g:if>

        <g:if test="${menuInstance?.dateCreated}">
            <li class="fieldcontain">
                <span id="dateCreated-label" class="property-label">
                    <g:message code="menu.dateCreated.label" default="Date Created" />
                </span>
                <span class="property-value" aria-labelledby="dateCreated-label">
                    <g:formatDate date="${menuInstance?.dateCreated}" />
                </span>
            </li>
        </g:if>
        
        <g:if test="${menuInstance?.lastUpdated}">
            <li class="fieldcontain">
                <span id="lastUpdated-label" class="property-label">
                    <g:message code="menu.lastUpdated.label" default="Last Updated" />
                </span>
                <span class="property-value" aria-labelledby="lastUpdated-label">
                    <g:formatDate date="${menuInstance?.lastUpdated}" />
                </span>
            </li>
        </g:if>
        
        <g:if test="${menuInstance?.roles}">
            <li class="fieldcontain">
                <span id="roles-label" class="property-label">
                    <g:message code="menu.roles.label" default="Roles" />
                </span>
                <span class="property-value" aria-labelledby="roles-label">
                    <g:fieldValue bean="${menuInstance}" field="roles"/>
                </span>
            </li>
        </g:if>

        <g:if test="${menuInstance?.showOnlyWhenLoggedIn}">
            <li class="fieldcontain">
                <span id="showOnlyWhenLoggedIn-label" class="property-label">
                    <g:message code="menu.showOnlyWhenLoggedIn.label" default="Show Only When Logged In" />
                </span>
            </li>
        </g:if>

        <g:if test="${menuInstance?.menuItems}">
            <li class="fieldcontain">
                <span id="menuItems-label" class="property-label">
                    <g:message code="menu.menuItems.label" default="Menu Items" />
                </span>
                <g:each in="${menuInstance.menuItems}" var="m">
                    </br>
                    <span class="property-value" aria-labelledby="menuItems-label">
                        <g:link controller="menuItem" action="show" id="${m.id}">${m?.title.encodeAsHTML()}</g:link>
                    </span>
                </g:each>
            </li>
        </g:if>

    </ol>
    <g:form>
        <fieldset class="form-actions">
            <g:hiddenField name="id" value="${menuInstance?.id}" />
            <g:link class="btn btn-primary" action="edit" id="${menuInstance?.id}">
                <g:message code="default.button.edit.label" default="Edit" />
            </g:link>
            <g:actionSubmit class="btn btn-danger" action="delete"
                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
        </fieldset>
    </g:form>
</body>
</html>
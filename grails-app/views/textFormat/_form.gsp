<g:hasErrors bean="${textFormatInstance}">
    <ul class="text-error">
        <g:eachError bean="${textFormatInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                <g:message error="${error}" /></li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="control-group ${hasErrors(bean: textFormatInstance, field: 'name', 'error')}">
    <label class="control-label" for="name">
        <g:message code="textFormat.name.label" default="Name" />
    </label>
    <div class="controls">
        <g:textField name="name" value="${textFormatInstance?.name}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: textFormatInstance, field: 'roles', 'error')}">
    <label class="control-label" for="roles">
        <g:message code="textFormat.roles.label" default="Roles" />
    </label>
    <div class="controls">
        <g:textField name="roles" value="${textFormatInstance?.roles}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: textFormatInstance, field: 'allowedTags', 'error')}">
    <label class="control-label" for="allowedTags">
        <g:message code="textFormat.allowedTags.label" default="Allowed Tags" />
    </label>
    <div class="controls">
        <g:textField name="allowedTags" value="${textFormatInstance?.allowedTags}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: textFormatInstance, field: 'editor', 'error')}">
    <label class="control-label" for="editor">
        <g:message code="textFormat.editor.label" default="Editor" />
    </label>
    <div class="controls">
        <g:checkBox name="editor" value="${textFormatInstance?.editor}" />
    </div>
</div>


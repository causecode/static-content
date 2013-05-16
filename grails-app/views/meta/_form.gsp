<g:hasErrors bean="${metaInstance}">
    <ul class="text-error">
        <g:eachError bean="${metaInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                <g:message error="${error}" /></li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="control-group ${hasErrors(bean: metaInstance, field: 'type', 'error')}">
    <label class="control-label" for="type">
        <g:message code="meta.type.label" default="Type" />
    </label>
    <div class="controls">
        <g:textField name="type" required="" value="${metaInstance?.type}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: metaInstance, field: 'value', 'error')}">
    <label class="control-label" for="value">
        <g:message code="meta.value.label" default="Value" />
    </label>
    <div class="controls">
        <g:textField name="value" required="" value="${metaInstance?.value}"/>
    </div>
</div>


<g:hasErrors bean="${currencyInstance}">
    <ul class="text-error">
        <g:eachError bean="${currencyInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                <g:message error="${error}" /></li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="control-group ${hasErrors(bean: currencyInstance, field: 'code', 'error')}">
    <label class="control-label" for="code">
        <g:message code="currency.code.label" default="Code" />
    </label>
    <div class="controls">
        <g:textField name="code" required="" value="${currencyInstance?.code}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: currencyInstance, field: 'name', 'error')}">
    <label class="control-label" for="name">
        <g:message code="currency.name.label" default="Name" />
    </label>
    <div class="controls">
        <g:textField name="name" required="" value="${currencyInstance?.name}"/>
    </div>
</div>


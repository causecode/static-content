<g:hasErrors bean="${inputWidgetInstance}">
    <ul class="text-error">
        <g:eachError bean="${inputWidgetInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                <g:message error="${error}" /></li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="control-group ${hasErrors(bean: inputWidgetInstance, field: 'label', 'error')}">
    <label class="control-label" for="label">
        <g:message code="inputWidget.label.label" default="Label" />
    </label>
    <div class="controls">
        <g:textField name="label" required="" value="${inputWidgetInstance?.label}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: inputWidgetInstance, field: 'name', 'error')}">
    <label class="control-label" for="name">
        <g:message code="inputWidget.name.label" default="Name" />
    </label>
    <div class="controls">
        <g:select name="inputWidgetName" from="${['checkBox','select','textArea','textField']}" value="${inputWidgetInstance?.name}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: inputWidgetInstance, field: 'defaultValue', 'error')}">
    <label class="control-label" for="defaultValue">
        <g:message code="inputWidget.defaultValue.label" default="Default Value" />
    </label>
    <div class="controls">
        <g:textField name="defaultValue" value="${inputWidgetInstance?.defaultValue}"/>
    </div>
</div>


<div class="control-group hide ${hasErrors(bean: inputWidgetInstance, field: 'widgetKey', 'error')}" id="widgetKey">
    <label class="control-label" for="widgetKey">
        <g:message code="inputWidget.widgetKey.label" default="Widget Key" />
    </label>
    <div class="controls">
        <g:textField name="widgetKey" value="${inputWidgetInstance?.widgetKey}" />
    </div>
</div>

<div class="control-group hide ${hasErrors(bean: inputWidgetInstance, field: 'widgetValue', 'error')}" id="widgetValue">
    <label class="control-label" for="widgetValue">
        <g:message code="inputWidget.widgetValue.label" default="Widget Value" />
    </label>
    <div class="controls">
        <g:textField name="widgetValue" value="${inputWidgetInstance?.widgetValue}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: inputWidgetInstance, field: 'helpText', 'error')}">
    <label class="control-label" for="helpText">
        <g:message code="inputWidget.helpText.label" default="Help Text" />
    </label>
    <div class="controls">
        <g:textField name="helpText" value="${inputWidgetInstance?.helpText}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: inputWidgetInstance, field: 'helpType', 'error')}">
    <label class="control-label" for="helpType">
        <g:message code="inputWidget.helpType.label" default="Help Type" />
    </label>
    <div class="controls">
        <g:select name="helpType" from="${['inline','block','tooltip','placeHolder']}" value="${inputWidgetInstance?.helpType}" />
    </div>
</div>


<div class="control-group ${hasErrors(bean: inputWidgetInstance, field: 'validate', 'error')}">
    <label class="control-label" for="validate">
        <g:message code="inputWidget.validate.label" default="Validate" />
    </label>
    <div class="controls">
        <g:textField name="validate" required="" value="${inputWidgetInstance?.validate}"/>
    </div>
</div>
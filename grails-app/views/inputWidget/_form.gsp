<div class="form-group" >
    <label class="control-label col-lg-3">
        <g:message code="inputWidget.label.label" default="Label" />
    </label>
    <div class="controls col-lg-7">
        <g:textField name="label" required="" value="${inputWidgetInstance?.label}" class="form-control"/>
    </div>
</div>

<div class="form-group" >
    <label class="control-label col-lg-3">
        <g:message code="inputWidget.name.label" default="Name" />
    </label>
    <div class="controls col-lg-7">
        <g:select name="name" class="inputWidgetName" required="" optionKey="${inputWidgetInstance?.id }" optionValue="${inputWidgetInstance?.name}" 
        from="${['textArea','textField']}" value="${inputWidgetInstance?.name}"
        noSelection="${['':'']}" class="form-control"></g:select>
    </div>
</div>

<div class="form-group" >
    <label class="control-label col-lg-3">
        <g:message code="inputWidget.defaultValue.label" default="Default Value" />
    </label>
    <div class="controls col-lg-7">
        <g:textField name="defaultValue" value="${inputWidgetInstance?.defaultValue}" class="form-control"/>
    </div>
</div>

<div class="form-group" >
    <label class="control-label col-lg-3">
        <g:message code="inputWidget.helpText.label" default="Help Text" />
    </label>
    <div class="controls col-lg-7">
        <g:textField name="helpText" value="${inputWidgetInstance?.helpText}" class="form-control"/>
    </div>
</div>

<div class="form-group" >
    <label class="control-label col-lg-3">
        <g:message code="inputWidget.helpType.label" default="Help Type" />
    </label>
    <div class="controls col-lg-7">
        <g:select name="helpType" from="${['inline','block','tooltip','placeHolder']}" value="${inputWidgetInstance?.helpType}" class="form-control"/>
    </div>
</div>


<div class="form-group" >
    <label class="control-label col-lg-3">
        <g:message code="inputWidget.validate.label" default="Validate" />
    </label>
    <div class="controls col-lg-7">
        <g:textField name="validate" required="" value="${inputWidgetInstance?.validate}" class="form-control"/>
    </div>
</div>
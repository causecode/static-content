<div class="form-group">
    <label class="control-label col-sm-3" for="type"> <g:message code="inputWidget.type.label" default="Type" />
    </label>
    <div class=" col-lg-6">
        <g:select name="${prefix}type" from="${com.cc.content.inputWidget.InputWidgetType?.values()}"
            keys="${com.cc.content.inputWidget.InputWidgetType.values()*.name()}" required=""
            class="form-control input-widget-type-selector" value="${inputWidgetInstance.type.toString()}" 
            noSelection="${['null':'Select One...']}"/>
    </div>
    <g:hiddenField name="${prefix}widgetType" value="${inputWidgetInstance.type}" />
</div>

<div class="form-group">
    <label class="control-label col-sm-3" for="label"> <g:message code="inputWidget.label.label" default="Label" />
    </label>
    <div class=" col-lg-6">
        <g:textField name="${prefix}label" value="${inputWidgetInstance.label}"
            class="form-control " />
    </div>
</div>

<div class="form-group">
    <label class="control-label col-sm-3" for="name"> <g:message code="inputWidget.name.label" default="Name" />
    </label>
    <div class=" col-lg-6">
        <g:textField name="${prefix}name" required="" value="${inputWidgetInstance.name}"
            class="form-control " />
    </div>
</div>

<div class="form-group widgetKeyValue hide ">
    <label class="control-label col-sm-3" for="widgetKeys"> <g:message code="inputWidget.widgetKeys.label"
            default="Widget Key" />
    </label>
    <div class=" col-lg-6">
        <g:textField name="${prefix}widgetKeys" value="${inputWidgetInstance.widgetKeys}"
            class="form-control" placeholder="Enter comma seperated Keys" />
    </div>
</div>

<div class="form-group widgetKeyValue hide ">
    <label class="control-label col-sm-3" for="widgetValues"> <g:message code="inputWidget.widgetValues.label"
            default="Widget Value" />
    </label>
    <div class=" col-lg-6">
        <g:textField name="${prefix}widgetValues" value="${inputWidgetInstance.widgetValues}"
            class="form-control " placeholder="Enter comma seperated Values" />
    </div>
</div>

<div class="form-group defaultValue hide ">
    <label class="control-label col-sm-3" for="defaultValue"> <g:message code="inputWidget.defaultValue.label"
            default="Default Value" />
    </label>
    <div class=" col-lg-6">
        <g:textField name="${prefix}defaultValue" value="${inputWidgetInstance.defaultValue}"
            class="form-control "
            placeholder="Enter comma seperated Values" />
    </div>
</div>

<div class="form-group">
    <label class="control-label col-sm-3" for="helpType"> <g:message code="inputWidget.helpType.label"
            default="Help Type" />
    </label>
    <div class=" col-lg-6">
        <g:select name="${prefix}helpType" from="${com.cc.content.inputWidget.InputWidgetHelpType?.values()}"
            keys="${com.cc.content.inputWidget.InputWidgetHelpType.values()*.name()}"
            value="${inputWidgetInstance.helpType.toString()}" required="" class="form-control " />
    </div>
</div>

<div class="form-group">
    <label class="control-label col-sm-3" for="helpText"> <g:message code="inputWidget.helpText.label"
            default="Help Text" />
    </label>
    <div class=" col-lg-6">
        <g:textField name="${prefix}helpText" value="${inputWidgetInstance.helpText}"
            class="form-control " />
    </div>
</div>

<div class="form-group validation ">
    <label class="control-label col-sm-3" for="validation"> <g:message code="inputWidget.validation.label"
            default="validation" />
    </label>
    <div class=" col-lg-6">
        <g:select name="${prefix}validation" from="${com.cc.content.inputWidget.InputWidgetValidation?.values()}"
            keys="${com.cc.content.inputWidget.InputWidgetValidation.values()*.name()}" required=""
            class="form-control " value="${inputWidgetInstance.validation.toString()}"
            multiple="true" />
    </div>
</div>

<div class="form-group defaultSelected hide ">
    <label class="control-label col-sm-3 " for="defaultSelected"> <g:message
            code="inputWidget.defaultSelected.label" default="Default Selected" />
    </label>
    <div class=" col-lg-6">
        <g:textField name="${prefix}defaultSelected" value="${inputWidgetInstance.defaultSelected}"
            class="form-control " />
    </div>
</div>

<div class="form-group noSelectionText hide ">
    <label class="control-label col-sm-3" for="noSelectionText"> <g:message code="inputWidget.noSelectionText.label"
            default="No Selection Text" />
    </label>
    <div class=" col-lg-6">
        <g:textField name="${prefix}noSelectionText" value="${inputWidgetInstance.noSelectionText}"
            class="form-control " />
    </div>
</div>

<div class="form-group minChar hide ">
    <label class="control-label col-sm-3" for="minChar"> <g:message code="inputWidget.minChar.label"
            default="Min Char" />
    </label>
    <div class="col-sm-3">
        <g:field name="${prefix}minChar" type="number" value="${inputWidgetInstance.minChar}"
            class="form-control digits " />
    </div>
</div>

<div class="form-group maxChar hide ">
    <label class="control-label col-sm-3" for="maxChar"> <g:message code="inputWidget.maxChar.label"
            default="Max Char" />
    </label>
    <div class="col-sm-3">
        <g:field name="${prefix}maxChar" type="number" value="${inputWidgetInstance.maxChar}"
            class="form-control digits " />
    </div>
</div>

<div class="form-group minValueRange hide ">
    <label class="control-label col-sm-3" for="minValueRange"> <g:message code="inputWidget.minValueRange.label"
            default="Min Value Range" />
    </label>
    <div class="col-sm-3">
        <g:field name="${prefix}minValueRange" type="number" value="${inputWidgetInstance.minValueRange}"
            class="form-control digits " />
    </div>
</div>

<div class="form-group maxValueRange hide ">
    <label class="control-label col-sm-3" for="maxValueRange"> <g:message code="inputWidget.maxValueRange.label"
            default="Max Value Range" />
    </label>
    <div class="col-sm-3">
        <g:field name="${prefix}maxValueRange" type="number" value="${inputWidgetInstance.maxValueRange}"
            class="form-control digits " />
    </div>
</div>
<html>
<head>
<r:require modules="inputWidget" />
</head>
<body>
    <div class="form-group">
        <label class="control-label col-lg-3" for="type"> 
            <g:message code="inputWidget.type.label" default="Type" />
        </label>
        <div class=" col-lg-6">
            <g:select name="type" from="${com.cc.content.inputWidget.InputWidgetType?.values()}"
                keys="${com.cc.content.inputWidget.InputWidgetType.values()*.name()}" required="" class="form-control"
                value="${inputWidgetInstance?.type}" />
        </div>
        <g:hiddenField name="widgetType" value="${inputWidgetInstance?.type}" />
    </div>

    <div class="form-group">
        <label class="control-label col-lg-3" for="label"> 
            <g:message code="inputWidget.label.label" default="Label" />
        </label>
        <div class=" col-lg-6">
            <g:textField name="label" required="" value="${inputWidgetInstance?.label}" class="form-control" />
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-lg-3" for="name"> 
            <g:message code="inputWidget.name.label" default="Name" />
        </label>
        <div class=" col-lg-6">
            <g:textField name="name" required="" value="${inputWidgetInstance?.name}" class="form-control" />
        </div>
    </div>

    <div class="form-group widgetKeyValue hide">
        <label class="control-label col-lg-3" for="widgetKeys"> 
            <g:message code="inputWidget.widgetKeys.label" default="Widget Key" />
        </label>
        <div class=" col-lg-6">
            <g:textField name="widgetKeys" required="" value="${inputWidgetInstance?.widgetKeys}" class="form-control" />
        </div>
    </div>

    <div class="form-group widgetKeyValue hide">
        <label class="control-label col-lg-3" for="widgetValues"> 
            <g:message code="inputWidget.widgetValues.label" default="Widget Value" />
        </label>
        <div class=" col-lg-6">
            <g:textField name="widgetValues" required="" value="${inputWidgetInstance?.widgetValues}"
                class="form-control" />
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-lg-3" for="defaultValue"> 
            <g:message code="inputWidget.defaultValue.label" default="Default Value" />
        </label>
        <div class=" col-lg-6">
            <g:textField name="defaultValue" value="${inputWidgetInstance?.defaultValue}" class="form-control" />
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-lg-3" for="helpType"> 
            <g:message code="inputWidget.helpType.label" default="Help Type" />
        </label>
        <div class=" col-lg-6">
            <g:select name="helpType" from="${com.cc.content.inputWidget.InputWidgetHelpType?.values()}"
                keys="${com.cc.content.inputWidget.InputWidgetHelpType.values()*.name()}"
                value="${inputWidgetInstance?.helpType}" required="" class="form-control" />
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-lg-3" for="helpText"> 
            <g:message code="inputWidget.helpText.label" default="Help Text" />
        </label>
        <div class=" col-lg-6">
            <g:textField name="helpText" value="${inputWidgetInstance?.helpText}" class="form-control" />
        </div>
    </div>

    <div class="form-group validation ">
        <label class="control-label col-lg-3" for="validation"> 
            <g:message code="inputWidget.validation.label" default="validation" />
        </label>
        <div class=" col-lg-6">
            <g:select name="validation" from="${com.cc.content.inputWidget.InputWidgetValidation?.values()}"
                keys="${com.cc.content.inputWidget.InputWidgetValidation.values()*.name()}" required=""
                class="form-control" value="${inputWidgetInstance?.validation}" />
        </div>
    </div>

    <div class="form-group defaultSelected hide">
        <label class="control-label col-lg-3 " for="defaultSelected"> 
            <g:message code="inputWidget.defaultSelected.label" default="Default Selected" />
        </label>
        <div class=" col-lg-6">
            <g:textField name="defaultSelected" value="${inputWidgetInstance?.defaultSelected}" class="form-control"/>
        </div>
    </div>

    <div class="form-group noSelected hide">
        <label class="control-label col-lg-3" for="noSelected"> 
            <g:message code="inputWidget.noSelected.label" default="No Selected" />
        </label>
        <div class=" col-lg-6">
            <g:textField name="noSelected" value="${inputWidgetInstance?.noSelected}" class="form-control"/>
        </div>
    </div>

    <div class="form-group minChar hide">
        <label class="control-label col-lg-3" for="minChar"> 
            <g:message code="inputWidget.minChar.label" default="Min Char" />
        </label>
        <div class=" col-lg-3">
            <g:field name="minChar" type="number" value="${inputWidgetInstance?.minChar}" class="form-control"/>
        </div>
    </div>

    <div class="form-group maxChar hide">
        <label class="control-label col-lg-3" for="maxChar"> 
            <g:message code="inputWidget.maxChar.label" default="Max Char" />
        </label>
        <div class=" col-lg-3">
            <g:field name="maxChar" type="number" value="${inputWidgetInstance?.maxChar}" class="form-control"/>
        </div>
    </div>

    <div class="form-group minValueRange hide">
        <label class="control-label col-lg-3" for="minValueRange"> 
            <g:message code="inputWidget.minValueRange.label" default="Min Value Range" />
        </label>
        <div class=" col-lg-3">
            <g:field name="minValueRange" type="number" value="${inputWidgetInstance?.minValueRange}" class="form-control"/>
        </div>
    </div>

    <div class="form-group maxValueRange hide">
        <label class="control-label col-lg-3" for="maxValueRange"> 
            <g:message code="inputWidget.maxValueRange.label" default="Max Value Range" />
        </label>
        <div class=" col-lg-3">
            <g:field name="maxValueRange" type="number" value="${inputWidgetInstance?.maxValueRange}" class="form-control"/>
        </div>
    </div>
</body>

</html>

<div class="form-group" >
    <label class="control-label col-lg-3" for="type">
        <g:message code="inputWidget.type.label" default="Type" />
    </label>
    <div class=" col-lg-6">
        <g:select name="type" from="${com.cc.content.inputWidget.InputWidgetType?.values()}" 
            keys="${com.cc.content.inputWidget.InputWidgetType.values()*.name()}"
            required="" class="form-control" value="${inputWidgetInstance?.type}"/>
    </div>
    <g:hiddenField name="widgetType" value="${inputWidgetInstance?.type}"/>
</div>

<div class="form-group" >
    <label class="control-label col-lg-3" for="label">
        <g:message code="inputWidget.label.label" default="Label" />
    </label>
    <div class=" col-lg-6">
        <g:textField name="label" required="" value="${inputWidgetInstance?.label}" class="form-control"/>
    </div>
</div>

<div class="form-group" >
    <label class="control-label col-lg-3" for="name">
        <g:message code="inputWidget.name.label" default="Name" />
    </label>
    <div class=" col-lg-6">
        <g:textField name="name" required="" value="${inputWidgetInstance?.name}" class="form-control"/>
    </div>
</div>

<div class="form-group widgetKeyValue hide" >
    <label class="control-label col-lg-3" for="widgetKeys">
        <g:message code="inputWidget.widgetKeys.label" default="Widget Key" />
    </label>
    <div class=" col-lg-6">
        <g:textField name="widgetKeys" required="" value="${inputWidgetInstance?.widgetKeys}" class="form-control"/>
    </div>
</div>

<div class="form-group widgetKeyValue hide" >
    <label class="control-label col-lg-3" for="widgetValues">
        <g:message code="inputWidget.widgetValues.label" default="Widget Value" />
    </label>
    <div class=" col-lg-6">
        <g:textField name="widgetValues" required="" value="${inputWidgetInstance?.widgetValues}" class="form-control"/>
    </div>
</div>

<div class="form-group" >
    <label class="control-label col-lg-3" for="defaultValue">
        <g:message code="inputWidget.defaultValue.label" default="Default Value" />
    </label>
    <div class=" col-lg-6">
        <g:textField name="defaultValue" value="${inputWidgetInstance?.defaultValue}" class="form-control"/>
    </div>
</div>

<div class="form-group" >
    <label class="control-label col-lg-3" for="helpType">
        <g:message code="inputWidget.helpType.label" default="Help Type" />
    </label>
    <div class=" col-lg-6">
        <g:select name="helpType" from="${com.cc.content.inputWidget.InputWidgetHelpType?.values()}" 
            keys="${com.cc.content.inputWidget.InputWidgetHelpType.values()*.name()}"
            value="${inputWidgetInstance?.helpType}" required="" class="form-control"/>
    </div>
</div>

<div class="form-group" >
    <label class="control-label col-lg-3" for="helpText">
        <g:message code="inputWidget.helpText.label" default="Help Text" />
    </label>
    <div class=" col-lg-6">
        <g:textField name="helpText" value="${inputWidgetInstance?.helpText}" class="form-control"/>
    </div>
</div>

<div class="form-group" >
    <label class="control-label col-lg-3" for="validation">
        <g:message code="inputWidget.validation.label" default="validation" />
    </label>
    <div class=" col-lg-6">
         <g:select name="validation" from="${com.cc.content.inputWidget.InputWidgetValidation?.values()}" 
            keys="${com.cc.content.inputWidget.InputWidgetValidation.values()*.name()}"
            required="" class="form-control" value="${inputWidgetInstance?.validation}"/>
    </div>
</div>

<script>
$( document ).ready(function() {
    var $type = $('#widgetType').val()
    if($type == 'SELECT' || $type == 'MULTI_SELECT') {
        console.log($type)
        $("div.widgetKeyValue").removeClass("hide");
    } else {
        $("div.widgetKeyValue").addClass("hide");
    }
});
</script>
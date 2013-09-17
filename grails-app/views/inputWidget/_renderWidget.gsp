<%@ page import="com.cc.content.inputWidget.InputWidgetType" %>

<g:set var="inputWidgetType" value="${inputWidgetInstance.type}"></g:set>

<g:if test="${inputWidgetType == InputWidgetType.SELECT}">
    <g:select optionKey="id" optionValue="${{inputWidgetInstance.widgetValue?.toUpperCase()}}"
        class="inputWidget form-control" name="select" from="${inputWidgetValue}"
        data-input-widget-id="${inputWidgetInstance.id }" />
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.TEXT_AREA}">
    <g:textArea name="textArea" class="inputWidget form-control"
        data-input-widget-id="${inputWidgetInstance.id }" value="${inputWidgetValue}"
        data-help-type="${inputWidgetInstance.helpType }" data-help-text="${inputWidgetInstance.helpText }"></g:textArea>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.TEXT_FIELD}">
    <g:textField name="text" class="inputWidget form-control" value="${inputWidgetValue}"
        data-input-widget-id="${inputWidgetInstance.id }"
        data-help-type="${inputWidgetInstance.helpType }" data-help-text="${inputWidgetInstance.helpText }" />
</g:if>
<%@ page import="com.cc.content.inputWidget.InputWidgetType"%>

<g:set var="inputWidgetType" value="${inputWidgetInstance.type}"></g:set>
<g:set var="additionalAttrs" value="${additionalAttrs }" />

<g:if test="${inputWidgetType == InputWidgetType.SELECT}">
    <select class="inputWidget form-control ${classes }" data-input-widget-id="${inputWidgetInstance.id }" 
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>>
        <g:each in="${inputWidgetInstance.widgetKeys.tokenize(',')}" status="index" var="key">
            <option value="${key}"
                <content:widgetSelect inputWidgetValue="${inputWidgetValue}" inputWidgetKey="${inputWidgetInstance.widgetValues.tokenize(',')[index]?.trim()}" type="${inputWidgetType}"/>>
                ${inputWidgetInstance.widgetValues.tokenize(',')[index]?.trim()}
            </option>
        </g:each>
    </select>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.MULTI_SELECT}">
    <select class="inputWidget form-control ${classes }" multiple data-input-widget-id="${inputWidgetInstance.id }" 
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>>
        <g:each in="${inputWidgetInstance.widgetKeys.tokenize(',')}" status="index" var="key">
            <option value="${key }" 
                <content:widgetSelect inputWidgetValue="${inputWidgetValue}" inputWidgetKey="${inputWidgetInstance.widgetValues.tokenize(',')[index]?.trim()}" type="${inputWidgetType}"/>>
                ${inputWidgetInstance.widgetValues.tokenize(',')[index]?.trim()}
            </option>
        </g:each>
    </select>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.TEXT_AREA}">
    <textArea name="textArea" class="inputWidget form-control ${classes } textAreaWidget" 
        data-input-widget-id="${inputWidgetInstance.id }" value="${inputWidgetValue}"
        minlenght="${inputWidgetInstance.minChar ?: ''}" maxlenght="${inputWidgetInstance.maxChar ?: ''}"
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>></textArea>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.TEXT_FIELD}">
    <input type="text" class="inputWidget form-control ${classes } text" 
        data-input-widget-id="${inputWidgetInstance.id }" value="${inputWidgetValue}"
        minlenght="${inputWidgetInstance.minChar ?: ''}" maxlenght="${inputWidgetInstance.maxChar ?: ''}"
        min="${inputWidgetInstance.minValueRange}" max="${inputWidgetInstance.maxValueRange}"
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>/>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.CHECKBOX}">
    <input type="checkbox" class="inputWidget checkbox ${classes }" 
        data-input-widget-id="${inputWidgetInstance.id }" value="${inputWidgetValue}"
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>/>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.RADIO}">
    <input type="radio" class="inputWidget radio ${classes }" 
        data-input-widget-id="${inputWidgetInstance.id }" value="${inputWidgetValue}"
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>/>
</g:if>

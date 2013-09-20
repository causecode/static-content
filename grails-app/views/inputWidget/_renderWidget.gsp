<%@ page import="com.cc.content.inputWidget.InputWidgetType"%>

<g:set var="inputWidgetType" value="${inputWidgetInstance.type}"></g:set>

<g:if test="${inputWidgetType == InputWidgetType.SELECT}">
    <select class="inputWidget form-control" data-input-widget-id="${inputWidgetInstance.id }" 
        <content:widgetHelper id="${inputWidgetInstance?.id }"/>>
        <g:each in="${inputWidgetInstance?.widgetValues}">
            <option value="${it }">${it}</option>
        </g:each>
    </select>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.MULTI_SELECT}">
    <select class="inputWidget form-control" multiple data-input-widget-id="${inputWidgetInstance.id }" 
        <content:widgetHelper id="${inputWidgetInstance?.id }"/>>
        <g:each in="${inputWidgetInstance?.widgetValues}">
            <option value="${it }">${it}</option>
        </g:each>
    </select>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.TEXT_AREA}">
    <textArea name="textArea" class="inputWidget form-control textAreaWidget" 
        data-input-widget-id="${inputWidgetInstance.id }" value="${inputWidgetValue}"
        minlenght="${inputWidgetInstance?.minChar ?: ''}" maxlenght="${inputWidgetInstance?.maxChar ?: ''}"
        <content:widgetHelper id="${inputWidgetInstance?.id }"/>></textArea>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.TEXT_FIELD}">
    <input type="text" class="inputWidget form-control text" 
        data-input-widget-id="${inputWidgetInstance.id }" value="${inputWidgetValue}"
        minlenght="${inputWidgetInstance?.minChar ?: ''}" maxlenght="${inputWidgetInstance?.maxChar ?: ''}"
        min="${inputWidgetInstance?.minValueRange}" max="${inputWidgetInstance?.maxValueRange}"
        <content:widgetHelper id="${inputWidgetInstance?.id }"/>/>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.CHECKBOX}">
    <input type="checkbox" class="inputWidget checkbox" 
        data-input-widget-id="${inputWidgetInstance.id }" value="${inputWidgetValue}"
        <content:widgetHelper id="${inputWidgetInstance?.id }"/>/>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.RADIO}">
    <input type="radio" class="inputWidget radio" 
        data-input-widget-id="${inputWidgetInstance.id }" value="${inputWidgetValue}"
        <content:widgetHelper id="${inputWidgetInstance?.id }"/>/>
</g:if>

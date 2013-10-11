<%@ page import="com.cc.content.inputWidget.InputWidgetType"%>

<g:set var="inputWidgetType" value="${inputWidgetInstance.type}"></g:set>
<g:set var="additionalAttrs" value="${additionalAttrs }" />

<g:if test="${inputWidgetType == InputWidgetType.SELECT}">
    <select class="inputWidget select form-control ${classes }" data-input-widget-id="${inputWidgetInstance.id }" 
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>>
        <g:each in="${inputWidgetInstance.widgetKeys.tokenize(',')}" status="index" var="key">
            <option value="${key}" ${key == inputWidgetValue ? 'selected' : '' } >
                ${inputWidgetInstance.widgetValues.tokenize(',')[index]?.trim()}
            </option>
        </g:each>
    </select>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.MULTI_SELECT}">
    <select class="inputWidget multiselect form-control ${classes }" multiple data-input-widget-id="${inputWidgetInstance.id }" 
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>>
        <g:each in="${inputWidgetInstance.widgetKeys.tokenize(',')}" status="index" var="key">
            <option value="${key }" ${key in inputWidgetValue?.tokenize(',')*.trim() ? 'selected' : '' }>
                ${inputWidgetInstance.widgetValues.tokenize(',')[index]?.trim()}
            </option>
        </g:each>
    </select>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.TEXT_AREA}">
    <textArea name="textArea" class="inputWidget form-control ${classes } textAreaWidget" 
        data-input-widget-id="${inputWidgetInstance.id }"
        minlenght="${inputWidgetInstance.minChar ?: ''}" maxlenght="${inputWidgetInstance.maxChar ?: ''}"
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>>${inputWidgetValue}</textArea>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.TEXT_FIELD}">
    <input type="text" class="inputWidget form-control ${classes } text" 
        data-input-widget-id="${inputWidgetInstance.id }" value="${inputWidgetValue}"
        minlenght="${inputWidgetInstance.minChar ?: ''}" maxlenght="${inputWidgetInstance.maxChar ?: ''}"
        min="${inputWidgetInstance.minValueRange}" max="${inputWidgetInstance.maxValueRange}"
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>/>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.CHECKBOX}">
     <g:each in="${inputWidgetInstance.defaultValue.tokenize(',')}" status="index" var="value">
        <div class="row">
            <input type="checkbox" name="${inputWidgetInstance.label}" class="inputWidget col-sm-1 checkbox ${classes }" 
                data-input-widget-id="${inputWidgetInstance.id }" value="${value}" style="min-height: 15px;"
                <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}" />
                ${value in inputWidgetValue?.tokenize(',')*.trim() ? 'checked' : '' }/>
            <label class="control-label col-lg-2" for="defaultValue">${value}</label>
        </div>
    </g:each>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.RADIO}">
     <g:each in="${inputWidgetInstance.defaultValue.tokenize(',')}" status="index" var="value">
        <div class="row">
            <input type="radio" name="${inputWidgetInstance.label}" class="inputWidget col-sm-1 radio ${classes }" 
                data-input-widget-id="${inputWidgetInstance.id }" value="${value}" style="min-height: 15px;"
                <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>
                ${value == inputWidgetValue ? 'checked' : '' }/>
            <label class="control-label col-lg-2" for="defaultValue">${value}</label>
            </div>
    </g:each>
</g:if>

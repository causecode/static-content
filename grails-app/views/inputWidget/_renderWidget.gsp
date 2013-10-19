<%@ page import="com.cc.content.inputWidget.InputWidgetType"%>

<g:set var="inputWidgetType" value="${inputWidgetInstance.type}"></g:set>
<g:set var="additionalAttrs" value="${additionalAttrs }" />

<g:if test="${inputWidgetType == InputWidgetType.SELECT}">
    <select class="select form-control ${classes }" data-input-widget-id="${inputWidgetInstance.id }" 
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>
        <content:widgetValidation inputWidgetInstance="${inputWidgetInstance}"/>>
        <g:each in="${inputWidgetInstance.widgetKeys?.tokenize(',')}" status="index" var="key">
            <option value="${key}" ${key == inputWidgetValue ? 'selected' : '' } >
                ${inputWidgetInstance.widgetValues?.tokenize(',')[index]?.trim()}
            </option>
        </g:each>
    </select>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.MULTI_SELECT}">
    <select class="form-control ${classes } multiselect" multiple data-input-widget-id="${inputWidgetInstance.id }" 
        name="${inputWidgetInstance.name ?: 'select' }${inputWidgetInstance.id}"
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>
        <content:widgetValidation inputWidgetInstance="${inputWidgetInstance}"/>>
        <g:each in="${inputWidgetInstance.widgetKeys?.tokenize(',')}" status="index" var="key">
            <option value="${key }" ${key in inputWidgetValue?.tokenize(',')*.trim() ? 'selected' : '' }>
                ${inputWidgetInstance.widgetValues?.tokenize(',')[index]?.trim()}
            </option>
        </g:each>
    </select>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.TEXT_AREA}">
    <textArea name="${inputWidgetInstance.name ?: 'textArea' }${inputWidgetInstance.id}" class="form-control ${classes }" 
        data-input-widget-id="${inputWidgetInstance.id }"
        minlenght="${inputWidgetInstance.minChar ?: ''}" maxlenght="${inputWidgetInstance.maxChar ?: ''}"
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>
        <content:widgetValidation inputWidgetInstance="${inputWidgetInstance}"/>>${inputWidgetValue}</textArea>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.TEXT_FIELD}">
    <input type="text" class="form-control ${classes }" 
        data-input-widget-id="${inputWidgetInstance.id }" value="${inputWidgetValue}"
        minlenght="${inputWidgetInstance.minChar ?: ''}" maxlenght="${inputWidgetInstance.maxChar ?: ''}"
        min="${inputWidgetInstance.minValueRange}" max="${inputWidgetInstance.maxValueRange}"
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>
        <content:widgetValidation inputWidgetInstance="${inputWidgetInstance}"/>/>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.CHECKBOX}">
    <g:each in="${inputWidgetInstance.widgetKeys?.tokenize(',')}" status="index" var="key">
        <div class="checkbox">
            <label>
                <input type="checkbox" name="${inputWidgetInstance.name ?: 'checkbox' }${inputWidgetInstance.id}" 
                    class="${classes } " 
                    data-input-widget-id="${inputWidgetInstance.id }" 
                    value="${key}"
                    <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}" />
                    <content:widgetValidation inputWidgetInstance="${inputWidgetInstance}"/>
                    ${key in inputWidgetValue?.tokenize(',')*.trim() ? 'checked' : '' }/>
                <strong>${inputWidgetInstance.widgetValues?.tokenize(',')[index]?.trim() }</strong>
            </label>
        </div>
    </g:each>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.RADIO}">
    <g:each in="${inputWidgetInstance.widgetKeys?.tokenize(',')}" status="index" var="key">
        <div class="radio">
            <label>
                <input type="radio" name="${inputWidgetInstance.name ?: 'radio' }${inputWidgetInstance.id}" 
                    class="${classes }" 
                    data-input-widget-id="${inputWidgetInstance.id }" 
                    value="${key}"
                    <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}" />
                    <content:widgetValidation inputWidgetInstance="${inputWidgetInstance}"/>
                    ${key == inputWidgetValue ? 'checked' : '' }/>
                <strong>${inputWidgetInstance.widgetValues?.tokenize(',')[index]?.trim() }</strong>
            </label>
        </div>
    </g:each>
</g:if>
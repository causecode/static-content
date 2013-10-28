<%@ page import="com.cc.content.inputWidget.InputWidgetType"%>

<g:set var="inputWidgetType" value="${inputWidgetInstance.type}"></g:set>
<g:set var="additionalAttrs" value="${additionalAttrs }" />
<g:set var="fieldValue" value="${inputWidgetValue ?: (inputWidgetInstance?.defaultValue ?: '') }" />

<g:if test="${inputWidgetType == InputWidgetType.SELECT}">
    <select class="select form-control ${classes }" data-input-widget-id="${inputWidgetInstance.id }"
        name="${inputWidgetInstance.name ?: 'select' }${inputWidgetInstance.id}"
        id="${inputWidgetInstance.name ?: 'select' }${inputWidgetInstance.id}"  
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>
        <content:widgetValidation inputWidgetInstance="${inputWidgetInstance}"/>>
        <g:if test="${inputWidgetInstance.noSelectionText }">
            <option value="">${inputWidgetInstance.noSelectionText }</option>
        </g:if>
        <g:each in="${inputWidgetInstance.widgetKeys?.tokenize(',')*.trim()}" status="index" var="key">
            <option value="${key}" ${key == fieldValue ? 'selected' : '' } >
                ${inputWidgetInstance.widgetValues?.tokenize(',')[index]?.trim()}
            </option>
        </g:each>
    </select>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.MULTI_SELECT}">
    <select class="form-control ${classes } multiselect" multiple data-input-widget-id="${inputWidgetInstance.id }" 
        name="${inputWidgetInstance.name ?: 'select' }${inputWidgetInstance.id}"
        id="${inputWidgetInstance.name ?: 'select' }${inputWidgetInstance.id}"
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>
        <content:widgetValidation inputWidgetInstance="${inputWidgetInstance}"/>>
        <g:if test="${inputWidgetInstance.noSelectionText }">
            <option value="">${inputWidgetInstance.noSelectionText }</option>
        </g:if>
        <g:each in="${inputWidgetInstance.widgetKeys?.tokenize(',')*.trim()}" status="index" var="key">
            <option value="${key }" ${key in fieldValue?.tokenize(',')*.trim() ? 'selected' : '' }>
                ${inputWidgetInstance.widgetValues?.tokenize(',')[index]?.trim()}
            </option>
        </g:each>
    </select>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.TEXT_AREA}">
    <textArea name="${inputWidgetInstance.name ?: 'textArea' }${inputWidgetInstance.id}"
        id="${inputWidgetInstance.name ?: 'textArea' }${inputWidgetInstance.id}"
        data-input-widget-id="${inputWidgetInstance.id }" class="form-control ${classes }"
        style="${inputWidgetInstance.maxChar && inputWidgetInstance.maxChar > 400 ? 'min-height: 225px' : ''}"
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>
        <content:widgetValidation inputWidgetInstance="${inputWidgetInstance}"/>>${fieldValue}</textArea>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.TEXT_FIELD}">
    <input type="text" class="form-control ${classes }"
        name="${inputWidgetInstance.name ?: 'textfield' }${inputWidgetInstance.id}"
        id="${inputWidgetInstance.name ?: 'textfield' }${inputWidgetInstance.id}"
        data-input-widget-id="${inputWidgetInstance.id }" value="${fieldValue}"
        <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}"/>
        <content:widgetValidation inputWidgetInstance="${inputWidgetInstance}"/>/>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.CHECKBOX}">
    <g:each in="${inputWidgetInstance.widgetKeys?.tokenize(',')}" status="index" var="key">
        <g:if test="${index == 1}">
            <%
                /*
                 * Needs to do this because if same class is added to same name of checkbox, than
                 * jquery selector of type $('.inputWidget.having-default-value') will return all
                 * records which will cause some kind of problems. Like firing some event for this
                 * will fired 3 times of 3 same checkboxes.
                 */
             %>
            <g:set var="classes" value="${classes?.replace('having-default-value', '') }" />
        </g:if>
        <div class="checkbox">
            <label>
                <input type="checkbox" name="${inputWidgetInstance.name ?: 'checkbox' }${inputWidgetInstance.id}" 
                    class="${classes }" data-input-widget-id="${inputWidgetInstance.id }" value="${key}"
                    <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}" />
                    <content:widgetValidation inputWidgetInstance="${inputWidgetInstance}"/>
                    ${key in fieldValue?.tokenize(',')*.trim() ? 'checked' : '' }/>
                <strong>${inputWidgetInstance.widgetValues?.tokenize(',')[index]?.trim() }</strong>
            </label>
        </div>
    </g:each>
</g:if>

<g:if test="${inputWidgetType == InputWidgetType.RADIO}">
    <g:each in="${inputWidgetInstance.widgetKeys?.tokenize(',')}" status="index" var="key">
        <g:if test="${index == 1}">
            <g:set var="classes" value="${classes?.replace('having-default-value', '') }" />
        </g:if>
        <div class="radio">
            <label>
                <input type="radio" name="${inputWidgetInstance.name ?: 'radio' }${inputWidgetInstance.id}" 
                    class="${classes }" 
                    data-input-widget-id="${inputWidgetInstance.id }" 
                    value="${key}"
                    <content:widgetHelper inputWidgetInstance="${inputWidgetInstance}" />
                    <content:widgetValidation inputWidgetInstance="${inputWidgetInstance}"/>
                    ${key == fieldValue ? 'checked' : '' }/>
                <strong>${inputWidgetInstance.widgetValues?.tokenize(',')[index]?.trim() }</strong>
            </label>
        </div>
    </g:each>
</g:if>
<g:set var="inputWidgetType" value="${inputWidgetInstance.name}"></g:set>
<g:if test="${inputWidgetType.equals('select')}">
    <div class="col-lg-4">
        <g:select optionKey="id" optionValue="${{inputWidgetInstance.widgetValue?.toUpperCase()}}"
            class="inputWidget form-control" name="select" from="${inputWidgetValue}"
            id="${questionInstance?.id}" data-input-widget-id="${inputWidgetInstance.id }" />
    </div>
</g:if>

<g:if test="${inputWidgetType.equals('textArea')}">
    <div class="col-lg-4">
        <g:textArea name="textArea" id="${questionInstance?.id}" class="inputWidget form-control"
            data-input-widget-id="${inputWidgetInstance.id }" value="${inputWidgetValue}"></g:textArea>
    </div>
</g:if>

<g:if test="${inputWidgetType.equals('textField')}">
    <div class="col-lg-4">
        <g:textField name="text" class="inputWidget form-control" value="${inputWidgetValue}"
            id="${questionInstance?.id}" data-input-widget-id="${inputWidgetInstance.id }" />
    </div>
</g:if>
<g:set var="inputWidgetType" value="${inputWidgetInstance.name}"></g:set>
<g:if test="${inputWidgetType.equals('select')}">
        <g:select optionKey="id" optionValue="${{inputWidgetInstance.widgetValue?.toUpperCase()}}"
            class="inputWidget form-control" name="select" from="${inputWidgetValue}"
            data-input-widget-id="${inputWidgetInstance.id }" />
</g:if>

<g:if test="${inputWidgetType.equals('textArea')}">
        <g:textArea name="textArea" class="inputWidget form-control"
            data-input-widget-id="${inputWidgetInstance.id }" value="${inputWidgetValue}"></g:textArea>
</g:if>

<g:if test="${inputWidgetType.equals('textField')}">
        <g:textField name="text" class="inputWidget form-control" value="${inputWidgetValue}"
            data-input-widget-id="${inputWidgetInstance.id }" />
</g:if>
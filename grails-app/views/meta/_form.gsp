<%@ page import="com.cc.content.meta.Meta"%>

<div class="form-group">
    <label class="control-label col-sm-2" for="type">
    </label>
    <div class="col-sm-5">
        <g:select name="meta.type" from="${Meta.getTypeList() }" value="${metaInstance?.type }"
            noSelection="['': 'Select Meta Tag']" class="form-control" />
        <br>
        <g:textArea name="meta.value" class="form-control">${metaInstance?.value }</g:textArea>
    </div>
</div>
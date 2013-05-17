<%@ page import="com.cc.content.meta.Meta" %>

<div class="control-group">
    <label class="control-label" for="type">
        <g:remoteLink controller="meta" action="deleteMeta" id="${metaInstance?.id }" 
            before="\$this = \$(this); var c=confirm('Are you sure');if(!c) return false"
            onSuccess="\$this.parents('.control-group').remove()">
            <i class="icon-remove">
            </i>
        </g:remoteLink>
    </label>
    <div class="controls">
        <g:select name="meta.type" from="${Meta.getTypeList() }" value="${metaInstance?.type }"
            noSelection="['': 'Select Meta Tag']"/><br>
        <g:textArea name="meta.value" >${metaInstance?.value }</g:textArea>
    </div>
</div>
<%@ page import="com.cc.content.meta.Meta"%>

<div class="form-group">
    <label class="control-label col-sm-2" for="type"> <g:remoteLink controller="meta" action="deleteMeta"
            id="${metaInstance?.id }" before="\$this = \$(this); var c=confirm('Are you sure');if(!c) return false"
            onSuccess="\$this.parents('.form-group').remove()">
            <span><i class="icon-remove"></i></span>
        </g:remoteLink>
    </label>
    <div class="col-sm-5">
        <g:select name="meta.type" from="${Meta.getTypeList() }" value="${metaInstance?.type }"
            noSelection="['': 'Select Meta Tag']" class="form-control" />
        <br>
        <g:textArea name="meta.value" class="form-control">
            ${metaInstance?.value }
        </g:textArea>
    </div>
</div>
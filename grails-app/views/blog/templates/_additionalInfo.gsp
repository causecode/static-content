<small class="text-muted">
    <i class="icon-user" title="Posted By"></i>&nbsp;
    <g:if test="${authorName }">
        ${authorName }
    </g:if>
    <g:else>
        <content:resolveAuthor id="${id }" />
    </g:else>
    <g:if test="${publishedDate }">
        &nbsp;&nbsp;|&nbsp;&nbsp;
        <i class="icon-time" title="Posted On"></i>&nbsp;
        ${publishedDate.format('dd MMM yyyy')}
    </g:if>
</small>
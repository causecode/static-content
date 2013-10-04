<small class="text-muted">
    <i class="icon-user" title="Posted By"></i>&nbsp;
    <g:if test="${authorName }">
        ${authorName }
    </g:if>
    <g:else>
        <content:resolveAuthor id="${id }" />
    </g:else>
    &nbsp;&nbsp;|&nbsp;&nbsp;
    <i class="icon-time" title="Posted On"></i>&nbsp;
    ${dateCreated.format('dd MMM yyyy')}
</small>
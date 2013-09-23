<div class="media-body">
    <h4 class="media-heading inline">${commentInstance.subject}&nbsp;</h4>
    <content:canEdit>
        <g:link controller="comment" action="delete" id="${commentInstance.id }"  class="clear-hover"><i class="icon-trash"></i></g:link>
    </content:canEdit>
    <div class="comment-info">
        <g:render template="/blog/templates/additionalInfo" model="[authorName: commentInstance.name,
            dateCreated: commentInstance.dateCreated]" />
    </div>
    <p class="comment-text">
        ${commentInstance.commentText}
        <a href="#comment-overlay" class="btn btn-default btn-xs commentButton" data-toggle="modal"
            data-comment-id="${commentInstance.id}"><i class="icon-reply"></i> Reply</a>
    </p>
    <content:nestedComment commentInstance="${commentInstance}" />
</div>
<div class="comment" style="margin-bottom: 20px">
    <h4><strong>${commentInstance.subject}</strong></h4>
    <div>
        <strong>By:</strong>
        <small>
            ${commentInstance.name }
        </small>
        &nbsp;&nbsp;|&nbsp;&nbsp;
        <strong>Posted on:</strong>
        <small>
            ${commentInstance.dateCreated.format("dd-MM-yyyy")}
        </small>
    </div>
    <br>
    ${commentInstance.commentText}
    <a href="#comment-overlay" class="btn btn-default btn-xs commentButton" data-toggle="modal"
        data-comment-id="${commentInstance.id}"><i class="icon-reply"></i> Reply</a>
    <content:nestedComment commentInstance="${commentInstance}" />
</div>
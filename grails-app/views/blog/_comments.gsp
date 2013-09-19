<div class='comment' data-comment='${it.id}' style="margin-bottom:20px">
    <b>
        ${it.subject}
    </b> <br>
    ${it.name}
    | Date :
    <g:formatDate date="${it.dateCreated}" format="MM-dd-yyyy"></g:formatDate>
    <br>
    ${it.commentText}<br> <a href='#commentModal' role='button' class='btn btn-default btn-xs commentButton'
        data-toggle='modal'>Reply</a>
    <com:nestedComment commentId="${it.id}"></com:nestedComment>
</div>
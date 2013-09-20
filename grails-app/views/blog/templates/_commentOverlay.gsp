<div id="comment-overlay" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title">Comment</h4>
            </div>
            <g:form name="comment-modal-form" class="form-horizontal jquery-form" action="comment">
                <fieldset form="comment-modal-form">
                    <div class="modal-body">
                        <div class="form-group">
                            <label class="control-label col-sm-3">Subject </label>
                            <div class="col-sm-7">
                                <g:textField name="subject" class="form-control" required="" autofocus="autofocus" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3">Name </label>
                            <div class="col-sm-7">
                                <g:textField name="name" class="form-control" required="" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3">Email </label>
                            <div class="col-sm-7">
                                <g:textField name="email" class="form-control" required="" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3">Comment </label>
                            <div class="col-sm-7">
                                <g:textArea name="commentText" class="form-control" required="" />
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <g:submitButton class="btn btn-primary" name="Comment" />
                        <button class="btn btn-default" data-dismiss="modal" aria-hidden="true">Close</button>
                        <g:hiddenField name="id" value="${blogInstance.id}" />
                        <g:hiddenField name="commentId" />
                    </div>
                </fieldset>
            </g:form>
        </div>
    </div>
</div>
<script type="text/javascript">
    $("a.commentButton").on("click", function() { 
        $("input[type=hidden][name=commentId]").val($(this).data('comment-id'));
    });
</script>
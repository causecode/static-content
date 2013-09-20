<div id="editMenuItemModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="edit-menu-item-title"
    aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 class="modal-title" id="edit-menu-item-title">Edit Menu Item</h3>
            </div>
            <g:form name="edit-menu-item-form" class="form-horizontal block-error jquery-form">
                <fieldset form="edit-menu-item-form">
                    <div class="modal-body">
                        <div class="alert hide fade in" id="alert-lead">
                            <button type="button" class="close" onclick="$(this).parent().fadeOut()">&times;</button>
                            <p></p>
                        </div>
                        <g:render template="basicForm"></g:render>
                    </div>
                    <div class="modal-footer">
                        <a id="updateMenuItem" class="btn btn-primary" data-dismiss="modal">Update</a>
                        <a id="deleteMenuItem" class="btn btn-danger" data-dismiss="modal"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');">Delete</a>
                    </div>
                </fieldset>
            </g:form>
        </div>
    </div>
</div>
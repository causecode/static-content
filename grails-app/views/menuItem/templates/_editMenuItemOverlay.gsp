<div id="edit-menu-item-overlay" class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 class="modal-title" id="edit-menu-item-title">Edit Menu Item</h3>
            </div>
            <g:form name="edit-menu-item-form" class="form-horizontal jquery-form">
                <fieldset form="edit-menu-item-form">
                    <div class="modal-body">
                        <div class="alert hide fade in" id="alert-lead">
                            <button type="button" class="close" onclick="$(this).parent().fadeOut()">&times;</button>
                            <p></p>
                        </div>
                        <g:render template="/menuItem/templates/form" />
                    </div>
                    <div class="modal-footer">
                        <a id="update" class="btn btn-primary" data-dismiss="modal">Update</a>
                        <a href="#" class="btn btn-default" data-dismiss="modal">Cancel</a>
                    </div>
                </fieldset>
            </g:form>
        </div>
    </div>
</div>
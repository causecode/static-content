<div id="create-menu-item-overlay" class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="create-menu-item-title">Create Menu Item</h4>
            </div>
            <g:form name="create-menu-item-form" class="form-horizontal jquery-form">
                <fieldset form="create-menu-item-form">
                    <div class="modal-body">
                        <div class="alert hide fade in" id="alert-lead">
                            <button type="button" class="close" onclick="$(this).parent().fadeOut()">&times;</button>
                            <p></p>
                        </div>
                        <g:render template="/menuItem/templates/form" />
                    </div>
                    <div class="modal-footer">
                        <a id="create" href="#" class="btn btn-primary" data-dismiss="modal">Create</a>
                        <a href="#" class="btn btn-default" data-dismiss="modal">Cancel</a>
                    </div>
                </fieldset>
            </g:form>
        </div>
    </div>
</div>
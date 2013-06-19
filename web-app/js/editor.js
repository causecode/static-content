$.noConflict();
function editorCheck() {
    var selectedValue = jQuery("#drop-down select").val();
    if (jQuery("#drop-down select").val() == '2') {
        CKEDITOR.instances = {}
        switchEditor('true');
        jQuery("#editor a").remove();
    } else {
        switchEditor('false');
    }
}
function switchEditor(flag) {
    jQuery.ajax({
        type : 'POST',
        data : {
            'flag' : flag
        },
        url : '/page/editorSwitch',
        success : function(data, textStatus) {
            jQuery('#editor').html(data);
            CKEDITOR.instances = {}
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
        }
    });
    return false;
}
jQuery(function() {
    editorCheck();
    jQuery("#drop-down select").change(function() {
        editorCheck();
    });
});
function editorCheck() {
    selectedValue = $("#drop-down select").val();
    if (noEditorIdList.indexOf(parseInt(selectedValue)) != -1) {
        switchEditor(false);
    } else {
        switchEditor(true);
    }
}
function switchEditor(editorFlag, switchEditorFlag) {
    $.ajax({
        type : 'POST',
        data : {
            'editorFlag' : editorFlag,
            'switcheditorFlag' : switchEditorFlag,
            'id': $('input#id').val()
        },
        url : '/page/editorSwitch',
        beforeSend : function() {
            selectedValue = $("#drop-down select").val();
            if ((editorFlag=='true'? true : false) && (noEditorIdList.indexOf(parseInt(selectedValue)) != -1)) {
                switchEditor('false');
                return 0;
            }
            CKEDITOR.instances = {}
        },
        success : function(data, textStatus) {
            $('#editor').html(data);
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
        }
    });
    return false;
}
$(function() {
    $("#drop-down select").change(function() {
        editorCheck();
    });
});
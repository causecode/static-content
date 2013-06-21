function editorCheck() {
    selectedValue = $("#drop-down select").val();
    if (noEditorIdList.indexOf(parseInt(selectedValue)) != -1) {
        switchEditor(false);
    } else {
        switchEditor(true);
    }
}
function switchEditor(editorFlag) {
    $.ajax({
        type : 'POST',
        data : {
            'editorFlag': editorFlag,
            'id': $('input#id').val(),
            'textInstanceId': $("#drop-down select").val()
        },
        url : '/page/editorSwitch',
        beforeSend : function() {
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
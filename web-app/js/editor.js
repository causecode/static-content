var editorBody;
function editorCheck() {
    selectedValue = $("#drop-down select").val();
    if (noEditorIdList.indexOf(parseInt(selectedValue)) != -1) {
        switchEditor(false);
    } else {
        switchEditor(true);
    }
}
function switchEditor(useEditor) {
    $.ajax({
        type : 'POST',
        data : {
            'useEditor': useEditor,
            'id': (useEditor ? null : $('input#id').val()),
            'textInstanceId': $("#drop-down select").val()
        },
        url : '/page/editorSwitch',
        beforeSend : function() {
            editorBody = useEditor ? $("#editor textarea").val() : CKEDITOR.instances['body'].getData();
            CKEDITOR.instances = {}
        },
        success : function(data, textStatus) {
            $('#editor').html(data);
        },
        complete : function(jqXHR, textStatus) {
            if (useEditor) {
                CKEDITOR.instances['body'].setData(editorBody);
            } else {
                $("#editor textarea").val(editorBody);
            }
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
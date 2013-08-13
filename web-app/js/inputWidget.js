$("select#inputWidgetName").change(function(){
    var str = "";
    $( "select#inputWidgetName option:selected" ).each(function() {
        str += $( this ).text() + " ";
      });
    if(str == 'select ') {
        $(".inputWidgetType").hide();
        $("select#inputWidgetSelect").show();
        $("select#inputWidgetSelect").data('selected','true');
        $("div#widgetKey").show();
        $("div#widgetValue").show();
        
    } else if(str == 'checkBox ') {
        //$(".inputWidgetType").hide();
        $("input#inputWidgetCheckbox").show();
        $("div#widgetKey").hide();
        $("div#widgetValue").hide();
        $("input#inputWidgetCheckbox").data('selected','true');
        $("input[type='text']#defaultValue").change(function(){
            if($( this ).val())
            $( "input#inputWidgetCheckbox" ).click();
        });
    } else if(str == 'textField ') {
        $(".inputWidgetType").hide();
        $("input#inputWidgetTextfield").show();
        $("input#inputWidgetTextfield").data('selected','true');
        $("div#widgetKey").hide();
        $("div#widgetValue").hide();
        $("input[type='text']#defaultValue").change(function(){
            $( "input#inputWidgetTextfield" ).val( $( this ).val() );
        });
    } else if(str == 'textArea ') {
        $(".inputWidgetType").hide();
        $("textarea#inputWidgetTextarea").show();
        $("textarea#inputWidgetTextarea").data('selected','true');
        $("div#widgetKey").hide();
        $("div#widgetValue").hide();
        $("input[type='text']#defaultValue").change(function(){
            $( "textarea#inputWidgetTextarea" ).text( $( this ).val() );
        });
    }
});

$("input[type='text']#label").change(function(){
    var str = "";
    str = $( this ).val() + "  :  ";
    $( "td#label" ).text( str );
});
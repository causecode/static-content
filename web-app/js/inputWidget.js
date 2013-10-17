/*
 * JS Used to show and hide Input Widget fields on widgetTypeSelection
 * while creating Input Widget.
 */
$("#type.inputWidgetSelector").on("change", function(){
    $("div.minChar.inputWidgetSelector").addClass("hide");
    $("div.maxChar.inputWidgetSelector").addClass("hide");
    $("div.minValueRange.inputWidgetSelector").addClass("hide");
    $("div.maxValueRange.inputWidgetSelector").addClass("hide");
    $("div.widgetKeyValue.inputWidgetSelector").addClass("hide");
    $("div.noSelected.inputWidgetSelector").addClass("hide");
    $("div.defaultSelected.inputWidgetSelector").addClass("hide");

    var $type = $(this).val()
    if($type == 'SELECT' || $type == 'MULTI_SELECT') {
        $("div.widgetKeyValue.inputWidgetSelector").removeClass("hide");
        $("div.noSelected.inputWidgetSelector").removeClass("hide");
        $("div.defaultSelected.inputWidgetSelector").removeClass("hide");
    }
    if($type == 'CHECKBOX' || $type == 'RADIO') {
        $("div.defaultSelected.inputWidgetSelector").removeClass("hide");
        $("div.defaultValue.inputWidgetSelector").removeClass("hide");
    } 
    if($type == 'TEXT_AREA') {
        $("div.minChar.inputWidgetSelector").removeClass("hide");
        $("div.maxChar.inputWidgetSelector").removeClass("hide");
    }
    if($type == 'TEXT_FIELD') {
        $("div.minChar.inputWidgetSelector").removeClass("hide");
        $("div.maxChar.inputWidgetSelector").removeClass("hide");
        $("div.minValueRange.inputWidgetSelector").removeClass("hide");
        $("div.maxValueRange.inputWidgetSelector").removeClass("hide");
    }
});
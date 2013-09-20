/*
 * JS Used to show and hide Input Widget fields on widgetTypeSelection
 * while creating Input Widget.
 */
$("#type").on("change", function(){
    $("div.minChar").addClass("hide");
    $("div.maxChar").addClass("hide");
    $("div.minValueRange").addClass("hide");
    $("div.maxValueRange").addClass("hide");
    $("div.widgetKeyValue").addClass("hide");
    $("div.noSelected").addClass("hide");
    $("div.defaultSelected").addClass("hide");

    var $type = $(this).val()
    if($type == 'SELECT' || $type == 'MULTI_SELECT') {
        $("div.widgetKeyValue").removeClass("hide");
        $("div.noSelected").removeClass("hide");
        $("div.defaultSelected").removeClass("hide");
    }
    if($type == 'CHECKBOX' || $type == 'RADIO') {
        $("div.defaultSelected").removeClass("hide");
        $("div.validation").addClass("hide");
    } 
    if($type == 'TEXT_AREA') {
        $("div.minChar").removeClass("hide");
        $("div.maxChar").removeClass("hide");
    }
    if($type == 'TEXT_FIELD') {
        $("div.minChar").removeClass("hide");
        $("div.maxChar").removeClass("hide");
        $("div.minValueRange").removeClass("hide");
        $("div.maxValueRange").removeClass("hide");
    }
});
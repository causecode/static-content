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
    }
    if($type == 'CHECKBOX' || $type == 'SELECT' || $type == 'MULTI_SELECT' || $type == 'RADIO') {
        $("div.defaultSelected").removeClass("hide");
    } 
    if($type == 'TEXT_AREA' || $type == 'TEXT_FIELD') {
        $("div.minChar").removeClass("hide");
        $("div.maxChar").removeClass("hide");
        $("div.minValueRange").removeClass("hide");
        $("div.maxValueRange").removeClass("hide");
    }
});
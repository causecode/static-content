/**
 * JS Used to show and hide Input Widget fields on widgetTypeSelection
 * while creating Input Widget.
 */

$("select.input-widget-type-selector").on("change", function(){
    var $parent = $(this).parents('div.widget-container');
    $("div.minChar", $parent).addClass("hide");
    $("div.maxChar", $parent).addClass("hide");
    $("div.minValueRange", $parent).addClass("hide");
    $("div.maxValueRange", $parent).addClass("hide");
    $("div.widgetKeyValue", $parent).addClass("hide");
    $("div.noSelectionText", $parent).addClass("hide");

    var $type = $(this).val()
    if($type == 'SELECT' || $type == 'MULTI_SELECT') {
        $("div.widgetKeyValue", $parent).removeClass("hide");
        $("div.noSelectionText", $parent).removeClass("hide");
    }
    if($type == 'CHECKBOX' || $type == 'RADIO') {
        $("div.widgetKeyValue", $parent).removeClass("hide");
    }
    if($type == 'TEXT_AREA') {
        $("div.minChar", $parent).removeClass("hide");
        $("div.maxChar", $parent).removeClass("hide");
    }
    if($type == 'TEXT_FIELD') {
        $("div.minChar", $parent).removeClass("hide");
        $("div.maxChar", $parent).removeClass("hide");
        $("div.minValueRange", $parent).removeClass("hide");
        $("div.maxValueRange", $parent).removeClass("hide");
    }
});

$("select.input-widget-type-selector").trigger("change");
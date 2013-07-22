//JS For Scrolling

$(document).ready(function() {
    var App = window.App || {}
    App.lock = false;
    $(window).scroll(function() {
        if($("#feedItemInstanceId").length == 0)
            return false;
        if($("#feedItemInstanceId").offset().top < $(window).scrollTop() + $(window).outerHeight()) {
            if(App.lock) {
                return false;
            }
            App.lock = true;
            $("#feedItemInstanceId").addClass("waiting-icon");
            jQuery.ajax ({
                type: 'POST',
                data:{'max':App.activity.params.max,'offset':App.activity.params.offset},
                url: '/activityFeedItem/show/',
                success: function(data) {
                    if(!data.resultData) {
                        App.lock = true;
                        $("#feedItemInstanceId").text('No more activities');
                        setTimeout(function(){
                            $("#feedItemInstanceId").fadeOut()
                        }, 5000);
                    } else {
                        App.lock = false;
                        $('#feedItems').append(data.resultData);
                        App.activity.params.offset = App.activity.params.offset + 10;
                    }
                    $("#feedItemInstanceId").removeClass("waiting-icon");
                }
            });
        } else {
            
        }
    })
});

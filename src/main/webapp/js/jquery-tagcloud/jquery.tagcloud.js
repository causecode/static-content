(function($) {

    $.fn.tagcloud = function(options) {
        var opts = $.extend({}, $.fn.tagcloud.defaults, options);
        tagWeights = this.map(function(){
            return $(this).attr("rel");
        })
        tagWeights = jQuery.makeArray(tagWeights).sort(compareWeights);
        lowest = tagWeights[0];
        highest = tagWeights.pop();
        range = highest - lowest;
        // Sizes
        if (opts.size) {
            fontIncr = (opts.size.end - opts.size.start)/range;
        }
        // Colors
        if (opts.color) {
            colorIncr = colorIncrement (opts.color, range);
        }
        return this.each(function() {
            weighting = $(this).attr("rel") - lowest;
            if (opts.size) {
                $(this).css({"font-size": opts.size.start + (weighting * fontIncr) + opts.size.unit});        
            }
            if (opts.color) {
                $(this).css({"color": tagColor(opts.color, colorIncr, weighting)});
            }
        })
    }

    $.fn.tagcloud.defaults = {
            size: {start: 14, end: 18, unit: "pt"}, 
            color: {start: '#cde', end: '#f52'}
    };

    // Converts hex to an RGB array
    function toRGB (code) {
        if (code.length == 4) {
            code = jQuery.map(/\w+/.exec(code), function(el) {return el + el }).join("");
        }
        hex = /(\w{2})(\w{2})(\w{2})/.exec(code);
        return [parseInt("0x" + hex[1]), parseInt("0x" + hex[2]), parseInt("0x" + hex[3])];
    }

    // Converts an RGB array to hex
    function toHex (ary) {
        return "#" + jQuery.map(ary, function(int) {
            hex =  int.toString(16);
            hex = (hex.length == 1) ? "0" + hex : hex;
            return hex;
        }).join("");
    }

    function colorIncrement (color, range) {
        return jQuery.map(toRGB(color.end), function(int, i) {
            return (int - toRGB(color.start)[i])/range;
        });
    }

    function tagColor (color, increment, weighting) {
        rgb = jQuery.map(toRGB(color.start), function(int, i) {
            ref = Math.round(int + (increment[i] * weighting));
            if (ref > 255) {
                ref = 255;
            } else {
                if (ref < 0) {
                    ref = 0;
                }
            }
            return ref;
        });
        return toHex(rgb);
    }

    function compareWeights(a, b)
    {
        return a - b;
    }


})(jQuery);
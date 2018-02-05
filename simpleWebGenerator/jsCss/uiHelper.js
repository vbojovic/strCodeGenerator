var UIHelper = (function () {
    function UIHelper() {}

    UIHelper.prototype.checkAllRegister = function (containerId, clickingItemId) {
        $('#' + clickingItemId).click(function () {
            $('#' + containerId).find('input:checkbox').prop('checked', true);
        });
    };
    UIHelper.prototype.unCheckAllRegister = function (containerId, clickingItemId) {
        $('#' + clickingItemId).click(function () {
            $('#' + containerId).find('input:checkbox').prop('checked', false);
        });
    };
    UIHelper.prototype.invertCheckboxesRegister = function (containerId, clickingItemId) {
        $('#' + clickingItemId).click(function () {
            $('#' + containerId).find('input:checkbox').each(function () {
                $(this).prop('checked', (!$(this).prop('checked')));
            });
        });
    };
    UIHelper.prototype.getCheckedCheckboxTexts = function (containerId) {
        var texts = new Array();
        $('#' + containerId).find('input:checkbox').each(function () {
            if ($(this).prop('checked')) {
                texts.push($(this).val());
            }
        });
        return texts;
    };

    UIHelper.prototype.getTextItems = function (containerSelector) {
        var texts = new Array();
        $('#' + containerId).find('input:text').each(function () {
                texts.push($(this).val());
        });
        return texts;
    };
    /*
     UIHelper.prototype.GetPRopertiesWithin=function(containerId,propertyName){
     var propList = new Array();
     $('#'+containerId).each(function(){
     if ($( this ).prop(propertyName) != null) {
     
     }
     });
     return propList;
     };
     */
    
    /**
     * 
     * @param {type} rangeElement
     * @param {type} timeFromLabel
     * @param {type} timeToLabel
     * @returns {undefined}
     */
    UIHelper.prototype.initTimeRange = function (rangeElement,timeFromLabel,timeToLabel) {
        $(rangeElement).slider({
            range: true,
            min: 0,
            max: 1440,
            step: 15,
            values: [540, 1020],
            slide: function (e, ui) {
                var hours1 = Math.floor(ui.values[0] / 60);
                var minutes1 = ui.values[0] - (hours1 * 60);

                if (hours1.length == 1)
                    hours1 = '0' + hours1;
                if (minutes1.length == 1)
                    minutes1 = '0' + minutes1;
                if (minutes1 == 0)
                    minutes1 = '00';
                if (hours1 >= 12) {
                    if (hours1 == 12) {
                        hours1 = hours1;
                        minutes1 = minutes1 + " PM";
                    } else {
                        hours1 = hours1 - 12;
                        minutes1 = minutes1 + " PM";
                    }
                } else {
                    hours1 = hours1;
                    minutes1 = minutes1 + " AM";
                }
                if (hours1 == 0) {
                    hours1 = 12;
                    minutes1 = minutes1;
                }


                if (timeFromLabel)
                    $(timeFromLabel).html(hours1 + ':' + minutes1);

                var hours2 = Math.floor(ui.values[1] / 60);
                var minutes2 = ui.values[1] - (hours2 * 60);

                if (hours2.length == 1)
                    hours2 = '0' + hours2;
                if (minutes2.length == 1)
                    minutes2 = '0' + minutes2;
                if (minutes2 == 0)
                    minutes2 = '00';
                if (hours2 >= 12) {
                    if (hours2 == 12) {
                        hours2 = hours2;
                        minutes2 = minutes2 + " PM";
                    } else if (hours2 == 24) {
                        hours2 = 11;
                        minutes2 = "59 PM";
                    } else {
                        hours2 = hours2 - 12;
                        minutes2 = minutes2 + " PM";
                    }
                } else {
                    hours2 = hours2;
                    minutes2 = minutes2 + " AM";
                }
                
                if (timeToLabel)
                    $(timeToLabel).html(hours2 + ':' + minutes2);
            }
        });
    };
    
    /**
     * 
     * @param {type} dateFromSelector
     * @param {type} dateToSelector
     * @param {type} $scope
     * @param {type} fromVar
     * @param {type} toVar
     * @returns {undefined}
     */
    UIHelper.prototype.initDateRange = function(dateFromSelector,dateToSelector,$scope,fromVar,toVar) {
            var setDate = function (isFrom, dateStr) {
                 var date = dateStr;
                 if (isFrom) {
                     $scope[fromVar] = date;
                 } else {
                     $scope[toVar] = date;
                 }
             };
             
            function getDate(element) {
                var date;
                try {
                    date = $.datepicker.parseDate(dateFormat, element.value);
                } catch (error) {
                    date = null;
                }
                return date;
            }
            
            var dateFormat = "yy-mm-dd",
                from = $(dateFromSelector)
                .datepicker({
                    defaultDate: "+1w",
                    changeMonth: true,
                    numberOfMonths: 3,
                    dateFormat: 'yy-mm-dd'
                })
                .on("change", function () {
                    to.datepicker("option", "minDate", getDate(this));
                    setDate(true, getDate(this));
                }),
                to = $(dateToSelector).datepicker({
                    defaultDate: "+1w",
                    dateFormat: 'yy-mm-dd',
                    changeMonth: true,
                    numberOfMonths: 3
                })
                .on("change", function () {
                    from.datepicker("option", "maxDate", getDate(this));
                    setDate(false, getDate(this));
                });


    };
    
    UIHelper.prototype.setSlider = function(serviceUrl,table,fieldName,$scope) {
        var url = serviceUrl +'/' +table+'/?'+fieldName+'$min&'+fieldName+'$max';
        url = url.replace('//','/');
        $.get(url, function (data) {
            data = JSON.parse(data);
            data = data[0];
            $scope[fieldName+'From'] = data[fieldName+'_min'];
            $scope[fieldName+'To'] = data[fieldName+'_max'];
            $("#slider-range"+fieldName).slider({
                range: true,
                min: 0,
                max: $scope[fieldName+'To'],
                step: 10,
                values: [0, $scope[fieldName+'To']],
                slide: function (event, ui) {
                    $("#"+fieldName).val("$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ]);
                }
            });
            $("#"+fieldName).val("$" + $("#slider-range"+fieldName).slider("values", 0) +
                    " - $" + $("#slider-range"+fieldName).slider("values", 1));
        });
    };
    
    UIHelper.prototype.glueArrayWithElement=function (arr,elem){
        var html=[];
        for (var i = 0 ; i < arr.length ; i++){
            html.push('<'+elem+'>');
            html.push(arr[i]);
            html.push('</'+elem+'>');
        }
        return html.join("\r\n");
    };
    
    return UIHelper;
})();
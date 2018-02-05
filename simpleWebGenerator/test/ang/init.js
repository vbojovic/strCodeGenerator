//'use strict';
//var app=null;
////$(document).ready(function(){
//  app= angular.module('myApp', ["ngRoute"]);
//          <script type="text/javascript">
////                        'use strict';
////
//            var app = angular.module('myApp',[]);
//        </script>


var initTimeRange = function (rangeElement,timeFromLabel,timeToLabel) {
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




var initDateRange = function(dateFromSelector,dateToSelector,$scope,fromVar,toVar) {
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
    
    
              
 /*-------------------------------------------------------------auth_userController.js-------------------------------------------------------------------------------*/ 
 app.controller("publicAuth_userController", ["$scope", "$rootScope", "$http", "$window", function ($scope, $rootScope, $http, $window) { 
 	$scope.visible = false; 
 	$scope.debug = false; 
 	$scope.includeFilters = true; 
 	$scope.data = {}; 
 	$scope.statistics = {}; 
 	$scope.pass=null; 
 	$scope.token=null; 
 	$scope.email=null; 
 	$scope.access_time=null; 
 	$scope.access_time$min=null; 
 	$scope.access_time$max=null; 
 	$scope.full_name=null; 
 	$scope.role_id=null; 
 	$scope.role_ids=[]; 
          $scope.setter=function(varName,value){ 
              $scope[varName]=value; 
          }; 
         $scope.$watch("visible", function () { 
             if ($scope.visible == false) 
                 return; 
             $scope.init(); 
         }); 
         $scope.hide = function () { 
             if ($scope.visible == true) 
                 $scope.visible = false; 
         }; 
         $scope.unhide = function () { 
             if ($scope.visible == false) 
                 $scope.visible = true; 
         }; 
         $scope.$on("unhidePublicAuth_userController", function (event, data) { 
             if ($scope.debug) { 
                 console.log("-------------------"); 
                 console.log("PublicAuth_userController: UNHIDE"); 
             } 
             $scope.unhide(); 
         }); 
         $scope.$on("hidePublicAuth_userController", function (event, data) { 
             if ($scope.debug) { 
                 console.log("-------------------"); 
                 console.log("PublicAuth_userController: HIDE"); 
             } 
             $scope.hide(); 
         }); 
 $scope.init = function () { 
 $scope.genericLoader('role_id',$rootScope.svcUrl+'/public.auth_roles/'); 
 }; 
 $scope.statLoader=function(varNames,url){ 
     var postData={}; 
     for (var i = 0; i < varNames.length; i++){ 
          postData[varNames[i]+"$min"]=1; 
          postData[varNames[i]+"$max"]=1; 
          postData[varNames[i]+"$cnt"]=1; 
     } 
     var obj={ 
         url: url, 
         type: "GET", 
         dataType: "json", 
         data: postData, 
         headers: {"Authorization": $rootScope.getToken()}, 
         success: function (data) { 
             $scope.statistics=data; 
             $scope.statistics.changed=1; 
         }, 
         error: $scope.showMsg(true,"Error loading statistics!") 
     }; 
     $.ajax(obj); 
 }; 
 $scope.genericLoader=function(varName,url){ 
     var postData={}; 
     var obj={ 
         url: url, 
         type: "GET", 
         dataType: "json", 
         data: postData, 
         headers: {"Authorization": $rootScope.getToken()}, 
         success: function (data) { 
             if (data[varName]!=undefined) { $scope[varName]=data;} 
             else {  
                  $scope[varName+"$min"]=data[varName+"$min"]; 
                  $scope[varName+"$max"]=data[varName+"$max"]; 
             }  
         }, 
         error: $scope.showMsg(true,"Error loading data!") 
     }; 
     $.ajax(obj); 
 }; 
 $scope.filterClick=function(){ 
        var data = $scope.getJTableDefinition(); 
        $(".expensesContainer") 
          .find(".jtableClass") 
          .jtable(data); 
         var getData = { 
              "pass":$scope.pass, 
              "token":$scope.token, 
              "email":$scope.email, 
              "access_time":$scope.access_time, 
              "full_name":$scope.full_name, 
              "role_id":$scope.role_id, 
         }; 
        $(".expensesContainer") 
                .find(".jtableClass") 
                .jtable("load", getData);      
 }; 
 $scope.getJTableDefinition=function(){ 
 	var tableData={ 
 		title: $rootScope.LANG["LIST_TABLE_NAME_PUBLIC_AUTH_USER"], 
 		paging: true, //Enable paging             
 		sorting: true, //Enable sorting              
 		defaultSorting: "id ASC",   
 		openChildAsAccordion: true, 
 actions: {             
     listAction: "./main.php?a=',actionName,'_list_jTable",     
     deleteAction: "./main.php?a=',actionName,'_delete_jTable",      
     updateAction: "./main.php?a=',actionName,'_update_jTable",      
     createAction: "./main.php?a=',actionName,'_insert_jTable"     
 }, 
 fields: {  
 	} 
 }; 
 	return tableData; 
 }; 
 //TODO moran provjerit ima li ikakvih DATE polja jer ako ima, tada moran drugacije postupit 
 }]); 
 
              

////
///*-------------------------------------------------------------usersController.js-------------------------------------------------------------------------------*/
//app.controller("publicUsersController", ["$scope", "$rootScope", "$http", "$window", function ($scope, $rootScope, $http, $window) {
//        $scope.visible = true;
//        $scope.debug = false;
//        $scope.includeFilters = true;
//        $scope.data = {};
//        $scope.login = null;
//        $scope.pass = null;
//        $scope.first_name = null;
//        $scope.last_name = null;
//        $scope.email = null;
//        $scope.loc_reason = null;
//        $scope.locked = null;
//        $scope.created = null;
//        $scope.last_login = null;
//        $scope.admin = null;
//        $scope.nick_name = null;
//        $scope.place_id = null;
//        $scope.is_seller = null;
//        $scope.about_myself = null;
//        $scope.picture_available = null;
//        $scope.place_addition = null;
//        $scope.country_id = null;
//        $scope.lang_id = null;
//        $scope.role_id = null;
//        $scope.valute_id = null;
//        $scope.use_metric_system = null;
//        $scope.conventional_production = null;
//        $scope.ecological_production = null;
//        $scope.industrial_production = null;
//        $scope.queen_production = null;
//
//        $scope.setter = function (varName, value) {
//            $scope[varName] = value;
//        };
//
//        $scope.$watch("visible", function () {
//            if ($scope.visible == false)
//                return;
//            $scope.init();
//        });
//        $scope.hide = function () {
//            if ($scope.visible == true)
//                $scope.visible = false;
//        };
//        $scope.unhide = function () {
//            if ($scope.visible == false)
//                $scope.visible = true;
//        };
//        $scope.$on("unhidePublicUsersController", function (event, data) {
//            if ($scope.debug) {
//                console.log("-------------------");
//                console.log("PublicUsersController: UNHIDE");
//            }
//            $scope.unhide();
//        });
//        $scope.$on("hidePublicUsersController", function (event, data) {
//            if ($scope.debug) {
//                console.log("-------------------");
//                console.log("PublicUsersController: HIDE");
//            }
//            $scope.hide();
//        });
//        $scope.init = function () {
////            $scope.initDateRanges(dateFromSelector, dateToSelector, $scope, fromVar, toVar);
////            $scope.initDateRanges(dateFromSelector, dateToSelector, $scope, fromVar, toVar);
////            $scope.initDateRanges(dateFromSelector, dateToSelector, $scope, fromVar, toVar);
//            $scope.genericLoader('place_id', $rootScope.svcUrl + '/public.places/');
//            $scope.genericLoader('country_id', $rootScope.svcUrl + '/public.places/');
//            $scope.genericLoader('lang_id', $rootScope.svcUrl + '/public.languages/');
//            $scope.genericLoader('role_id', $rootScope.svcUrl + '/public.roles/');
//            $scope.genericLoader('valute_id', $rootScope.svcUrl + '/public.valutes/');
//            $scope.setSliders();
//            $scope.initDateRanges();
//            $scope.initTimeRange();
//        };
//
//
//
//        $scope.setSliders = function () {
////            var url = $rootScope.serviceUrl + 'amount_stat/';
////            $.get(url, function (data) {
////                data = JSON.parse(data);
////                data = data[0];
////                $scope.amountFrom = data.amount_min;
////                $scope.amountTo = data.amount_max;
//            $("#slider-range").slider({
//                range: true,
//                min: 0,
//                max: 50000,
//                step: 10,
////                values: [0, $scope.amountTo],
//                values: [0, 500],
//                slide: function (event, ui) {
//                    $("#amount").val("$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ]);
//                }
//            });
//            $("#amount").val("$" + $("#slider-range").slider("values", 0) +
//                    " - $" + $("#slider-range").slider("values", 1));
////            });
//        };
//        $scope.initTimeRange = function () {
//            $("#slider-timeRange").slider({
//                range: true,
//                min: 0,
//                max: 1440,
//                step: 15,
//                values: [540, 1020],
//                slide: function (e, ui) {
//                    var hours1 = Math.floor(ui.values[0] / 60);
//                    var minutes1 = ui.values[0] - (hours1 * 60);
//
//                    if (hours1.length == 1)
//                        hours1 = '0' + hours1;
//                    if (minutes1.length == 1)
//                        minutes1 = '0' + minutes1;
//                    if (minutes1 == 0)
//                        minutes1 = '00';
//                    if (hours1 >= 12) {
//                        if (hours1 == 12) {
//                            hours1 = hours1;
//                            minutes1 = minutes1 + " PM";
//                        } else {
//                            hours1 = hours1 - 12;
//                            minutes1 = minutes1 + " PM";
//                        }
//                    } else {
//                        hours1 = hours1;
//                        minutes1 = minutes1 + " AM";
//                    }
//                    if (hours1 == 0) {
//                        hours1 = 12;
//                        minutes1 = minutes1;
//                    }
//
//
//
//                    $('.slider-time').html(hours1 + ':' + minutes1);
//
//                    var hours2 = Math.floor(ui.values[1] / 60);
//                    var minutes2 = ui.values[1] - (hours2 * 60);
//
//                    if (hours2.length == 1)
//                        hours2 = '0' + hours2;
//                    if (minutes2.length == 1)
//                        minutes2 = '0' + minutes2;
//                    if (minutes2 == 0)
//                        minutes2 = '00';
//                    if (hours2 >= 12) {
//                        if (hours2 == 12) {
//                            hours2 = hours2;
//                            minutes2 = minutes2 + " PM";
//                        } else if (hours2 == 24) {
//                            hours2 = 11;
//                            minutes2 = "59 PM";
//                        } else {
//                            hours2 = hours2 - 12;
//                            minutes2 = minutes2 + " PM";
//                        }
//                    } else {
//                        hours2 = hours2;
//                        minutes2 = minutes2 + " AM";
//                    }
//
//                    $('.slider-time2').html(hours2 + ':' + minutes2);
//                }
//            });
//        };
//        $scope.setDate = function (isFrom, dateStr) {
//
//            var date = dateStr;
//            if (isFrom) {
//                $scope.dateFrom = date;
//            } else {
//                $scope.dateTo = date;
//            }
//        };
//
//        $scope.initDateRanges = function (dateFromSelector, dateToSelector, $scope, fromVar, toVar) {
////            $(function () {
//            var dateFormat = "yy-mm-dd",
//                    from = $("#dateFrom")
//                    .datepicker({
//                        defaultDate: "+1w",
//                        changeMonth: true,
//                        numberOfMonths: 3,
//                        dateFormat: 'yy-mm-dd'
//                    })
//                    .on("change", function () {
//                        to.datepicker("option", "minDate", getDate(this));
//                        $scope.setDate(true, getDate(this));
//                    }),
//                    to = $("#dateTo").datepicker({
//                defaultDate: "+1w",
//                dateFormat: 'yy-mm-dd',
//                changeMonth: true,
//                numberOfMonths: 3
//            })
//                    .on("change", function () {
//                        from.datepicker("option", "maxDate", getDate(this));
//                        $scope.setDate(false, getDate(this));
//                    });
//
//            function getDate(element) {
//                var date;
//                try {
//                    date = $.datepicker.parseDate(dateFormat, element.value);
//                } catch (error) {
//                    date = null;
//                }
//
//                return date;
//            }
////            });
//        }
//        $scope.genericLoader = function (varName, url) {
//            postData = {};
//            var obj = {
//                url: url,
//                type: "GET",
//                dataType: "json",
//                data: postData,
//                headers: {"Authorization": $rootScope.getToken()},
//                success: function (data) {
//                    $scope[varName] = data;
//                },
//                error: $scope.showMsg(true, "Error loading data!")
//            };
//            $.ajax(obj);
//        };
//
//
//        $rootScope.svcUrl = "";
//        $rootScope.getToken = function () {
//
//        };
//        $scope.showMsg = function () {
//
//        }
//
//    }]);
//
//              
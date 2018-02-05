var GenericHelper = (function () {
    function GenericHelper() {
    };
    
    GenericHelper.prototype.clone = function (obj) {
        if (null == obj || "object" != typeof obj)
            return obj;
        var copy = obj.constructor();
        for (var attr in obj) {
            if (obj.hasOwnProperty(attr))
                copy[attr] = obj[attr];
        }
        return copy;
    };
    
    GenericHelper.prototype.getSelectionText = function () {
        var text = "";
        if (window.getSelection) {
            text = window.getSelection().toString();
        } else if (document.selection && document.selection.type != "Control") {
            text = document.selection.createRange().text;
        }
        return text;
    };
    GenericHelper.prototype.copyToClipboardWithBreaks = function (element,breakELement) {
        var text = $(element).first().clone().find(breakELement).prepend('\r\n').end().text();
        element = $('<textarea>').appendTo('body').val(text).select();
        document.execCommand('copy');
        element.remove();
    }; 

    GenericHelper.prototype.capitaliseFirstLetter = function (string)
    {
        return this.capitalizeFirstLetter(string);
    };

    GenericHelper.prototype.capitalizeFirstLetter = function (string)
    {
        return string.charAt(0).toUpperCase() + string.slice(1);
    };
    
    GenericHelper.prototype.getFunctionName = function (schemaName, tableName, isDatabase) {
        var funcName = '';
        if (isDatabase) {
            return schemaName + '_' + tableName;
        } else {
            var tableNameChunks = tableName.split('_');
            if (tableNameChunks.length === 1) {
                return schemaName + this.capitaliseFirstLetter(tableName);
            } else {
                funcName = schemaName;
                for (var i = 0; i < tableNameChunks.length; i++) {
                    funcName = funcName + this.capitaliseFirstLetter(tableNameChunks[i]);
                }
            }
        }
        return funcName;
    };
    
    GenericHelper.prototype.glueArrayElements = function (arr, prefix, sufix) {
        if (arr == null || arr.length == 0)
            return arr;
        var arrCloned = arr.slice(0);
        for (var i = 0; i < arrCloned.length; i++) {
            if (arrCloned[i] == null)
                continue;
            arrCloned[i] =
                    ((prefix == null) ? '' : prefix)
                    + arrCloned[i]
                    + ((sufix == null) ? '' : sufix);
        }
        return arrCloned;
    };
    
    GenericHelper.prototype.joinArrayWithPRefixes = function (arr, prefix, separator) {
        var arrCloned = this.glueArrayElements(arr, prefix, '');
        return arrCloned.join(separator);
    };
    
    GenericHelper.prototype.joinArrayWithGlues = function (arr, prefix, sufix, separator) {
        var arrCloned = this.glueArrayElements(arr, prefix, sufix);
        return arrCloned.join(separator);
    };
    
    GenericHelper.prototype.doubleArrayWithGlues = function (arr, prefix, sufix, separator) {
        var arrCloned = arr.slice(0);
        for (var i = 0; i < arrCloned.length; i++) {
            if (arrCloned[i] == null)
                continue;
            arrCloned[i] =
                    ((prefix == null) ? '' : prefix)
                    + arrCloned[i]
                    + ((separator == null) ? '' : separator)
                    + arrCloned[i]
                    + ((sufix == null) ? '' : sufix);
        }
        //this.glueArrayElements(arr,prefix,sufix);
        return arrCloned;
    }
    /*
     * 
     * @param {String} selectId - Id comboboxa
     * @param {String} url - adresa na koju cu poslat 
     * @param {String[]} keyFieldsArr
     * @param {String} valueField
     * @param {String} selectedIdValue - in case of editing, value to be selected
     * @returns {void}
     */
    GenericHelper.prototype.fillSelectFromUrlPost = function (selectId, url, keyFieldsArr, valueField, postAttribsWithValuesObj, selectedIdValue) {
        var posting = $.post(url, postAttribsWithValuesObj, function (data) {
        }, "json");
        posting.done(function (data) {
            var value = data[valueField];
            var key = "";
            for (var i = 0; i < keyFieldsArr.length; i++) {
                if (i != 0)
                    key += ',';
                //TODO ispitat jesan li dobro skuzija kljuc ili san pokupija samo value bespotrebno
                key += data[keyFieldsArr[i]];
            }

            //TODO selectedIdValue treba isprobat
            $('#' + selectId).append($("<option></option>")
                    .attr("value", key)
                    .text(value));
            //TODO ispitat ovaj dio!
            if (selectedIdValue != null && selectedIdValue != '' && selectedIdValue == data[key]) {
                $('#' + selectId).select(key);
            }
        });
    }

    GenericHelper.prototype.int2charNdx = function (num) {
        if (num < 25) {
            return 'abcdefghijklmnopqrstuvwxyz'[num];
        } else {
            return 'abcdefghijklmnopqrstuvwxyz'[num % 26] + (num / 26);
        }
    }

    GenericHelper.prototype.isElementInArray = function (elem, arr) {
        if (arr == null || arr.length === 0)
            return false;
        for (var i = 0; i < arr.length; i++) {
            if (arr[i] === elem)
                return true;
        }
        return false;
    }

    /**
     * ovu metodu ne mogu zvat intersect jer moram brisat podatke kod 
     * intersektanja da ne dodje do duplavanja kad drugi isti podatak 
     * naleti na isti match u searchu
     * 
     * @param {type[]} arr1
     * @param {type[]} arr2
     * @returns {boolean}
     */
    GenericHelper.prototype.doArraysHaveIntersection = function (arr1, arr2) {
        if (arr1 == null || arr2 == null)
            return false;
        var resultArr = new Array();
        for (var i = 0; i < arr1.length; i++) {
            if (this.isElementInArray(arr1[i], arr2)) {
                //resultArr.push(arr2[i]);
                return true;
            }
        }
        return false;
    }

    GenericHelper.prototype.getIntersectionItemsOfUniqueArray = function (uniqArr, arr2) {
        if (uniqArr == null || arr2 == null || uniqArr.length === 0 || arr2.length === 0)
            return null;
        var resultArr = new Array();
        for (var i = 0; i < uniqArr.length; i++) {
            if (this.isElementInArray(uniqArr[i], arr2)) {
                resultArr.push(uniqArr[i]);
                continue;
            }
        }
        return resultArr;
    }

    GenericHelper.prototype.isArray = function (obj) {
        return Object.prototype.toString.call(obj) === '[object Array]';
    }

    /**
     * 
     * @param {string or array with words} strOrArray
     * @param {type} separator
     * @returns {String}
     */
    GenericHelper.prototype.capitalizeFirstAndDecapitalizeRest = function (strOrArray, separator) {
        var str = "";
        var arr = new Array();
        if (!this.isArray(strOrArray))
            arr = strOrArray.split(separator);
        for (var i = 0; i < arr.length; i++) {
            if (i === 0)
                str = this.capitalizeFirstLetter(arr[i].toLowerCase());
            else
                str += ' ' + arr[i].toLowerCase();
        }
        return str;
    }

    GenericHelper.prototype.unique = function (a) {
        var temp = {};
        for (var i = 0; i < a.length; i++)
            temp[a[i]] = true;
        var r = [];
        for (var k in temp)
            r.push(k);
        return r;
    }

    return GenericHelper;
})();
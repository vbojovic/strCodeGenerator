var Generator = (function () {
    var _dbModel;
    var _resultsArr;
    var _functions;
    var _selectedTableNames;
    function Generator(dbModelStr) {
        _dbModel = JSON.parse(dbModelStr);
        _resultsArr = {
            "data": new Array(),
            "templateName": null
        };
        _selectedTableNames = new Array();

        this.loadDefultFunctions();

    }

    Generator.prototype.loadDefultFunctions = function () {
        _functions = new Array();
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg', name: "LaravelAppzCoderTemplate", funcName: "LaravelAppzCoderTemplate"});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP/js/bootstrap', name: "Fullstack1Template", funcName: "Fullstack1Template"});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'js', name: "JTable attrib view actions ", funcName: "JTableAttribViewActionsJs"});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP', name: "PgPHP update jTable With Array", funcName: "PgPHPUpdateJTableWithArray"});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP', name: "PgPHP insert jTable With Array", funcName: "PgPHPInsertJTableWithArray"});

        _functions.push({singleFile: false, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'js', name: "jTable Text Areas Js", funcName: "jTableTextAreasJs", 'callingArgs': new Array()});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP', name: "Pg PHP count SQL", funcName: "PgPHPCountSql"});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg', name: "Pg Sequence To Max Value In table", funcName: "PgSeqToMaxValInTbl"});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'shell', name: "Shell Create Images JTable", funcName: "shCreateImagesJTable"});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP/js', name: "PgPHP update jTable", funcName: "PgPHPUpdateJTable"});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'Pg/PHP/js', name: "PgPHP update sql returning", funcName: "PgPHPUpdateSqlReturning"});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'Pg/PHP/js', name: "PgPHP insert sql returning", funcName: "PgPHPInsertSqlReturning"});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP/js/JSON', name: "PgPHP options jTable Sql", funcName: "PgPhpJTableOptionsSql"});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP/js', name: "Pg PHP List Users JTable Sql Helper", funcName: "PgPHPListUsersJTableSqlHelper"});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP/js', name: "Pg PHP List SQL JTable", funcName: "PgPHPListSqlJTable"});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP/js', name: "Simple Pg SQL Inserts Generator 100", funcName: "SimplePgSQLInsertsGenerator100"});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP/js', name: "PHP jTable custom options", funcName: "PHPJTableCustomOptions"});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP/js', name: "PgPHP insert jTable", funcName: "PgPHPInsertJTable"});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'html/js/bootstrap/angular', name: "Angular bootstrap JTable REST", funcName: "angularJTableRestBootstrap"});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'html/js/bootstrap', name: "Dashboard generator bootstrap JTable", funcName: "dashboardGeneratorBootstrapJTable"});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'html', name: "list form jTable divs", funcName: "ListFormJTableDivs"});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'text', name: "LineOperators", funcName: "LineOperators"});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP/js', name: "PgPHP list form jTable.js unnested", funcName: "PgPHPListFormJTableJsUnnested"});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP/js', name: "PgPHP list form jTable.js", funcName: "PgPHPListFormJTableJs"});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP/js', name: "PgPHP Options Actions jTable", funcName: "PhpJTableOptionsActions", 'callingFunction': '', 'callingArgs': new Array()});

        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP/js', name: "PgPHP delete jTable", funcName: "PgPHPDeleteJTable", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP/js', name: "PgPHP list form jTable", funcName: "PgPHPListFormJTable", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP', name: "PgPHP list jTable", funcName: "PgPHPListJTable", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP', name: "PgPHP list jTable (actions)", funcName: "PgPHPListJTableActions", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP', name: "PgPHP get with lookups", funcName: "PgPHPGetWithLookup", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP', name: "PgPHP get with lookup and users", funcName: "PgPHPGetWithLookupUsers", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: false, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'HTML/Handlebar', name: "Handlebar view form", funcName: "HandlebarViewForm", 'callingArgs': new Array()});
        _functions.push({singleFile: false, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP', name: "PgPHP view with actions", funcName: "PgPHPViewWithActions", 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'html/js/bootstrap', name: "Dashboard generator bootstrap", funcName: "dashboardGeneratorBootstrap", 'callingFunction': '', 'callingArgs': new Array(), 'header': null, 'footer': null});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'html/js', name: "Handlebar templates loader", funcName: "handleBarTemplatesLoader", 'callingFunction': '', 'callingArgs': new Array(), 'header': null, 'footer': null});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'Pg/PHP/js', name: "PgPHP insert sql", funcName: "PgPHPInsertSql", 'callingFunction': '', 'callingArgs': new Array(), 'header': null, 'footer': null});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'Pg/PHP/js', name: "PgPHP update sql", funcName: "PgPHPUpdateSql", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'Pg/PHP', name: "PgPHP delete", funcName: "PgPHPDelete", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'Pg/PHP', name: "PgPHP delete returning", funcName: "PgPHPDeleteReturning", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'Pg/PHP/js', name: "PgPHP search", funcName: "PgPHPSearch", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'Pg/PHP/js', name: "PgPHP select", funcName: "PgPHPSelect", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP', name: "PgPHP get", funcName: "PgPHPGet", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'PHP/js', name: "PHP left menu bootstrap", funcName: "PHPLeftMenuBootstrap", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'PHP/js', name: "PHP top menu bootstrap", funcName: "PHPTopMenuBootstrap", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'PHP', name: "PHP actions", funcName: "PHPActions", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'PHP', name: "PHP unlink", funcName: "PHPUnlink", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'PHP', name: "PHP classes", funcName: "PHPClasses", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'PHP', name: "PHP class model", funcName: "PHPClasseModel", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'PHP/html/css/js', name: "PgPHP list form", funcName: "PgPHPListForm", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP', name: "PgPHP list sql", funcName: "PgPHPListSql", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP', name: "PgPHP list sql with counts", funcName: "PgPHPListSqlWithCount", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP', name: "PgPHP list", funcName: "PgPHPList", 'callingFunction': '', 'callingArgs': new Array()});
        _functions.push({singleFile: true, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP', name: "PgPHP switch", funcName: "PgPHPSwitch", 'callingArgs': new Array(), 'header': null, 'footer': null});
        _functions.push({singleFile: false, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg/PHP', name: "PgPHP view", funcName: "PgPHPView", 'callingArgs': new Array()});
        _functions.push({singleFile: false, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg', name: "Pg delete function (N/A)", funcName: "PgDeleteFunction", 'callingArgs': new Array()});
//        _functions.push({singleFile: false, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg', name: "Pg insert function", funcName: "PgInsertFunction", 'callingArgs': new Array()});
//        _functions.push({singleFile: false, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg', name: "Pg update function", funcName: "PgUpdateFunction", 'callingArgs': new Array()});
//        _functions.push({singleFile: false, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg', name: "Pg Switch", funcName: "PgSwitch", 'callingArgs': new Array()});
//        _functions.push({singleFile: false, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'C#', name: "C# View Model", funcName: "CSViewModel", 'callingArgs': new Array()});
//        _functions.push({singleFile: false, prefixFile: '', sufixFile: '', method: 'tStr', lang: 'Pg', name: "Pg warehouse", funcName: "PgWarehouse", 'callingArgs': new Array()});
        _functions.push({singleFile: false, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'js', name: "Translation variables", funcName: "jsTransVars", 'callingArgs': new Array()});
        _functions.push({singleFile: false, prefixFile: '', sufixFile: '', method: 'tmp', lang: 'js', name: "Translation variables autotranslated", funcName: "jsTransVarsAutotranslated", 'callingArgs': new Array()});

        for (var i = 0; i < _functions.length; i++) {
            _functions[i].args = new Array("dbModel", "tableName");
            //_functions[i].funcName= this.getFuncNameFromDescr(_functions[i].name,true,_functions[i].args);
            //_functions[i].funcNameWithArgs= this.getFuncNameWithArgs(_functions[i].funcName,true,_functions[i].args);
            //console.log(functions[i].funcName);
        }
    }
    //TODO tu san sta
    Generator.prototype.getSqlFieldFormattedByType = function (schemaName, tableName, fieldName) {
        if (this.isFieldBoolean(schemaName, tableName, fieldName) === true) {
            return "case when " + fieldName + "=true then 'true' when  " + fieldName + "=false then 'false' else null end as " + fieldName;
            // return "case when "+fieldName+"=true then 'true' when  "+fieldName+"=false then 'false' else null end ";
        } else if (this.isFieldDateOrTime(schemaName, tableName, fieldName) === true) {
            return "to_char(" + fieldName + ",'YYYY-MM-DD') as " + fieldName;
            // return "to_char("+fieldName+",'YYYY-MM-DD') ";
        } else {
            return fieldName;
        }
    };

    /**
     * helper za trazenje //nije testirano
     * @param {type} fieldName
     * @returns {Array(SchemaTable)}
     */
    Generator.prototype.getTablesContainingField = function (fieldName) {
        //TODO
        var tableNames = this.getTableNames();
//        var tableNames2 = jQuery.extend({}, tableNames);
        var resultTableNames = [];
        for (var y = 0; y < tableNames.length; y++) {
            var tableSchema2 = tableNames[y];
            var fields = this.getTableFieldNamesArr(tableSchema2.schema, tableSchema2.table, false);
            if (fields.indexOf(fieldName) === -1)
                continue;
            resultTableNames.push(new {
                table: tableSchema2.table,
                schema: tableSchema2.schema
            });
        }
        ;
        //slucaj kad su jednaki onda iden kopat po toj tablici
        return resultTableNames;
    };

    /**
     * cemu ovo sluzi?? jel oce vidit koji su polja u jednoj i polja u drugoj, tj sta FKovi dile medjusobno? zasto mi je ovo tribalo?
     * @param {type} schemaTable1
     * @param {type} schemaTable2
     * @returns {array{tableschema,field}}
     */
    Generator.prototype.getMatchingFieldsBetweenTables = function (schemaTable1, schemaTable2) {
        //TODO
    };

    //TODO ovu   funkciju moram podsit
    Generator.prototype.getSqlFieldFormattedByTypeWithAliasArr = function (schemaName, tableName, aliasName, skipAutogenerated) {
        var fields = this.getTableFields(schemaName, tableName, skipAutogenerated);
        var fieldNames = new Array();
        for (var i = 0; i < fields.length; i++) {
            var fieldName = fields[i].fieldName;
            // var aliasedFieldName = (aliasName+"."+fieldName+" as "+schemaName+"_"+tableName+"_"+fieldName).toUpperCase();
            var aliasedFieldName = (schemaName + "_" + tableName + "_" + fieldName).toUpperCase();
            // aliasedFieldName = this.getSqlFieldFormattedByTypeWithAlias(schemaName, tableName,fieldName,aliasName+'.'+fieldName,aliasedFieldName);
            aliasedFieldName = this.getSqlFieldFormattedByTypeWithAlias(schemaName, tableName, fieldName, aliasName + '.' + fieldName, aliasedFieldName);
            fieldNames.push(aliasedFieldName);
        }
        return fieldNames;
    };

    Generator.prototype.getSqlFieldFormattedByTypeWithAlias = function (schemaName, tableName, fieldName, fieldPath, fieldAlias) {
        // console.log(schemaName+'.'+tableName+'.'+fieldName+' path:'+fieldPath+' alias:'+fieldAlias);
        if (this.isFieldBoolean(schemaName, tableName, fieldName) === true) {
            return "case when " + fieldPath + "=true then 'true' when  " + fieldPath + "=false then 'false' else null end as " + fieldAlias;
        } else if (this.isFieldDateOrTime(schemaName, tableName, fieldName) === true) {
            return "to_char(" + fieldPath + ",'YYYY-MM-DD') as " + fieldAlias;
        } else {
            return fieldPath + ' as ' + fieldAlias;
        }
    }


    Generator.prototype.getFunctionListByKeywords = function (kwds) {
        var data = new Object();
        data.functions = new Array();
        for (var i = 0; i < _functions.length; i++) {
            var func = _functions[i];
            var targetLine = func.name + " " + func.lang + " " + func.funcName;
            targetLine = targetLine.toLowerCase();
            var kwdsArr = kwds.split(/\s/g);
            var wordsFound = 0;
            for (var j = 0; j < kwdsArr.length; j++) {
                if (targetLine.search(kwdsArr[j]) > -1)
                    wordsFound++;
            }
            if (wordsFound == kwdsArr.length)
                data.functions.push(func);
        }
        return data;
    };
    /**
     * 
     * @param {String} schemaName
     * @param {String} TableName
     * @returns {Array}
     */
    Generator.prototype.getBooleanFieldsNamesNotInPk = function (schemaName, TableName) {
        var nonPkey = this.getTableFields(schemaName, TableName, true);
        var fieldNames = new Array();
        for (var i = 0; i < nonPkey.length; i++) {
            if (nonPkey[i].genericDataType === "bool")
                fieldNames.push(nonPkey[i].fieldName);
        }
        return fieldNames;
    }
    /**
     * 
     * @param {String} schemaName
     * @param {String} tableName
     * @returns {String}
     */
    Generator.prototype.getFirstVarcharField = function (schemaName, tableName) {
        var tableFields = this.getTableFieldNamesArr(schemaName, tableName, true);
        if (tableFields == null)
            return null;
        for (var i = 0; i < tableFields.length; i++) {
            if (this.isFieldTextual(schemaName, tableName, tableFields[i]) === true)
                return tableFields[i];
        }
        return null;
    }
    /**
     * 
     * @param {String} schemaName
     * @param {String} TableName
     * @param {String} fieldName
     * @returns {Boolean}
     */
    Generator.prototype.isFieldBoolean = function (schemaName, TableName, fieldName) {
        var field = this.getFieldData(schemaName, TableName, fieldName);
        if (field == null)
            console.log("isFieldBoolean: " + schemaName + '.' + TableName + '.' + fieldName + " cannot be null");
        return (field.genericDataType === "bool");
    }
    /**
     * 
     * @param {String} schemaName
     * @param {String} TableName
     * @returns {Boolean}
     */
    Generator.prototype.hasBooleanFieldsNotInPk = function (schemaName, TableName) {
        var nonPkey = this.getTableFields(schemaName, TableName, true);
        for (var i = 0; i < nonPkey.length; i++) {
            if (nonPkey[i].genericDataType === "bool")
                return true;
        }
        return false;
    }
    /**
     * 
     * @param {type} schTable
     * @returns {String}
     */
    Generator.prototype.getTableNameFromSchTable = function (schTable) {
        return schTable.split("\.")[1];
    }
    /**
     * 
     * @param {type} schTable
     * @returns {String}
     */
    Generator.prototype.getSchNameFromSchTable = function (schTable) {
        return schTable.split("\.")[0];
    }
    /**
     * 
     * @param {String} funcKey
     * @returns {_L1._functions|Array}
     */
    Generator.prototype.getFunctionDataByKey = function (funcKey) {
        for (var i = 0; i < _functions.length; i++) {
            if (_functions[i].funcName === funcKey)
                return _functions[i];
        }
        return null;
    };
    Generator.prototype.clearSelectedTables = function () {
        while (_selectedTableNames.length > 0) {
            _selectedTableNames.pop();
        }
    }

    Generator.prototype.addSelectedTableNamesBySchTbl = function (schemaNameTableName) {
        _selectedTableNames.push({
            'schema': this.getSchNameFromSchTable(schemaNameTableName)
            , 'table': this.getTableNameFromSchTable(schemaNameTableName)
        });
    }
    Generator.prototype.addSelectedTableNames = function (schemaName, tableName) {
        _selectedTableNames.push({'schema': schemaName, 'table': tableName});
    }
    Generator.prototype.setSelectedTableNames = function (selectedTableNames) {
        this.clearSelectedTables();
        _selectedTableNames = selectedTableNames;
    }
    Generator.prototype.setSelectedTableNamesBySchTbl = function (selectedTableSchemaNames) {
        this.clearSelectedTables();
        for (var i = 0; i < selectedTableSchemaNames.length; i++) {
            //_selectedTableNames = selectedTableNames;
            var schTblName = selectedTableSchemaNames[i];
            this.addSelectedTableNamesBySchTbl(schTblName);
        }
    }
    Generator.prototype.getSelectedTableNames = function () {
        return _selectedTableNames;
    }
    Generator.prototype.getFuncTemplateFileName = function (funcNameNoArgs) {
        for (var i = 0; i < _functions.length; i++) {
            if (_functions[i].funcName === funcNameNoArgs)
                return _functions[i].funcName + ".html";
        }
        return null;
    }
    Generator.prototype.getDbModel = function () {
        return _dbModel;
    }
    Generator.prototype.setDbModel = function (dbModelStr) {
        _dbModel = JSON.parse(dbModelStr);
    };
    Generator.prototype.capitaliseFirstLetter = function (string)
    {
        return string.charAt(0).toUpperCase() + string.slice(1);
    };
    Generator.prototype.getAllTables = function () {
        return this.extractTables();
    };

    Generator.prototype.getLookups = function (inSelected) {
        var lookups = [];
        var tables = (inSelected)
                ? this.getSelectedTableNames()
                : this.extractTables();

        for (var i = 0; i < tables.length; i++) {
            if (!this.isLookupTable(tables[i].schemaName, tables[i].tableName))
                continue;
            lookups.push(tables[i]);
        }

        return lookups;
    };
    
    Generator.prototype.cleanEmptySchemas = function(){
        if (_dbModel == null)
            return null;

        var i = 0;
        var schemas = _dbModel.schemas['@items'];
        var resultTables = new Array();
        for ( i = schemas.length -1 ; i>=0 ; i-- ) {
            var schemaName = schemas[i].schemaName;
            var j = 0;

            var tables = schemas[i].tables["@items"];
            if (tables == undefined) {
                _dbModel.schemas['@items'] = _dbModel.schemas['@items'].splice(i,1)
            }
  
        }
        return resultTables;
    }
    
    Generator.prototype.extractTables = function () {
        if (_dbModel == null)
            return null;
        this.cleanEmptySchemas(); //TODO viktor corrections
        var i = 0;
        var schemas = _dbModel.schemas['@items'];
        var resultTables = new Array();
        for (; i < schemas.length; ) {
            var schemaName = schemas[i].schemaName;
            var j = 0;

            var tables = schemas[i].tables["@items"];
            if (tables == undefined) continue;
            for (; j < tables.length; ) {
                resultTables.push({
                    "schema": schemaName,
                    "table": tables[j].Name
                });
                j++;
            }
            i++;
        }
        return resultTables;
    }

    Generator.prototype.extractSchemaNames = function () {
        if (_dbModel == null)
            return null;
        var i = 0;
        var schemas = _dbModel.schemas['@items'];
        var resultSchemas = new Array();
        for (; i < schemas.length; ) {
            var schemaName = schemas[i].schemaName;
            resultSchemas.push(schemaName);
            i++;
        }
        return resultSchemas;
    }

    Generator.prototype.getFunctionList = function () {
        var data = new Object();
        data.functions = _functions;
        return data;
    };

    Generator.prototype.getTable = function (schemaName, tableName) {
        if (_dbModel == null)
            return null;
        var i = 0;
        var schemas = _dbModel.schemas["@items"];

        for (; i < schemas.length; ) {
            if (schemas[i].schemaName === schemaName) {
                var j = 0;
                var tables = schemas[i].tables["@items"];
                for (; j < tables.length; ) {
                    if (tables[j].Name === tableName)
                        return tables[j];
                    j++;
                }
            }
            i++;
        }
        return null;
    };

    /**
     * 
     * @param {type} schemaName
     * @param {type} tableName
     * @returns {Array}
     */
    Generator.prototype.getPkeyFieldNames = function (schemaName, tableName) {
        var table = this.getTable(schemaName, tableName);
        var fields = new Array();
        for (var i = 0; i < table.fields['@items'].length; i++) {
            var field = table.fields['@items'][i];
            if (field.inPrimaryKey)
                fields.push(field.fieldName);
        }
        return fields;
    };

    /**
     * 
     * @param {type} schemaName
     * @param {type} tableName
     * @returns {Array}
     */
    Generator.prototype.getPkeyFieldNamesWithTablePrefix = function (schemaName, tableName) {
        var table = this.getTable(schemaName, tableName);
        var fields = new Array();
        for (var i = 0; i < table.fields['@items'].length; i++) {
            var field = table.fields['@items'][i];
            if (field.inPrimaryKey)
                fields.push(tableName + '.' + field.fieldName);
        }
        return fields;
    };

    /**
     * 
     * @param {type} schemaName
     * @param {type} tableName
     * @returns {Array}
     */
    Generator.prototype.getPkeyFields = function (schemaName, tableName) {
        var table = this.getTable(schemaName, tableName);
        var fields = new Array();
        for (var i = 0; i < table.fields['@items'].length; i++) {
            var field = table.fields['@items'][i];
            if (field.inPrimaryKey)
                fields.push(field);
        }
        return fields;
    };

    /**
     * 
     * @param {String} schemaName
     * @param {String} tableName
     * @param {bool} skipAutogenerated
     * @returns {Field|Array(javaSerializedObj{})}
     */
    Generator.prototype.getTableFields = function (schemaName, tableName, skipAutogenerated) {
        if (tableName === null || schemaName === null) {
            return;
        }
        var table = this.getTable(schemaName, tableName);
        if (table === null) {
            return;
        }

        var fields = new Array();
        for (var i = 0; i < table.fields['@items'].length; i++) {
            //TODO provjerit koji su PKey ili autogenerirani (tstamp, sekvence)
            var field = table.fields['@items'][i];
            if ((skipAutogenerated && (field.genericDataType === 'integer' || field.genericDataType === 'smallint' || field.genericDataType === 'bigint') /*&& field.inPrimaryKey*/ && (field.defaultValue != null) && (field.defaultValue.indexOf("nextval") != -1))
                    || (skipAutogenerated && (field.genericDataType === 'timestamp') && (field.defaultValue != null))
                    ) {
                continue;
            }
            fields.push(field);
        }
        return fields;
    };

    /**
     * 
     * @param {type} schemaName
     * @param {type} tableName
     * @param {type} skipAutogenerated
     * @returns {Array(String)}
     */
    Generator.prototype.getTableFieldNamesArr = function (schemaName, tableName, skipAutogenerated) {
        var fields = this.getTableFields(schemaName, tableName, skipAutogenerated);
        if (fields == null)
            return null;
        var fieldNames = new Array();
        for (var i = 0; i < fields.length; i++) {
            var fieldName = fields[i].fieldName;
            fieldNames.push(fieldName);
        }
        return fieldNames;
    };


    /**
     * 
     * @param {type} file
     * @param {type} code
     * @param {type} decr
     * @param {type} templateName
     * @returns {undefined}
     */
    Generator.prototype.addToResultsArray = function (file, code, decr, templateName) {
        _resultsArr.templateName = templateName;
        _resultsArr.data.push({
            "file": file,
            "code": code,
            "descr": decr
        });
    };
    /**
     * 
     * @param {type} data
     * @param {type} templateName
     * @returns {undefined}
     */
    Generator.prototype.setResultsArray = function (data, templateName) {
        _resultsArr.templateName = templateName;
        _resultsArr.data = data;
    };
    /**
     * 
     * @returns {generator_L1._resultsArr}
     */
    Generator.prototype.getResultsArray = function () {
        return  _resultsArr;
    };

    /**
     * 
     * @param {type} functionKey
     * @param {type} tablesSchemas
     * @param {type} destObjectId
     * @returns {undefined}
     */
    Generator.prototype.generateCode = function (functionKey, tablesSchemas, destObjectId) {
        var templateName = this.getFuncTemplateFileName(functionKey);
        var templateUrl = './generatorTemplates/' + templateName;
        var funcName = this.getFunctionDataByKey(functionKey).funcNameNoArgs;
        this.setSelectedTableNamesBySchTbl(tablesSchemas);
        var inputData = this.getData();
        $.get(templateUrl, function (data) {
            $('#' + destObjectId).html(data);
            var code = tmpl('user_tmpl', inputData);
            $('#codeDiv').html(code);

            $(document).ready(function () {
                var cnt = $('code').length;
                var buttons = '';
                var tabs = '';
                for (var i = 1; i <= cnt; i++) {
                    buttons += '<button type="button" class="page btn btn-success">' + i + '</button>';
                }
                buttons += '<button type="button" class="copy btn btn-warning">Copy</button>';

                $('#codeDiv').prepend(buttons);

                $('pre code').each(function (i, e) {
                    hljs.highlightBlock(e);
                });
                $('code').hide();
                $('code:eq(0)').show();
            });
        });

    };

    /**
     * 
     * @param {type} functionKey
     * @param {type} tablesSchemas
     * @param {type} destObjectId
     * @returns {undefined}
     */
    Generator.prototype.generateCodeFromStringTemplate = function (functionKey, tablesSchemas, destObjectId) {
        var templateName = this.getFuncTemplateFileName(functionKey);
        var templateUrl = './generatorTemplates/' + templateName;
        //TODO
//        var  funcName = this.getFunctionDataByKey(functionKey).funcNameNoArgs;
//        this.setSelectedTableNamesBySchTbl(tablesSchemas);
//        var inputData = this.getData();
//        console.log(inputData);
//        $.get( templateUrl, function( data ) {
//            $('#'+destObjectId).html(data);
//            var code = tmpl('user_tmpl',inputData);
//            $('#codeDiv').html(code);
//        });

    };

    /**
     * TODO
     * @param {type} functionKey
     * @param {type} tablesSchemas
     * @param {type} destObjectId
     * @returns {undefined}
     */
    Generator.prototype.generateCodeDirect = function (functionKey, tablesSchemas, destObjectId) {
        if (tablesSchemas.length == 0 || tablesSchemas == null) {
            alert('Please select tables');
            return;
        }
        //TODO
//        this.setSelectedTableNamesBySchTbl(tablesSchemas);
//        var func = this.getFunctionDataByKey(functionKey);
//        var inputData = this.getData();
//        console.log(inputData);
//        $.get( templateUrl, function( data ) {
//            $('#'+destObjectId).html(data);
//            var code = tmpl('user_tmpl',inputData);
//            $('#codeDiv').html(code);
//        });

    };

    /**
     * 
     * @param {type} schemaName
     * @returns {Array[String]}
     */
    Generator.prototype.extractTablesFromSchema = function (schemaName) {
        var tablesSchemaObject = this.extractTables();
        var tables = new Array();
        //TODO ovo projerit. mislin da dolje ide length
        for (var i = 0; i < tablesSchemaObject.length; i++) {
            if (tablesSchemaObject[i].schema === schemaName) {
                tables.push(tablesSchemaObject[i].table);
            }
        }
        return tables;
    };

    /**
     * 
     * @param {type} schemaName
     * @param {type} tableName
     * @param {type} isDatabase
     * @returns {String}
     */
    Generator.prototype.getFunctionName = function (schemaName, tableName, isDatabase) {
        var funcName = '';
        if (isDatabase) {
            return schemaName + '_' + tableName;
        } else {
            var tableNameChunks = tableName.split('_');
            if (tableNameChunks.length == 1) {
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

    /**
     * 
     * @param {type} schemaName
     * @param {type} tableName
     * @returns {Array}
     */
    Generator.prototype.getUserFieldNamesFromTable = function (schemaName, tableName) {
        var allFields = this.getTableFieldNamesArr(schemaName, tableName, false);
        var userFields = new Array();
        for (var i = 0; i < allFields.length; i++) {
            if (this.isUserField(allFields[i]))
                userFields.push(allFields[i]);
        }
        return userFields;
    };

    /**
     * 
     * @param {type} fieldName
     * @returns {Boolean}
     */
    Generator.prototype.isUserField = function (fieldName) {
        var userFieldNames = "userid,user_id,osoba_id,osobaid,person_id,personid".toLowerCase().split(",");
        return (userFieldNames.indexOf(fieldName) > -1);
    };

    /**
     * 
     * @returns {generator_L1.Generator.prototype.getData.data}
     */
    Generator.prototype.getData = function () {
        var data = {
            'selectedTables': this.getSelectedTableNames(),
            'dbModel': this.getDbModel(),
            //'generator': $(this),
            'generator': this,
            'genericHelper': new GenericHelper(),
            'viewableSections': "edit,view,search,advanced_search".split(','),
            'allSections': "delete,save,edit,view,insert,search,advanced_search,list".split(','),
            'searchableSections': "search,advanced_search,list".split(','),
            'userFieldNamesAllowed': "userid,user_id,osoba_id,osobaid,person_id,personid".toLowerCase().split(","),
            'predefinedData': {},
        };
        data.lookupTableNames = this.getLookupTableNames();
        var schemaNames = this.extractSchemaNames();
        data.predefinedData.schemaNames = schemaNames;
        for (var i = 0; i < schemaNames; i++) {
            data.predefinedData[schemaNames[i]] = {};
            var tables = this.extractTablesFromSchema(schemaNames[i]);
            data.predefinedData[schemaNames[i]]._tableNames = tables;
        }

        return data;
    }
    /**
     * 
     * @param {type} tableSchemas
     * @param {type} glue
     * @returns {String}
     */
    Generator.prototype.tableSchemas2StrJoin = function (tableSchemas, glue) {
        if (tableSchemas == null || tableSchemas.length == 0)
            return null;
        var joinedStr = '';
        for (var i = 0; i < tableSchemas.length; i++) {
            if (i > 0)
                joinedStr += glue;
            joinedStr += tableSchemas[i].schema + '.' + tableSchemas[i].table;
        }
        return joinedStr;
    };
    
    Generator.prototype.htmlEntities=function(str) {
       return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
    };
    /*
     * 
     * @param {type} schemaName
     * @param {type} tableName
     * @returns {Array(schemaTable{})}
     */
    Generator.prototype.getReferencingTables = function (schemaName, tableName) {
        var table = this.getTable(schemaName, tableName);
        var tables = new Array();
        var fKeys = table.foreignKeys['@items'];
        if (fKeys == null)
            return null;
        for (var i = 0; i < fKeys.length; i++) {
            tables.push({
                table: fKeys[i].referencingTable.table,
                schema: fKeys[i].referencingTable.schema
            });
        }
        return tables;
    };
    /**
     * 
     * @param {String} schemaName
     * @param {String} tableName
     * @returns {Boolean}
     */
    Generator.prototype.isPigtail = function (schemaName, tableName) {
        var srcTables = this.getReferencingTables(schemaName, tableName);
        for (var i = 0; i < srcTables.length; i++) {
            if (srcTables[i].table === tableName && srcTables[i].schema === schemaName)
                return true;
        }
        return false;
    }

    /**
     * 
     * @param {String} schemaName
     * @param {String} tableName
     * @param {String} fieldName
     * @returns {Boolean}
     */
    Generator.prototype.isFieldPartOfFKey = function (schemaName, tableName, fieldName) {
        var table = this.getTable(schemaName, tableName);
        var fKeys = table.foreignKeys['@items'];
        if (fKeys == null)
            return false;
        for (var i = 0; i < fKeys.length; i++) {
            var keyFields = table.foreignKeys['@items'][i].fields['@items'];
            for (var j = 0; j < keyFields.length; j++) {
                //console.log(keyFields[j].fieldName);
                if (keyFields[j].fieldName == fieldName)
                    return true;
            }
        }
        return false;
    }
    /**
     * 
     * @param {String} schemaName
     * @param {String} tableName
     * @param {String} fieldName
     * @returns {Boolean}
     */
    Generator.prototype.isFieldPartOfCompositeFKey = function (schemaName, tableName, fieldName) {
        var table = this.getTable(schemaName, tableName);
        var fKeys = table.foreignKeys['@items'];
        for (var i = 0; i < fKeys.length; i++) {
            var keyFields = table.foreignKeys['@items'][i].fields['@items'];
            for (var j = 0; j < keyFields.length; j++) {
                if (keyFields[j].fieldName === fieldName && keyFields.length > 1)
                    return true;
            }
        }
        return false;
    };

    /**
     * 
     * @param {String} schemaName
     * @param {String} tableName
     * @param {String} srcSchema
     * @param {String} srcTable
     * @param {boolean} isSource
     * @returns {Array}
     */
    Generator.prototype.getRefTableFieldNames = function (schemaName, tableName, srcSchema, srcTable, isSource) {
        var table = this.getTable(schemaName, tableName);
        var fields = new Array();
        var fKeys = table.foreignKeys['@items'];
        for (var i = 0; i < fKeys.length; i++) {
            if (fKeys[i].referencingTable.table === srcTable && fKeys[i].referencingTable.schema === srcSchema) {
                var keyFields = (isSource)
                        ? table.foreignKeys['@items'][i].referencingFields['@items']
                        : table.foreignKeys['@items'][i].fields['@items'];
                for (var j = 0; j < keyFields.length; j++) {
                    fields.push(keyFields[j].fieldName);
                }
            }
        }
        return fields;
    }
    //TODO testirat
    /**
     * 
     * @param {String} schemaName
     * @param {String} tableName
     * @param {String} alias
     * @param {boolean} skipAutogenerated
     * @returns {Array}
     */
    Generator.prototype.getTableFieldNamesWithAliasesArr = function (schemaName, tableName, alias, skipAutogenerated) {
        var fields = this.getTableFields(schemaName, tableName, skipAutogenerated);
        var fieldNames = new Array();
        for (var i = 0; i < fields.length; i++) {
            var fieldName = fields[i].fieldName;
            var aliasedFieldName = (alias + "." + fieldName + " as " + schemaName + "_" + tableName + "_" + fieldName).toUpperCase()
            fieldNames.push(aliasedFieldName);
        }
        return fieldNames;
    };

    /**
     *     //TODO nije testirano
     * @param {type} schemaName
     * @param {type} tableName
     * @param {type} fieldName
     * @returns {Field|generator_L1.Generator.prototype.getFieldData.fields|undefined|Array}
     */
    Generator.prototype.getFieldData = function (schemaName, tableName, fieldName) {
        var fields = this.getTableFields(schemaName, tableName, false);
        for (var i = 0; i < fields.length; i++) {
            if (fieldName === fields[i].fieldName)
                return fields[i];
        }
        return null;
    };

    /*
     *     
     * @param {type} schemaName
     * @param {type} tableName
     * @param {type} fieldName
     * @returns {String} //TODO nije testirano
     */
    Generator.prototype.getFieldGenericDataType = function (schemaName, tableName, fieldName) {
        var fields = this.getTableFields(schemaName, tableName, false);
        var field = null;
        for (var i = 0; i < fields.length; i++)
            if (fieldName === fields[i].fieldName)
                return fields[i].genericDataType;
        return field;
    };

    /**
     * 
     * @param {type} dataType
     * @param {type} fieldName
     * @returns {String}
     */
    Generator.prototype.genericDataType2PhpInject = function (dataType, fieldName) {
        var types = {};
        types.integer = "(int)%var%";
        types.varchar = "addslashes(%var%)";
        types.bool = "(%var% == \'\' || %var% == null || %var% == \'0\' || %var% == false || strtolower(%var%) == \'f\' || strtolower(%var%) == \'false\')? \'FALSE\' : \'TRUE\';";
        types.date = "addslashes(%var%)";
        types.time = "addslashes(%var%)";
        types.timestamp = "addslashes(%var%)";
        types.double = "floatval(%var%)";
        types.smallint = types.integer;
        types.bigint = types.integer;
        types.char = types.varchar;
        types.text = types.varchar;

        return types[dataType.toLowerCase()].replace(/%var%/g, "$" + fieldName);
    };

    /**
     * 
     * @param {type} dataType
     * @param {type} fieldName
     * @returns {String}
     */
    Generator.prototype.genericDataType2PhpInject4autoins = function (dataType, fieldName) {
        var types = {};
        types.integer = "(int)%var%";
        types.varchar = "addslashes(%var%)";
        types.bool = "(%var% == \'\' || %var% == null || %var% == \'0\' || %var% == false || strtolower(%var%) == \'f\' || strtolower(%var%) == \'false\')? FALSE : TRUE;";
        types.date = "addslashes(%var%)";
        types.time = "addslashes(%var%)";
        types.timestamp = "addslashes(%var%)";
        types.double = "floatval(%var%)";
        types.smallint = types.integer;
        types.bigint = types.integer;
        types.char = types.varchar;
        types.text = types.varchar;

        return types[dataType.trim().toLowerCase()].replace(/%var%/g, "$" + fieldName);

    };
    /**
     * 
     * @param {String} schemaName
     * @param {String} TableName
     * @param {String} fieldName
     * @returns {Boolean}
     */
    Generator.prototype.isFieldTextual = function (schemaName, TableName, fieldName) {
        var dataType = this.getFieldGenericDataType(schemaName, TableName, fieldName);
        return (dataType === "varchar" || dataType === "text" || dataType === "json");
    }
    /**
     * 
     * @param {String} schemaName
     * @param {String} TableName
     * @param {String} fieldName
     * @returns {Boolean}
     */
    Generator.prototype.isFieldNumeric = function (schemaName, TableName, fieldName) {
        var dataType = this.getFieldGenericDataType(schemaName, TableName, fieldName).trim().toLowerCase();
//        return (dataType === "integer" || dataType === "double" || dataType === "smallint" || dataType === "bigint");
        return this.isFieldTypeNumeric(dataType);
    };
    
    /**
     * 
     * @param {type} dataType
     * @returns {Boolean}
     */
    Generator.prototype.isFieldTypeNumeric= function(dataType){
        return (dataType === "integer" || dataType === "double" || dataType === "smallint" || dataType === "bigint");
    };
    /**
     * 
     * @param {String} schemaName
     * @param {String} TableName
     * @param {String} fieldName
     * @returns {Boolean}
     */
    Generator.prototype.isFieldDateOrTime = function (schemaName, TableName, fieldName) {
        var dataType = this.getFieldGenericDataType(schemaName, TableName, fieldName).trim().toLowerCase();
        return (dataType === "time" || dataType === "date" || dataType === "timestamp");
    }
    /**
     * 
     * @param {String} schemaName
     * @param {String} tableName
     * @returns {String Array}
     */
    Generator.prototype.getFKeyNames = function (schemaName, tableName) {
        var table = this.getTable(schemaName, tableName);
        var fKeys = table.foreignKeys['@items'];
        if (fKeys == null)
            return null;

        var fKeyNames = new Array();
        for (var i = 0; i < fKeys.length; i++) {
            fKeyNames.push(fKeys[i].name);
        }
        return fKeyNames;
    };
    /**
     * 
     * @param {Field[]} fields
     * @returns {String[]}
     */
    Generator.prototype.fields2fieldNames = function (fields) {
        if (fields == null || fields.length == 0)
            return null;
        var resultArr = new Array();
        for (var i = 0; i < fields.length; i++) {
            resultArr.push(fields[i].fieldName);
        }
        return resultArr;
    };

    /**
     * 
     * @param {String} schemaName
     * @param {String} tableName
     * @param {String} fKeyName
     * @param {bool} isSrc
     * @returns {String[]}
     */
    Generator.prototype.getFKeyFieldNames = function (schemaName, tableName, fKeyName, fromSrc) {
        var table = this.getTable(schemaName, tableName);
        var fKeys = table.foreignKeys['@items'];
        if (fKeys == null)
            return null;
        var resultArr = new Array();
        for (var i = 0; i < fKeys.length; i++) {
            if (fKeys[i].name === fKeyName) {
                var fields = (fromSrc)
                        ? fKeys[i].referencingFields["@items"]
                        : fKeys[i].fields["@items"];
                return  this.fields2fieldNames(fields);
            }
        }
        return resultArr;
    };

    Generator.prototype.doubleQuoteStrEntity = function (str) {
        return '&quot;' + str + '&quot;';
    };

    Generator.prototype.getSetterName = function (fieldName) {
        return 'set' + this.capitaliseFirstLetter(fieldName);
    };

    Generator.prototype.getGetterName = function (fieldName) {
        return 'get' + this.capitaliseFirstLetter(fieldName);
    };

    /**
     * 
     * @param {string} schemaName
     * @param {string} tableName
     * @param {string} fKeyName
     * @returns {{schema,table}}
     */
    Generator.prototype.getFKeySrcTable = function (schemaName, tableName, fKeyName) {
        var table = this.getTable(schemaName, tableName);
        var fKeys = table.foreignKeys['@items'];
        if (fKeys == null)
            return null;
        var resultArr = new Array();
        for (var i = 0; i < fKeys.length; i++) {
            if (fKeys[i].name == fKeyName) {
                return {
                    'table': fKeys[i].referencingTable.table,
                    'schema': fKeys[i].referencingTable.schema
                };
            }
        }
        return null;
    };
    /**
     * 
     * @param {type} schemaName
     * @param {type} tableName
     * @returns {int}
     */
    Generator.prototype.getNumberOfNonPKeyFields = function (schemaName, tableName) {
        var fields = this.getTableFieldNamesArr(schemaName, tableName, true);
        return fields.length;
    };

    /**
     * 
     * @param {type} schemaName
     * @param {type} tableName
     * @param {type} fieldName
     * @returns {tableSchema{table,schema}}
     */
    Generator.prototype.getFKeySrcTableByField = function (schemaName, tableName, fieldName) {
        var table = this.getTable(schemaName, tableName);
        if (table == null)
            return null;
        var fKeys = table.foreignKeys['@items'];
        if (fKeys == null)
            return null;

        var resultArr = new Array();
        for (var i = 0; i < fKeys.length; i++) {
            var fieldsInFk = this.getFKeyFieldNames(schemaName, tableName, fKeys[i].name, false);
            var fieldInFk = ($.inArray(fieldName, fieldsInFk) > -1);
            if (fieldInFk == true) {
                return {
                    'table': fKeys[i].referencingTable.table,
                    'schema': fKeys[i].referencingTable.schema
                };
            }
        }
        return null;
    }


    /* //lookup tablicu definiram ka onu tablicu koja ima unique index na jednom ili 2 polja koji nisu ID i pri tome je duzina minus suma pkey i uniq manja od 2
     * @param {type} schemaName
     * @param {type} tableName
     * @returns {boolean}
     */
    Generator.prototype.isLookupTable = function (schemaName, tableName) {
        var table = this.getTable(schemaName, tableName);
        var pKeyFields = this.getPkeyFieldNames(schemaName, tableName);
        var fieldCount = this.getTableFieldNamesArr(schemaName, tableName);
        if (table.indices["@items"] == undefined) return false;
        for (var i = 0; i < table.indices["@items"].length; i++) {
            var ndx = table.indices["@items"][i];
            //console.log(ndx);
            if (ndx.isUnique == true) {
                var fields = ndx.fields["@items"];
                if (fieldCount.length - (fields.length + pKeyFields.length) > 2)
                    continue;
                for (var j = 0; j < fields.length; j++) {
                    var field = fields[j];
                    if (field.inPrimaryKey == true)
                        continue;
                    return true;
                }
            }
        }
        return false;
    };

    /**
     * 
     * @param {type} schemaName
     * @param {type} tableName
     * @returns {Array(String)|unresolved|Array}
     */
    Generator.prototype.getLookupFields = function (schemaName, tableName) {
        if (!this.isLookupTable(schemaName, tableName))
            return null;
        var fields = new Array();
        fields = this.getTableFieldNamesArr(schemaName, tableName, true);
        return fields;
    };

    /*
     * check if there is link between two tables
     * @param {string} schemaName
     * @param {string} tableName
     * @param {string} srcShemaName
     * @param {string} srcTableName
     * @returns {bool}
     */
    Generator.prototype.isTableReferencingAnotherOne = function (schemaName, tableName, srcShemaName, srcTableName) {
        var fkeyNames = this.getFKeyNames(schemaName, tableName);
        if (fkeyNames == null)
            return false;
        for (var i = 0; i < fkeyNames.length; i++) {
            var fkeyName = fkeyNames[i];
            var fkSrcTable = this.getFKeySrcTable(schemaName, tableName, fkeyName);
            if (fkSrcTable.table == srcTableName && fkSrcTable.schema == srcShemaName)
                return true;
        }
        return false;
    };

    /*
     * 
     * @returns {Array{schemaTable}}
     */
    Generator.prototype.getTableNames = function () {
        if (_dbModel == null)
            return null;
        var i = 0;
        var schemas = _dbModel.schemas["@items"];
        if (schemas == null || schemas.length == 0)
            return null;
        var tableNames = new Array();
        for (; i < schemas.length; ) {
            var j = 0;
            var tables = schemas[i].tables["@items"];
            for (; j < tables.length; ) {
                tableNames.push({schema: schemas[i].schemaName, table: tables[j].Name});
                j++;
            }
            i++;
        }
        return tableNames;
    };
    /**
     * 
     * @param {string} schemaName
     * @param {string} tableName
     * @param {string} srcSchemaName
     * @param {string} srcTableName
     * @returns {int}
     */
    Generator.prototype.getFkeyCountBetweenTables = function (schemaName, tableName, srcSchemaName, srcTableName) {
        if (!this.isTableReferencingAnotherOne(schemaName, tableName, srcSchemaName, srcTableName))
            return 0;
        var fkeyNames = this.getFKeyNames(schemaName, tableName);
        if (fkeyNames == null)
            return 0;
        var cnt = 0;
        for (var i = 0; i < fkeyNames.length; i++) {
            var fKeyName = fkeyNames[i];
            var srcTable = this.getFKeySrcTable(schemaName, tableName, fKeyName);
            if (srcTable.schema === srcSchemaName && srcTable.table === srcTableName) {
                cnt++;
            }
        }
        return cnt;
    };
    /**
     * 
     * @param {string} schemaName
     * @param {string} tableName
     * @param {string} srcSchemaName
     * @param {string} srcTableName
     * @returns {array of object { schema}}
     */
    Generator.prototype.getFkeyFieldNamesOfDestTable = function (schemaName, tableName, srcSchemaName, srcTableName, index) {
        if (!this.isTableReferencingAnotherOne(schemaName, tableName, srcSchemaName, srcTableName))
            return null;
        var fkeyNames = this.getFKeyNames(schemaName, tableName);
        if (fkeyNames == null)
            return null;
        var cnt = 0;
        for (var i = 0; i < fkeyNames.length; i++) {
            var fKeyName = fkeyNames[i];
            var srcTable = this.getFKeySrcTable(schemaName, tableName, fKeyName);
            if (srcTable.schema === srcSchemaName && srcTable.table === srcTableName) {
                cnt++;
                if (cnt === index)
                    return this.getFKeyFieldNames(schemaName, tableName, fKeyName, false);
            }
        }
        return null;
    };
    /**
     * 
     * @param {type} schemaName
     * @param {type} tableName
     * @param {type} srcSchemaName
     * @param {type} srcTableName
     * @returns {Array(String)}
     */
    Generator.prototype.getFkeyNamesBetweenTables = function (schemaName, tableName, srcSchemaName, srcTableName) {
        if (!this.isTableReferencingAnotherOne(schemaName, tableName, srcSchemaName, srcTableName))
            return null;
        var fkeyNames = this.getFKeyNames(schemaName, tableName);
        if (fkeyNames == null)
            return null;
        var fkeyNamesLinked = new Array();
        for (var i = 0; i < fkeyNames.length; i++) {
            var fKeyName = fkeyNames[i];
            var srcTable = this.getFKeySrcTable(schemaName, tableName, fKeyName);
            if (srcTable.schema === srcSchemaName && srcTable.table === srcTableName)
                fkeyNamesLinked.push(fKeyName);
        }
        return fkeyNamesLinked;
    };
    /*
     * pokusava na temelju 
     * @param {string} srcShemaName
     * @param {string} srcTableName
     * @returns {{schema,table}[]}
     */
    Generator.prototype.getDestTableNames = function (srcShemaName, srcTableName) {
        //TODO testirat!
        var srcTable = this.getTable(srcShemaName, srcTableName);

        var selectedTables = this.getTableNames();
        var destTables = new Array();
        for (var i = 0; i < selectedTables.length; i++) {
            var tableName = selectedTables[i].table;
            var schemaName = selectedTables[i].schema;

            if (tableName == srcTableName && schemaName == srcShemaName)
                continue;
            var fKeyNames = this.getFKeyNames(schemaName, tableName);
            if (fKeyNames == null)
                continue;
            for (var j = 0; j < fKeyNames.length; j++) {
                var srcTable = this.getFKeySrcTable(schemaName, tableName, fKeyNames[j]);
                if (srcTable.schema == srcShemaName && srcTable.table == srcTableName) {
                    destTables.push({table: tableName, schema: schemaName});
                    break;
                }
            }
        }
        return destTables;
    };
    /*
     * gleda da li se sors atrib iz src tablice povezuje sa destTablicom
     * @param {string} schemaName
     * @param {string} tableName
     * @param {string} srcShemaName
     * @param {string} srcTableName
     * @param {string} srcAttrib
     * @returns {boolean}
     */
    Generator.prototype.isAttribFKeyInDestTable = function (schemaName, tableName, srcShemaName, srcTableName, srcAttrib) {
        if (this.isTableReferencingAnotherOne(schemaName, tableName, srcShemaName, srcTableName) == false)
            return false;
        var fKeysNames = this.getFKeyNames(schemaName, tableName);
        for (var i = 0; i < fKeysNames.length; i++) {
            var fKeyName = fKeysNames[i];
            var srcTable = this.getFKeySrcTable(schemaName, tableName, fKeyName);
            //console.log(srcTable);
            if (!(srcTable.table == srcTableName && srcTable.schema == srcShemaName))
                continue;
            var srcFields = this.getFKeyFieldNames(schemaName, tableName, fKeyName, true);
            return ($.inArray(srcAttrib, srcFields) > -1);
        }
        return false;
    };

    /**
     * 
     * @returns {Array(String)}
     */
    Generator.prototype.getLookupTableNames = function () {
        var tableNames = new Array();
        var allTableNames = this.getTableNames();
        for (var i = 0; i < allTableNames.length; i++) {
            if (this.isLookupTable(allTableNames[i].schema, allTableNames[i].table) === true) {
                tableNames.push(allTableNames[i]);
            }
        }
        return tableNames;
    };
    /**
     * 
     * @param {String} tableName
     * @param {{table,schema}[]} tableNames
     * @returns {Boolean}
     */
    Generator.prototype.isTableNameInTableNames = function (tableName, tableNames) {
        if (tableName == null || tableNames == null)
            return false;
        for (var i = 0; i < tableNames; i++) {
            if ((tableNames[i].schema === tableName.schema) && (tableNames[i].table === tableName.table)) {
                return true;
            }
        }
        return false;
    };

    /**
     * 
     * @param {string} schemaName
     * @param {string} tableName
     * @param {string} fieldName
     * @param {boolean} isDedicatedLookup
     * @returns {String}
     */
    Generator.prototype.getJTableLookupUrlForField = function (schemaName, tableName, fieldName) {
        var srcTable = this.getFKeySrcTableByField(schemaName, tableName, fieldName);
        if (srcTable == null)
            return null;
        var pkeyFields = this.getPkeyFieldNames(srcTable.schema, srcTable.table);
        var funcName = srcTable.schema.toLowerCase() + "_" + srcTable.table.toLowerCase();
        var isGenericLookup = !this.isLookupTable(srcTable.schema, srcTable.table);
        if (isGenericLookup === false)
            return "main.php?a=" + funcName + "_options_jTable";
        var firstVarcharField = this.getFirstVarcharField(srcTable.schema, srcTable.table);
        if (firstVarcharField == null)
            firstVarcharField = this.getTableFieldNamesArr(srcTable.schema, srcTable.table, true)[0];
        return "main.php?a=lookup_jTable&tableName=" + srcTable.table + "&schemaName=" + srcTable.schema + "&fieldName=" + firstVarcharField + "&id=" + pkeyFields[0];
    };

    /**
     * 
     * @param {type} schemaName
     * @param {type} tableName
     * @param {type} fieldName
     * @param {type} schemaNameSrc
     * @param {type} tableNameSrc
     * @param {type} fieldNameSrc
     * @returns {Boolean}
     */
    Generator.prototype.areFieldsLinked = function (schemaName, tableName, fieldName, schemaNameSrc, tableNameSrc, fieldNameSrc) {
        var srcTable = this.getFKeySrcTableByField(schemaName, tableName, fieldName);
        if (srcTable == null)
            return false;
        if (!(srcTable.schema === schemaNameSrc && srcTable.table === tableNameSrc))
            return false;
        var pkeyFields = this.getPkeyFieldNames(srcTable.schema, srcTable.table);
        return ($.inArray(fieldNameSrc, pkeyFields) > -1);
    };

    /**
     * 
     * @param {string} schemaName
     * @param {string} tableName
     * @param {string} fieldName
     * @returns {String}
     */
    Generator.prototype.getSourceFieldFromDestField = function (schemaName, tableName, fieldName) {
        var srcTable = this.getFKeySrcTableByField(schemaName, tableName, fieldName);
        if (srcTable == null)
            return null;
        var pkeyFields = this.getPkeyFieldNames(srcTable.schema, srcTable.table);
        for (var i = 0; i < pkeyFields.length; i++) {
            if (this.areFieldsLinked(schemaName, tableName, fieldName, srcTable.schema, srcTable.table, pkeyFields[i]) === true)
                return pkeyFields[i];
        }
        return null;
    };


    /**
     *     //TODO ovo napravit takso da vrati imena onih kljuceva u kojima je polje a da nije samo
     * @param {type} schemaName
     * @param {type} tableName
     * @param {type} fieldName
     * @returns {undefined}
     */
    Generator.prototype.getCompositeFKeyNames = function (schemaName, tableName, fieldName) {
        /*
         var table = this.getTable(schemaName,tableName);
         var fKeys = table.foreignKeys['@items'];
         for (var i = 0 ; i < fKeys.length; i++){
         var keyFields =  table.foreignKeys['@items'][i].fields['@items']; 
         for (var j = 0 ; j < keyFields.length; j++){
         if (keyFields[j].fieldName == fieldName && keyFields.length>1) return true;
         } 
         } 
         return false;
         */
    };
    /**
     * 
     * @param {type} schemaName
     * @param {string} tableName
     * @returns {string[]}
     */
    Generator.prototype.getSequenceNamesFromPkey = function (schemaName, tableName) {
        if (this.getTable(schemaName, tableName) == null)
            return null;
        var fieldNames = this.getPkeyFieldNames(schemaName, tableName);
        if (fieldNames == null || fieldNames.length === 0)
            return null;
        var seqNames = new Array();
        for (var i = 0; i < fieldNames.length; i++) {
            var field = this.getFieldData(schemaName, tableName, fieldNames[i]);
            if (field.defaultValue.indexOf("nextval") != -1) {
                var seq = field.defaultValue;
                seq = seq.replace(/nextval\(\'|\'::regclass\)/g, "");
                seqNames.push(seq);
            }
        }
        return seqNames;
    };

    /**
     * 
     * @param {type} schemaName
     * @param {type} tableName
     * @returns {Array}
     */
    Generator.prototype.getSequenceNamesAndFieldsFromPkey = function (schemaName, tableName) {
        if (this.getTable(schemaName, tableName) == null)
            return null;
        var fieldNames = this.getPkeyFieldNames(schemaName, tableName);
        if (fieldNames == null || fieldNames.length === 0)
            return null;
        var seqNames = new Array();
        for (var i = 0; i < fieldNames.length; i++) {
            var field = this.getFieldData(schemaName, tableName, fieldNames[i]);
            if (field == null || field.defaultValue == null)
                continue;
            if (field.defaultValue.indexOf("nextval") != -1) {
                var seq = field.defaultValue;
                seq = seq.replace(/nextval\(\'|\'::regclass\)/g, "");
                seqNames.push({sequence: seq, fieldName: fieldNames[i]});
            }
        }
        return seqNames;
    };

    Generator.prototype.entityDecode = function (html) {
        return $('<div>').html(html).text();
    };

    Generator.prototype.beautifyHtml = function (html) {
        return js_beautify(html, {
            "indent_size": 4,
            "html": {
                "end_with_newline": false,
                "js": {
                    "indent_size": 4
                },
                "css": {
                    "indent_size": 2
                }
            },
            "css": {
                "indent_size": 2
            },
            "js": {
                "preserve-newlines": true
            }
        });
    };

    Generator.prototype.genericDataTypes = {
        int: 'integer',
        bigint: 'bigint',
        smallint: 'smallint',
        str: 'varchar',
        date: 'date',
        numeric: 'numeric',
        number: 'number',
        double: 'double',
        time: 'time',
        boolean: 'bool',
        txt: 'text',
        chr: 'char',
        tstamp: 'timestamp',
        getNumbers: function () {
            return [this.int, this.double, this.number, this.smallint, this.bigint];
        },
        getTexts: function () {
            return [this.txt, this.chr, this.str];
        },
        getDates: function () {
            return [this.date, this.tstamp];
        }
    };

    Generator.prototype.typeInTable = function (sch, tblName, genericType, skipUserField) {
        var fields = this.getTableFields(sch, tblName, true);
        //console.log([fields,sch,tblName]);
        for (var j = 0; j < fields.length; j++) {
            var field = fields[j];
            if (skipUserField && this.isUserField(field.fieldName))
                continue;
            var gType = field.genericDataType.trim();
            if (gType == genericType)
                return true;
        }
        return false;
    };

    Generator.prototype.tableHasNumericFields = function (sch, tblName, skipUserField) {
        var numFields = this.genericDataTypes.getNumbers();
        for (var i = 0; i < numFields.length; i++) {
            if (this.typeInTable(sch, tblName, numFields[i], skipUserField))
                return true;
        }
        return false;
    };

    Generator.prototype.getNumericFields = function (sch, tblName, skipUserField) {
        var numFields = this.genericDataTypes.getNumbers();
        var fields = this.getTableFields(sch, tblName, true);
        var res = [];
        for (var i = 0; i < fields.length; i++) {
            var type = fields[i].genericDataType.trim();
            if (numFields.indexOf(type) > -1)
                res.push(fields[i]);
        }
        return res;
    };
    Generator.prototype.tableHasDateFields = function (sch, tblName, skipUserField) {
        return this.getDateFields(sch, tblName, skipUserField).length > 0;
    };
    Generator.prototype.getDateFields = function (sch, tblName, skipUserField) {
        var dateFields = this.genericDataTypes.getDates();
        var fields = this.getTableFields(sch, tblName, true);
        var res = [];
        for (var i = 0; i < fields.length; i++) {
            var type = fields[i].genericDataType.trim();
            if (dateFields.indexOf(type) > -1)
                res.push(fields[i]);
        }
        return res;
    };

    return Generator;
})();
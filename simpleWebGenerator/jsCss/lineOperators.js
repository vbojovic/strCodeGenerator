var LineOperators = (function() { 
    var lines = new Array();
    var separator = "";
    LineOperators.prototype.getRowSeparator=function(txt){
        var separators = new Array("\r\n","\n","\r");
        for (var i = 0; i<separators.length; i++){
            var lines = text.split(separators[i]);
            if (lines.length>1) return separators[i];
        }
        return "\r\n";
    }
    
    LineOperators.prototype.loadLines=function(text) {
        if (text === '' || text == null) return;
        this.separator = this.getRowSeparator(text);
        this.lines = text.split(this.separator);
    };
    LineOperators.prototype.repeatString=function(startIndex, endIndex,isLine,isIncluded){
        
    }
    LineOperators.prototype.cutSegment=function(startIndex, endIndex,isLine,isIncluded){
        
    }
    LineOperators.prototype.linesContainingStr=function(strArr, isBegining, isEnding, isCaseSensitive,isIncluded){
        
    }
    
    LineOperators.prototype.capitalizeFirstLetter=function(str, toUpperCase){
        
    }
    LineOperators.prototype.reverseString=function(str){
    }
    LineOperators.prototype.reverseLines=function(lineArr){
    }
    LineOperators.prototype.sortStringArray=function(){
        
    }
    LineOperators.prototype.toText=function(){
        return this.lines.join(this.separator);
    }
    LineOperators.prototype.mergeResultsWith=function(sufix){
        var oldLines = this.lines;
        this.loadLines(sufix);
        var newLines = this.lines;
        this.lines = oldLines;
        this.lines.concat(newLines);
    }
    LineOperators.prototype.clone=function(){
        var newLineOp = new LineOperators();
        newLineOp.lines = new Array();
        newLineOp.lines.concat(this.lines);
        newLineOp.separator = this.separator;
        return  newLineOp();
    }
    LineOperators.prototype.everyNthLine=function(nArr,isIncluded){}
    LineOperators.prototype.columnSelect=function(intColumnNumStart, intColumnNumEnd,isIncluded){}
    
    
    return LineOperators;
})();
//var oper= new LineOperators();
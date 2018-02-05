var FieldRetrieval=(function(){
	var _retrieveUrl;
	var _storeUrl;
	var _sessName;
	var _selectorGroup;
	var _keySelectorValue;
	var _index;
	function FieldRetrieval(retrieveUrl, storeUrl, sessName,selectorGroup){ //sessName , keySelectorValue
		this._storeUrl = storeUrl;
		this._retrieveUrl = retrieveUrl;
		this._sessName = sessName;
		//this._keySelector = keySelector;
		this._selectorGroup = selectorGroup;
		//this._keySelectorValue = keySelectorValue;
		this.setIndex();
	};
	FieldRetrieval.prototype.setIndex = function(){
		var group = $(this._selectorGroup);
		for (var i = 0 ; i< group.length ; i++){
			if ($(group[i]).val()==this._sessName) this._index=i;
		}
		if (this._index == null) this._index = 0;
	};
	FieldRetrieval.prototype.getRowCount = function(){
		return $(selectorGroup).length;
	};
	FieldRetrieval.prototype.getNumberOfNonEmptyFieldsByIndex = function(){
		var data = this.getAllFieldsByIndex();
		var cnt=0;
		for (var i = 0 ; i<data.length ; i++ ){
			if (typeof data[i] === 'undefined' || data[i].val == '' || data[i].val == null){
				cnt++;
			}
		}
		return cnt;
	};
	FieldRetrieval.prototype.getAllFields = function(){
		var data = this.selectItems2array();
		data = data.concat(this.textItems2array());
		data = data.concat(this.hiddenItems2array());
		return data;
	};
	FieldRetrieval.prototype.getAllFieldsByIndex = function(){
		var data = this.selectItems2array();
		data = data.concat(this.textItems2array());
		data = data.concat(this.hiddenItems2array());
		return this.filterDataByIndex(data);
	};


	FieldRetrieval.prototype.selectItems2array = function(){
		var data = [];
		items = jQuery( "select:enabled" );
		for (var i = 0; i < items.length ; i++){
		   var item = items[i];
		   var itemName = $(item).attr("name");   
		   var itemVal=$(item).val();
		   var itemId = $(item).attr('id');
		   data.push({
		   		name: itemName,
		   		val: itemVal,
		   		type: "select",
		   		id: itemId
		   });
		}
		return data;
	};


	FieldRetrieval.prototype.hiddenItems2array = function(){
		var data = [];
		var items = jQuery( "input:hidden:enabled" ); //TODO change this
		for (var i = 0; i < items.length ; i++){
		   var item = items[i];
		   var itemName = $(item).attr("name");   
		   var itemVal=$(item).val();
		   var itemId = $(item).attr('id');
		   data.push({
		   		name: itemName,
		   		val: itemVal,
		   		type: "text",
		   		id: itemId
		   });
		}
		return data;
	};
	FieldRetrieval.prototype.textItems2array = function(){
		var data = [];
		var items = jQuery( "input[type=text]:enabled, not[select]" );
		for (var i = 0; i < items.length ; i++){
		   var item = items[i];
		   var itemName = $(item).attr("name");   
		   var itemVal=$(item).val();
		   var itemId = $(item).attr('id');
		   data.push({
		   		name: itemName,
		   		val: itemVal,
		   		type: "text",
		   		id: itemId
		   });
		}
		return data;
	};


	FieldRetrieval.prototype.filterDataByIndex = function(data) {
		var hmCounter={};
		var filteredData = []; //TODO tu san sta jer triban prebacit sve sta je po indeksu u odgovarajuci niz..
		for (var i = 0; i < data.length ; i++){
			if (typeof hmCounter[data[i].name] === 'undefined') {
				hmCounter[data[i].name] = 0;	
			} else {
				hmCounter[data[i].name]++;
			}
			if (hmCounter[data[i].name] === this._index){
				filteredData.push(data[i]);
			}
		}
		return filteredData;
	};
	FieldRetrieval.prototype.sendToServer = function(byIndex) { //TODO
		var data = (byIndex === true) 
				? this.getAllFieldsByIndex()
				: this.getAllFields();
		//index vucem odozgo
		var postData =   { 
			items : []
			, sessName : this._sessName
		};
		postData.items = JSON.stringify(data);

		var url = this._storeUrl+'?action=store&sessName='+this._sessName+'&';
		var p = $.post(url,postData);
	};
        
        FieldRetrieval.prototype.checkAll=function(){
            var items = $( ":checkbox" );
            for (var i = 0 ; i< items.length ; i++){
              if ( $(items[i]).val()=='1') $(items[i]).attr('checked',true); 
            }            
        };
        
    
	FieldRetrieval.prototype.getFromServerAndAssign = function() {
		var self = this;
		var url = this._retrieveUrl+'?action=retrieve&sessName='+this._sessName+'&';
		var p = $.post(url,function(data){
			var d = { items : JSON.parse(data) };
			self.assignValues(d);
                        self.checkAll();
		});
		//this.AssignValues(data);
	};
        
        /*
	FieldRetrieval.prototype.setValueToItemByIndex=function(items,val){
		if (this._index == null) return;
		for (var i = 0 ; i< items.length ; i++){
			if (i==this._index)  $(items[i]).val(val);
		}
	};
        */
       
	FieldRetrieval.prototype.setValueToItemByIndex=function(items,val){
		if (this._index == null) return;
		for (var i = 0 ; i< items.length ; i++){
                    if (i==this._index)  {
                        if ($(items[i]).is(':checkbox')){
                            $(items[i]).attr('checked', !(val==0 || val=='0'));
                            $(items[i]).val(1);
                        }else {
                            $(items[i]).val(val);
                        }
                    }
		}
	};


	FieldRetrieval.prototype.assignValues = function(data) {
		if (data.items == null) return;
		for (var i = 0 ; i< data.items[0].data.length ; i++){
			var item = data.items[0].data[i];
     	// console.log(item);
			if (item.id != null && item.id != ''){ //assign by id
				$('#'.id).val(item.val);
			}else { //assign by name
				//$("input[name="+item.name+"]").val(item.val);
       // console.log('tucmo');
        //console.log($("input[name='"+item.name+"']"));
				this.setValueToItemByIndex($("input[name='"+item.name+"']"), item.val);
			}
		}
	};


	return FieldRetrieval;
})();

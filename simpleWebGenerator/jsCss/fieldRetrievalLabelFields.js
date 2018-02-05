
FieldRetrieval.prototype.loadKeywordsFromInput  = function() {
	$('input[title="keywords"]').each(function(){
	    var txt = $(this).val();
	    if (typeof txt != 'undefined' && txt != null && txt != ''){
	        $(this).parent().find("a").text(txt);
	    }
	});
};

FieldRetrieval.prototype.loadGenresFromInput  = function() {
	var f=genreFullPath;
	$('input[title="genre_1"],input[title="genre_2"],input[title="genre_3"],input[title="genre_4"],input[title="genre_5"]').each(function(){
	    var txt = $(this).val();
	    if (typeof txt != 'undefined' && txt != null && txt != ''){
	        $(this).parent().find("a").text(f(txt));
	    }
	});
};

FieldRetrieval.prototype.getFromServerAndAssign = function() {
		var self = this;
		var url = this._retrieveUrl+'?action=retrieve&sessName='+this._sessName+'&';
        var ndx = this._index;
		var p = $.post(url,function(data){
			var d = { items : JSON.parse(data) };
			self.assignValues(d);
            self.checkAll();
            self.loadKeywordsFromInput();
            self.loadGenresFromInput();
            //TODO
            //<bug #1096 ian honeyman
            $(('.tracklist tr')[ndx]).not('tr.headerrow').not('tr.needs_validate').addClass('needs_validate');
            //</bug #1096 ian honeyman
		});
		//this.AssignValues(data);
};



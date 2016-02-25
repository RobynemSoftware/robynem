function SmartSearch(settings) {
	
	$this = this;
	$namespace = settings.namespace;
	$chooseLocalityError = settings.chooseLocalityError;
	
	$accepted_google_types = ["locality", "administrative_area_level_2"];
	
	$placeId = null;
	
	this._init = function() {
		$($this._getJqField("btnSearch").attr("disabled", true));
		
		$this._initAutocomplete();
		
		$this._getJqField("btnSearch").click(function() {
			if ($placeId == null) {
				showApplicationMessages($chooseLocalityError);
				//alert(CHOOSE_A_LOCALITY_MESSAGE);
			} else {
				//Valorizza il campo form placeId
				// Effettua la submit della form
			}
		});
		
		$this._getJqField("searchLocation").keydown(function() {
			$placeId = null;
		});
	}
	
	this._initAutocomplete = function() {
		$placeId = null;
		var inputFilter = document.getElementById($namespace + "searchLocation");
		
		var autocomplete = new google.maps.places.Autocomplete(inputFilter);
		
		autocomplete.addListener('place_changed', function() {
		    var place = autocomplete.getPlace();
		    
		    
		    if (place.place_id != undefined && place.types != undefined) {
		    	
		    	var match = false;
		    	
		    	var types = place.types;
		    	for (var i = 0; i < $accepted_google_types.length; i++) {
		    		
		    		for (var j = 0; j < types.length; j++) {
		    			
		    			if (types[j] == $accepted_google_types[i]) {
			    			match = true;
			    			break;
			    		}
		    		}
		    		
		    	}
		    	
		    	if (!match) {
		    		$($this._getJqField("btnSearch").attr("disabled", true));
		    		//alert(CHOOSE_A_LOCALITY_MESSAGE);
					showApplicationMessages($chooseLocalityError);
		    	} else {
		    		$placeId = place.place_id;
		    		$($this._getJqField("btnSearch").attr("disabled", false));
		    	}
		    } else {
		    	$($this._getJqField("btnSearch").attr("disabled", true));
	    		//alert(CHOOSE_A_LOCALITY_MESSAGE);
				showApplicationMessages($chooseLocalityError);
		    }
		    
		    
		  });
	}
	
	this._getFieldId = function(idPart) {
		return "#" + $namespace + idPart;
	}
	
	this._getJqField = function(idPart) {
		return $($this._getFieldId(idPart));
	}
}
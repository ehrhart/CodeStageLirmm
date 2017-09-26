$(document).ready(function () {
	$( function() {
		var handleMin = $( "#custom-handleMin" );
		var handleMax = $( "#custom-handleMax" );
		var filtreMin = $("#filtreIndiceMin")
		var filtreMax = $("#filtreIndiceMax")
		$( "#slider" ).slider({
			range: true,
		    min: 0,
		    max: 100,
			values: [0,100],
			create: function() {
				handleMin.text( $( this ).slider( "values", 0) );
				handleMax.text( $( this ).slider( "values", 1 ) );
				filtreMin.val( $( this ).slider( "values", 0 ) );
				filtreMax.val( $( this ).slider( "values", 1 ) );
			},
			slide: function( event, ui ) {
				handleMin.text( ui.values[ 0 ] );
				handleMax.text(  ui.values[ 1 ] );
				filtreMin.val( ui.values[ 0 ]);
				filtreMax.val( ui.values[ 1 ]);
			}
		});
	});
	
	
});
function selectAll(){
	var x = document.getElementsByClassName("PropertyList");
	for (var i=0; i<x.length; i++){
		x[i].checked = true;
	}
}

function deselectAll(){
	var x = document.getElementsByClassName("PropertyList");
	for (var i=0; i<x.length; i++){
		x[i].checked = false;
	}
}

function showHideDisplay(cb){
	var x = document.getElementById("hiddenDisplay");
	if (cb.checked){
		x.style.display="block";
	}
	else{
		x.style.display="none";
	}
}
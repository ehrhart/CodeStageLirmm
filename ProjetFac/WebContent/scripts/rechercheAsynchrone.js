/**


*/

var req;



function test(){
	mdiv = document.getElementById("results");
	while (mdiv.firstChild) {

		console.log(mdiv.firstChild);
		mdiv.removeChild(mdiv.firstChild);
	}
	if(!document.getElementById("researchTextArea").value.trim()==""){
		req = new XMLHttpRequest();
		req.open('POST', 'SameAsAffichage', true);
		req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		console.log(req);
		req.onreadystatechange = function majIHM() {
			var message = "";

			if (req.readyState == 4) {
				if (req.status == 200) {
					// exploitation des données de la réponse

					if (toString(req.responseText)=="{}"){
						divRow = document.createElement("div");
						divRow.innerHTML="No result for this research";
					}
					
					console.log(req.responseText);
					var jsonRes = JSON.parse(req.responseText);
					console.log(jsonRes);
					
					nbrRep=0;
					document.getElementById("resRec").innerHTML="Research result(s)";
					
					for(var rows in jsonRes){
						nbrRep+=1;
						divRow = document.createElement("div");
						divRow.className="row";
						divCol1=document.createElement("div");
						divCol1.className="col-md-5";
						divCol1.style.height="200px";
						divCol1.style.overflowY="scroll";
						divCol1.style.border="1px solid #000000";
						divCol2=document.createElement("div");
						divCol2.className="col-md-5";
						divCol2.style.height="200px";
						divCol2.style.overflowY="scroll";
						divCol2.style.border="1px solid #000000";
						divCentral=document.createElement("div");
						divCentral.className="col-md-2";
						
						divButton=document.createElement("div");
						divDecision=document.createElement("div");
						divDecision.innerHTML="<b>Decision: </b><br/>"+jsonRes[rows]["Decision"];
						divDecision.style.width="50%";
						divDecision.style.marginLeft="25%";
						divDecision.style.marginTop="30%";
						
						button=document.createElement("input");
						button.className="btnSubmit btn"
						button.type="submit";
						button.value="Select";
						button.name="return"+rows
						button.style.border="none";
						button.style.width="50%";
						button.style.marginLeft="25%";
						button.style.marginTop="5%";
						
						
						divButton.appendChild(button);
						
						divCentral.appendChild(divDecision);
						divCentral.appendChild(divButton);
						
						divRow.appendChild(divCol1);
						divRow.appendChild(divCentral);
						divRow.appendChild(divCol2);
						for(var prop in jsonRes[rows]['List1']){
							divRowAtt = document.createElement("div");
							divRowAtt.className="row";
							divColProp=document.createElement("div");
							divColProp.className="col-md-2";
							divColAtt=document.createElement("div");
							divColAtt.className="col-md-10";
							divRowAtt.appendChild(divColProp);
							divRowAtt.appendChild(divColAtt);
							divColProp.innerHTML = prop;
							divColAtt.innerHTML = jsonRes[rows]["List1"][prop];
							divCol1.appendChild(divRowAtt);
							divCol1.appendChild(document.createElement("br"));
						}
						for(var prop in jsonRes[rows]['List2']){
							divRowAtt = document.createElement("div");
							divRowAtt.className="row";
							divColProp=document.createElement("div");
							divColProp.className="col-md-2";
							divColAtt=document.createElement("div");
							divColAtt.className="col-md-10";
							divRowAtt.appendChild(divColProp);
							divRowAtt.appendChild(divColAtt);
							divColProp.innerHTML = prop;
							divColAtt.innerHTML = jsonRes[rows]["List2"][prop];
							divCol2.appendChild(divRowAtt);
							divCol2.appendChild(document.createElement("br"));
						}
						mdiv.appendChild(divRow);
						mdiv.appendChild(document.createElement("br"));
						mdiv.appendChild(document.createElement("br"));
					}
					document.getElementById("resRec").innerHTML+=": "+nbrRep;
				}
			}
		};
	req.send("researchText="+document.getElementById("researchTextArea").value);
	}
}

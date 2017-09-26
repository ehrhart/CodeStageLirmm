<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page import="beans.StockageOeuvres"%>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SameAs Validation</title>
<link href="css/bootstrap.css" rel="stylesheet">
<link href="css/bootstrap-theme.css" rel="stylesheet">
<link href="css/block.css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="scripts/bootstrap.js"></script>
<script src="scripts/popup.js"></script>
<script src="scripts/messageFinFiltre.js"></script>
<script src="scripts/rechercheAsynchrone.js"></script>
</head>

<body>
	<div id="curseur" class="infobulle"></div>
	
	<div id="affich">
		<div class="panel panel-primary" style="width: 40%; max-width: 40%">
			<div class="panel-heading">BNF</div>
			<div class="panel-body fixed-panel"
				style="height: 500px; overflow-y: scroll; width: 100%;">
				<c:forEach items="${listeProp}" var="propriete">
					<c:set var="propValue" value="${propriete }1" />
					<div id="${propriete }">
						<h4>${requestScope[propriete]}</h4>
						${ !empty(requestScope[propValue]) ? requestScope[propValue] : !empty(requestScope[propriete]) ? " inconnu</br>" : requestScope[propriete] }
					</div>
				</c:forEach>
			</div>
		</div>


		<div id="verti">
			<div id="alin" class="panel panel-info">
				<div class="panel-heading">Map(s) left</div>
				<div class="panel-body" style="padding: 5px">${compteurRestant}/${compteurMax}</div>
				<div class="panel-body" style="padding: 5px">${(compteurRestant/compteurMax)*100}%</div>
			</div>

			<c:if test="${filtrage == true }">
				<div id="alin" class="panel panel-info">
					<div class="panel-heading">Filter size</div>
					<div class="panel-body" style="padding: 5px">${compteurFiltre}/${compteurMax}</div>
					<div class="panel-body" style="padding: 5px">${(compteurFiltre/compteurMax)*100}%</div>
				</div>
			</c:if>

			<div id="alin" class="panel panel-info">
				<div class="panel-heading">Confidence Index</div>
				<div class="panel-body">${ratio }</div>
			</div>

			<!--  A MODIFIER  -->
			<div id="alin" class="panel panel-info">
				<div class="panel-heading">Decision Taken</div>
				<div class="panel-body">${decision[cptNext]}</div>
			</div>
		</div>



		<div class="panel panel-primary" style="width: 40%">
			<div class="panel-heading">PP</div>
			<div class="panel-body fixed-panel"
				style="height: 500px; overflow-y: scroll; width: 100%;">
				<c:forEach items="${listeProp}" var="propriete">
					<c:set var="propValue" value="${propriete }2" />
					<div id="${propriete }">
						<h4>${requestScope[propriete]}</h4>
						${ !empty(requestScope[propValue]) ? requestScope[propValue] : !empty(requestScope[propriete]) ? " inconnu</br>" : requestScope[propriete] }
					</div>
				</c:forEach>
			</div>
		</div>
	</div>

	<form action="SameAsAffichage" method="post" id="recherche">

		<div class="row"
			style="border-bottom: 1px solid #eee; margin-top: 20px">
			<div class="col-md-5" style="text-align: right">
				<input class="btn btnSubmit" type="submit" value="Same"
					style="background-color: #66bb6a;" name="identiques" />
			</div>
			<div class="col-md-2" style="text-align: center">
				<input class="btn btnSubmit" type="submit" value="Undecided"
					style="background-color: #b2eaea;" name="indecis" />
			</div>
			<div class="col-md-5">
				<input class="btn btnSubmit" type="submit" value="Different"
					style="background-color: #ef5350;" name="differents" />
			</div>
		</div>

		<div class="row"
			style="border-bottom: 1px solid #eee; margin-left: 20px">
			<div style="margin-bottom: 20px;">
				<h4>Parameter & research</h4>
			</div>
			<div class="col-md-4" style="border-right: 1px solid #eee;">
				<c:forEach var="i" begin="0" end="${lV }">
					<c:if test="${lastV[i] ge 0}">
						<c:set var = "string01" value= "<b>${requestScope[listeProp[0]]}</b>Oeuvre 1:${so1.getOeuvres()[so1.getListeEntity1()[lastV[i]]][so1.getListeProp()[0]]} <br>Oeuvre 2: ${so1.getOeuvres()[so1.getListeEntity2()[lastV[i]]][so1.getListeProp()[0]]}"></c:set>
						<c:set var = "string02" value="${fn:replace(string01, '###', ', ')}"></c:set>
						<c:set var = "string11" value= "<b>${requestScope[listeProp[1]]}</b>Oeuvre 1:${so1.getOeuvres()[so1.getListeEntity1()[lastV[i]]][so1.getListeProp()[1]]} <br>Oeuvre 2: ${so1.getOeuvres()[so1.getListeEntity2()[lastV[i]]][so1.getListeProp()[1]]}"></c:set>
						<c:set var = "string12" value="${fn:replace(string11, '###', ', ')}"></c:set>
						
						<div onmouseover='montre("${string02}<br><br>${string12 }"); '	onmouseout="cache();" class='progress-bar progress-bar-${!empty(decision[lastV[i]]) ? decision[lastV[i]]: "todefine"}' role='progressbar' style='width: 19%'>
						<input type='submit' value="  " name='return${lastV[i] }'	style='border: none; background: transparent; outline: 0; width: 100%' />
						</div>
						<div class='progress-bar' role='progressbar' style='background-color: black; width: 1%; height:22.222px'></div>
					</c:if>
				</c:forEach>
				
				<c:set var = "string01" value= "<b>${requestScope[listeProp[0]]}</b>Oeuvre 1:${so1.getOeuvres()[so1.getListeEntity1()[cptTemp]][so1.getListeProp()[0]]} <br>Oeuvre 2: ${so1.getOeuvres()[so1.getListeEntity2()[cptTemp]][so1.getListeProp()[0]]}"></c:set>
				<c:set var = "string02" value="${fn:replace(string01, '###', ', ')}"></c:set>
				<c:set var = "string11" value= "<b>${requestScope[listeProp[1]]}</b>Oeuvre 1:${so1.getOeuvres()[so1.getListeEntity1()[cptTemp]][so1.getListeProp()[1]]} <br>Oeuvre 2: ${so1.getOeuvres()[so1.getListeEntity2()[cptTemp]][so1.getListeProp()[1]]}"></c:set>
				<c:set var = "string12" value="${fn:replace(string11, '###', ', ')}"></c:set>
				
				<div
					onmouseover='montre("${string02}<br><br>${string12 }"); '
					onmouseout="cache();"
					class='progress-bar progress-bar-${!empty(decision[cptTemp]) ? decision[cptTemp]: "todefine"}'
					role='progressbar' style='width: 17.5%'>
					<input type='submit' value='current' name='return${cptTemp }'
						style='border: none; background: transparent; outline: 0; width: 100%' />
				</div>
			</div>

			<div class="col-md-6"
				style="margin-top: 20px; border-right: 1px solid #eee;">
				<div class="col-md-9">
					<textarea style="width:100%;height:100%" name="researchTextArea" id="researchTextArea" form="recherche">${recherche }</textarea>
				</div>
				<div class="col-md-3">
					<button type="button" onclick="test();" data-toggle="modal"
						data-target="#myResearch" id="researchButton"
						name="researchButton">Research</button>
					<!-- <input class="btn btnSubmit" type="submit" value="Rechercher"
						style="background-color: #66bb6a;" name="envoyer" form="recherche" /> -->
				</div>
				<div id="myResearch" class="modal fade" role="dialog" style="margin-left:20px">
					<div class="modal-content">
					<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal">&times;</button>
								<h4 class="modal-title" id="resRec"></h4>
							</div>
					<div class="modal-body" id="results" style="overflow-y: scroll;">
					
					</div>
					</div>
				</div>
				<!-- <div>
					<input type="checkbox" id="rechCB" value="checkbox17" name="rechCB"
						form="recherche" /> Chercher depuis le début?
				</div> -->
			</div>

			<div class="col-md-2" style="margin-top: 20px;">
				<button type="button" class="btn btn-info btn-lg"
					data-toggle="modal" data-target="#myModal">Parameters</button>


				<div id="myModal" class="modal fade" role="dialog">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal">&times;</button>
								<h4 class="modal-title">Parameters</h4>
							</div>

							<div class="modal-body">
								<h4>Display</h4>
								<c:forEach items="${listeProp}" var="propriete">
									<label> <input style="margin-left: 50px;"
										type="checkbox" id="CB${propriete }" value="checkbox1"
										name="CB${propriete }" ${checkedServ[propriete] }>
										Display ${propriete }
									</label>
									<br>
								</c:forEach>
							</div>

							<div class="modal-footer">
								<span><input class="btn btnSubmit" type="submit"
									value="Update display" name="maj" /></span>
							</div>
						</div>
					</div>
				</div>
			</div>



		</div>
		<!--     PARAMETRES    -->
		<div class="row" style="margin-left: 20px">
			<h4>Save</h4>

			<div class="col-md-12" style="margin-top: 20px; text-align: center">
				<button type="button" class="btn btn-info btn-lg"
					data-toggle="modal" data-target="#mySave">Save</button>

				<div id="mySave" class="modal fade" role="dialog">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<h4>Give your file a name (a-Z 0-9 accepted / no space allowed)</h4> <!--Trouver comment imposer les bornes-->
								<textarea name="nomFichier"></textarea>
								<button type="button" class="close" data-dismiss="modal">&times;</button>
							</div>

							<div class="modal-footer">
								<span><input class="btn btnSubmit" type="submit"
									value="save" name="save" /></span>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- <input class="btn btnSubmit" type="submit" value="Save"
					style="background-color: #ef5350;" name="save" /> -->
		</div>
	</form>
	<footer>
		<%@include file="Footer.jsp"%>
	</footer>
</body>
</html>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page import="beans.StockageOeuvres"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />

<title>SameAs Properties</title>
<link href="css/bootstrap.css" rel="stylesheet">
<link href="css/bootstrap-theme.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="css/DoubleSlide.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script src="scripts/double-slide.js"></script>
  <script src="scripts/SaPselect.js"></script>

</head>
<body>
	<form action="SameAsAffichage" method="post"  id="recherche">
		<div class="panel panel-info" style="width: 30%; margin: auto;">
			<div class="panel-heading">Parameters</div>
			<br />
			<div class="panel-body">
				<h4 class="col-md-9" style="border-right: 1px solid #eee;">Display</h4>
				<h5 class="col-md-3">Frequency</h5>
				<c:set var="CBchecked" value="checked" />
				<c:set var="i" value="0" />
				<c:forEach items="${listeProp}" var="propriete">
					<div class="col-md-9">
						<label> <input class="PropertyList"
							style="margin-left: 50px;" type="checkbox" id="CB${propriete }"
							value="checkbox1" name="CB${propriete }" ${CBchecked }>${propriete }
						</label> <br>
						<c:set var="i" value="${i+1 }" />
						<c:if test="${i ge 5 }">
							<c:set var="CBchecked" value="" />
						</c:if>

					</div>
					<div class="col-md-3">${statProp[propriete] }%</div>
				</c:forEach>
				<div class="col-md-6" style="text-align: center; margin-top: 20px">
					<button type="button" onclick="selectAll();">Select All</button>
				</div>
				<div class="col-md-6" style="text-align: center; margin-top: 20px">
					<button type="button" onclick="deselectAll();">Deselect
						All</button>
				</div>
			</div>
			<br />
			<div class="panel-heading">Filtering</div>
			<div class="panel-body" style="border-bottom: 1px solid #eee">
				<textarea name="filtre" style='width: 100%;'
					placeholder='Leave blank to not filter'></textarea>
			</div>
			<br>
			<div>
				<h4>
					Select the range of confidence you want to cover <br>(Handles'
					values are included)
				</h4>
				<div id="slider">
					<div id="custom-handleMin" class="ui-slider-handle"></div>
					<div id="custom-handleMax" class="ui-slider-handle"></div>
					<input type="hidden" value="" id="filtreIndiceMin"
						name="filtreIndiceMin" /> <input type="hidden" value=""
						id="filtreIndiceMax" name="filtreIndiceMax" />
				</div>
			</div>
			<br>
			<div>
				<label> <input style="margin-left: 50px;" type="checkbox"
					id="redisplay" value="checkboxRd" onclick="showHideDisplay(this)" name="redisplay">Affiche
					couples deja traités
				</label>
			</div>
			<div id="hiddenDisplay" style="display: none">
				<label> <input style="margin-left: 50px;" type="radio"
					id="redisplaySame" value="radioS" name="redisplayRadio">Same
				</label> <label> <input style="margin-left: 50px;" type="radio"
					id="redisplayDifferent" value="radioD" name="redisplayRadio">Different
				</label>
				</label> <label> <input style="margin-left: 50px;" type="radio"
					id="redisplayBoth" value="radioB" name="redisplayRadio" checked="checked">Both
				</label>
			</div>
		</div>
		<br>
		<div style="text-align: center; margin: auto;">
			<input type="submit" value="Validate" class="btn btn-primary" />
		</div>

	</form>
	<footer>
		<%@include file="Footer.jsp"%>
	</footer>
</body>
</html>
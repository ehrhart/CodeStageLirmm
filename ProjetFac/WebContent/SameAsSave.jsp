<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="beans.StockageOeuvres"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="css/bootstrap.css" rel="stylesheet">
<link href="css/bootstrap-theme.css" rel="stylesheet">
<link href="css/block.css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="scripts/bootstrap.js"></script>
</head> 
<body>

	<form action="SameAsAffichage" method="post"
		enctype="multipart/form-data">

		<div style="visibility: hidden">
			<c:forEach items="${listeProp}" var="propriete">
				<label> <input style="margin-left: 50px;" type="checkbox"
					id="CB${propriete }" value="checkbox1" name="CB${propriete }"
					${checkedServ[propriete] }></label>
				<br>
			</c:forEach>
		</div>

	</form>
	<form action="SameAsSave" method="get">
		<div class="panel panel-primary" style="width:40%;">
			<div class="panel-heading">Enregistrer</div>
			<div class="panel-body">Click here to save <input type="submit" class="btn btn-primary" id="enregistrer"/></div>
		</div>

	</form>
	<footer>
		<%@include file="Footer.jsp"%>
	</footer>
</body>
</html>

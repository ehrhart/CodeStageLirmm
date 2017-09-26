<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<!DOCTYPE html>
<html lang="en" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta charset="utf-8" />
          
			<title>SameAs Validator</title>
	<link href="css/bootstrap.css" rel="stylesheet">
    	<link href="css/bootstrap-theme.css" rel="stylesheet">
        <link type="text/css" rel="stylesheet" href="css/cssTPOCR.css">
    </head>
    <body>
    
    <!-- <h2>${reussi }</h2> -->
    
        <form action="SameAsValidator" method="post" enctype="multipart/form-data" id="recherche">
            <br /><br />
			<div class="panel panel-primary" style="width:30%; margin: auto;">
                <div class="panel-heading">Select alignment file</div><br /><br />                
					<div class="panel-body">
         		       <label for="fichier" style="margin-left:50px;">File Location: </label><br />
						<input class="btn btn-info" type="file" id="fichier" name="fichier" value ="Select a file" accept=".rdf, .xml, .ttl" style="margin-left:50px;" required/>
	               	 	<span class="succes"><c:out value="${fichier}" /></span>
					</div>
	                <br /><br />                
					</div>
            <br />
			<br />

			<div style="text-align: center; margin: auto;">
			<input type="submit" value="Validate" class="btn btn-primary" />
		</div>
        </form>
        <footer>
		<%@include file="Footer.jsp"%>
	</footer>
    </body>
</html>

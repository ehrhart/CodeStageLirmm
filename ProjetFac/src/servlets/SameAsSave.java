package servlets;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import beans.StockageOeuvres;
import methods.ExportResult;

@SuppressWarnings("serial")
public class SameAsSave extends HttpServlet {

	HashMap<String,String> checkedBox;
	ArrayList<String> aEcrire;
	ExportResult ER;
	StockageOeuvres so1=new StockageOeuvres();

	//Ceci ne sert que quand TOUTES les oeuvres ont été traitées
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		aEcrire= new ArrayList<>();
		aEcrire=StockageOeuvres.getListeTripletResultat();

		try {
			ER=new ExportResult();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//celui-ci correspond a un fichier .txt qui contient toutes les "oeuvre1, sameAs, oevure2" validées
		PrintWriter sortie = new PrintWriter(new BufferedWriter(new FileWriter(System.getProperty("java.io.tmpdir") + File.separator + "resultat.txt")));

		for(int i = 0; i < aEcrire.size(); i++){
			if(aEcrire.get(i)!=null){
				sortie.println(aEcrire.get(i).toString());
			}
			try {
				ER.addInfoXMLDoc(ER.doc, so1.getListeEntity1().get(i), so1.getListeEntity2().get(i) , so1.getRate().get(i), so1.getDecision().get(i));
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//celui-ci est le fichier RDF/XML
		try {
			ER.outputXMLtoFile(ER.doc, System.getProperty("java.io.tmpdir") + File.separator + "resultat.xml");
		} catch (TransformerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		sortie.close();

		request.setAttribute("reussi", "Fichier sauvegardé avec succés");

		// Export file for download
		String exportFileName = "resultat.xml";
		String contextPath = this.getServletContext().getRealPath(File.separator);
		File file = new File(System.getProperty("java.io.tmpdir") + File.separator + exportFileName);
		response.setContentType("application/xml");
		response.addHeader("Content-Disposition", "attachment; filename=" + exportFileName);
		response.setContentLength((int) file.length());
		FileInputStream fileInputStream = new FileInputStream(file);
		OutputStream responseOutputStream = response.getOutputStream();
		int bytes;
		while ((bytes = fileInputStream.read()) != -1) {
			responseOutputStream.write(bytes);
		}
	}

	@SuppressWarnings("unchecked")
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{

		checkedBox= (HashMap<String, String>) request.getAttribute("checkServ");
		so1=(StockageOeuvres) request.getAttribute("so1");

		request.setAttribute("checkServ", checkedBox);
		request.setAttribute("so1", so1);
		request.setAttribute("listeProp", StockageOeuvres.getListeProp());

		this.getServletContext().getRequestDispatcher( "/SameAsSave.jsp" ).forward( request, response );
	}
}

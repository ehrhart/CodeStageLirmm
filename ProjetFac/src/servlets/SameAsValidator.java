package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import beans.StockageOeuvres;
import methods.AR;

@SuppressWarnings("serial")
@MultipartConfig
public class SameAsValidator extends HttpServlet {
	
	int[] lastVisited=new int[4];
	
	public static final String CHAMP_FICHIER = "fichier";
	boolean presenceDecision=false;
	
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		
		this.getServletContext().getRequestDispatcher( "/SameAsValidator.jsp" ).forward( request, response );
	}
	
	
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		
		Part part; //Pour r�cuperer des fichier via doPost, il faut un formulaire encod� en Multipart: donc on r�cup�re des part.
		Model srcRDF; //On utilise la librairie Jena pour acc�der � un fichier de type RDF: on stocke le fichier dans un model
		StmtIterator iterRDF; //Que l'on va parcourir gr�ce � un it�rateur
		StockageOeuvres.statProp = new HashMap<>();
		
		part = request.getPart( CHAMP_FICHIER ); 
		
		srcRDF = ModelFactory.createDefaultModel(); //A la mani�re d'un DocumentBuilderFactory..
		
		try {
			srcRDF.read(part.getInputStream(),"RDF"); //On lit le fichier stock� dans part, en tant que fichier RDF/XML => on en sort donc une liste de triplet
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		iterRDF = srcRDF.listStatements(); //On va it�rer sur l'ensemble des triplets
		
		StockageOeuvres so1=new StockageOeuvres();
		StockageOeuvres.setScoreProp(new HashMap<>());
		StockageOeuvres.setListeProp(new ArrayList<>());
		StockageOeuvres.setListeTripletResultat(new ArrayList<>());
		StockageOeuvres.compteur=0;
		StockageOeuvres.compteurRestant=0;
		
		while (iterRDF.hasNext()) { //!\\ Pas s�r que tout soit utile ici, � revoir plus tard. Ne fonctionne que sur du format EDOAL.
			Statement stmt = iterRDF.nextStatement();
			
			while(!stmt.getPredicate().toString().contains("Cell")){
				Resource pred = stmt.getPredicate();
				RDFNode obj = stmt.getObject();
				

				if (pred.toString().contains("decision")){
					so1.getDecision().add(obj.toString());
					presenceDecision=true;
					if (!obj.toString().equals("todefine")){
						StockageOeuvres.compteurRestant+=1;
					}
				}
				
				if (pred.toString().contains("entity2")){
					so1.getListeEntity2().add(obj.toString());
					so1.getOeuvres().put(obj.toString(),AR.getAttributes(obj.toString()));
				}

				if (pred.toString().contains("entity1")){
					StockageOeuvres.compteur+=1; //A chaque Entity1 est associ� un et un seul Entity2 donc => nombre de couple +1
					if(!presenceDecision){    //A chaque Entity1 correspond une et une seule decision => si aucune decision n'a �t� trouv� avant dans la cell: +"todefine"
						so1.getDecision().add("todefine");
					}
					presenceDecision=false;
					StockageOeuvres.getListeTripletResultat().add(null); //Les listes ont d�j� la bonne taille => simplifie le travail d'ajout apr�s : on n'a pas besoin de v�rifi� que la liste est vide ou non lors d'un retrait/ajout
					so1.getListeEntity1().add(obj.toString());
					so1.getOeuvres().put(obj.toString(),AR.getAttributes(obj.toString()));
				}

				if (pred.toString().contains("measure")){
					so1.getRate().add(obj.toString().substring(0,3)); //� changer par un RegEx plus tard pour ne prendre que les chiffres: Voir dans la m�thode AR pour la RegEx
				}
				
				if(iterRDF.hasNext()){
					stmt = iterRDF.nextStatement();					
				}
				else{
					break;
				}
			}
		}
		
		/*DISTRIBUTION DES PROPRIETES DANS L"ARRAYLIST EN FONCTION DU SCORE*/
		//Lors de la recherche des propri�t�s des oeuvres, pour chaque apparition d'une propri�t� donn�e,
		//celle-ci gagnait +1 point. Ces propri�t�s �tant rang� dans une HashMap (non ordonn�e), on les range ici
		//dans une ArrayList ordonn�e par ordre de points		
		double size = so1.getListeEntity1().size();
		
		
		for(String s : StockageOeuvres.getScoreProp().keySet()){
			StockageOeuvres.statProp.put(s, (StockageOeuvres.getScoreProp().get(s)/size)*50);
			
			if(StockageOeuvres.getListeProp().size()==0){
				StockageOeuvres.getListeProp().add(s);
			}
			else{
				int j=0;
				while(j<StockageOeuvres.getListeProp().size() && StockageOeuvres.getScoreProp().get(StockageOeuvres.getListeProp().get(j))>StockageOeuvres.getScoreProp().get(s)){
						j++;
				}
				if (j<StockageOeuvres.getListeProp().size()){
					StockageOeuvres.getListeProp().add(j,s);
				}
				else{
					StockageOeuvres.getListeProp().add(s);
				}
			}
		}
		
		for (int i=0; i<4; i++){
			lastVisited[i]=-1;
		}
		
		StockageOeuvres.compteurRestant=StockageOeuvres.compteur-StockageOeuvres.compteurRestant;
		StockageOeuvres.compteurNext=-99; //Init pour premiere arriv�e dans SameAsAffichage.java
		
		request.setAttribute("oeuvres", so1);
		request.setAttribute("lastVisited", lastVisited);
		request.setAttribute("statProp", StockageOeuvres.statProp);
		request.setAttribute("sameAsProp", true); //Pour passer par l'�cran de param�trage apr�s la validation
		request.setAttribute("listeProp", StockageOeuvres.getListeProp()); //Raccourci d'acc�s � la liste pour SameAsPropriete.jsp
		
		this.getServletContext().getRequestDispatcher( "/SameAsAffichage" ).forward( request, response );
		
	}
}
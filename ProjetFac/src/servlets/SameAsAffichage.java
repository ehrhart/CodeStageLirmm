package servlets;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import beans.StockageOeuvres;
import methods.ExportResult;
import methods.Research;

@SuppressWarnings("serial")
@MultipartConfig

public class SameAsAffichage extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	StockageOeuvres so1; // TOUTE LA RESSOURCE
	StockageOeuvres sof=new StockageOeuvres(); //RESSOURCE FILTRAGE
	StockageOeuvres sor=new StockageOeuvres(); //RESSOURCE RECHERCHE
	StockageOeuvres sou; //RESSOURCE UTILISEE
	HashMap<String,String> checkedBox;

	Enumeration<String> names;

	String URI1;
	String URI2;
	int start;
	int numeroOeuvre=-1;
	int numeroOeuvreTemp=0;

	int[] lastVisited=new int[4];
	boolean verifList=true;

	public final String CHAMP_ID="identiques";
	public final String CHAMP_DIF="differents";
	public final String CHAMP_IND="indecis";

	public final String CHAMP_MAJ="maj";
	public final String CHAMP_RETURN="return";
	public final String CHAMP_SAUVEGARDE="save";

	public final String CHAMP_RECHERCHE="recherche";
	public final String CHAMP_FILTRE="filtre";
	public String[] INNERHTML_RECHERCHE;
	public String[] INNERHTML_FILTRE;

	public final String messageFinFiltrage="Filtre entièrement traité: fin du filtrage";

	double filtreIndiceMin;
	double filtreIndiceMax;

	//MODES
	boolean modeFiltrage=false;
	boolean modeRetour=false;
	boolean modeRecherche=false;
	boolean modeRedisplay=false;
	String redisplay="";
	boolean initFiltrage;

	//ECRITURE
	ExportResult ER;

	//Recherche
	String resultat="{";
	String valeur="";

	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{

		/*SAVE WORK IN PROGRESS*/
		if (request.getParameter(CHAMP_SAUVEGARDE)!=null){
			String nomFichier=request.getParameter("nomFichier");
			for(int i = 0; i <so1.getDecision().size(); i++){
				try {
					ER.addInfoXMLDoc(ER.doc, so1.getListeEntity1().get(i), so1.getListeEntity2().get(i) , so1.getRate().get(i), so1.getDecision().get(i));
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//sauvegarde au format RDF/XML pour pouvoir relire le fichier plus tard et continuer le travail en cours
			try {
				ER.outputXMLtoFile(ER.doc, System.getProperty("java.io.tmpdir") + File.separator + nomFichier + ".xml");
			} catch (TransformerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		/*INITIALISATION*/
		if(StockageOeuvres.compteurNext==-99 && request.getParameter(CHAMP_MAJ)==null){
			//recuperer notre bean StockageOeuvres complet
			so1=(StockageOeuvres) request.getAttribute("oeuvres");

			//Init Compteurs.
			StockageOeuvres.compteurNext=0;
			StockageOeuvres.compteurTemp=0;

			//init test
			initFiltrage=true;

			//lastVisited permet de mettre a jour la "progress bar" affichant les 4 derniere oevures visitée + celle en cours
			lastVisited=(int[]) request.getAttribute("lastVisited");
			numeroOeuvre=-1;
			numeroOeuvreTemp=0;


			//initEcriture
			try {
				ER= new ExportResult();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//initRedisplay
			redisplay= "todefine";
		}

		/*Apres avoir selectionné le fichier d'alignement => page des parametres. Ce passage ne se fera qu'une fois au cours de l'execution*/
		if (request.getAttribute("sameAsProp") != null){
			this.getServletContext().getRequestDispatcher( "/SameAsPropriete.jsp" ).forward( request, response );
		}



		/* Apres la page des parametre => page d'affichage*/
		else{
			//Premierement nous allons traiter les différents parametrages..
			//le redisplay des oeuvres deja traitee. Se fait grace a une chaine de caractere.
			//si la decision de l'oeuvre est contenu dans la String alors l'oeuvre sera affichée.
			if (request.getParameter("redisplay")!= null){
				if (request.getParameter("redisplayRadio").equals("radioS")||request.getParameter("redisplayRadio").equals("radioB")){
					redisplay+="same";
				}
				if (request.getParameter("redisplayRadio").equals("radioD")||request.getParameter("redisplayRadio").equals("radioB")){
					redisplay+="different";
				}
			}

			/*FILTRAGE*/
			if(initFiltrage){
				sou=new StockageOeuvres();
				filtreIndiceMin = Double.parseDouble(request.getParameter("filtreIndiceMin"));
				filtreIndiceMax = Double.parseDouble(request.getParameter("filtreIndiceMax"));
				if(!request.getParameter(CHAMP_FILTRE).trim().equals("") || (filtreIndiceMax != 100 || filtreIndiceMin !=0)){ //SI LE CHAMP FILTRE N'EST PAS VIDE
					INNERHTML_FILTRE = request.getParameter(CHAMP_FILTRE).trim().split(" ");
					sof=Research.filtrage(so1,INNERHTML_FILTRE,filtreIndiceMax,filtreIndiceMin);
					sou=sof;
				}
				if (sou.getListeEntity1().size() == 0){ //SI LE CHAMP FILTRE N'EST PAS VIDE MAIS QU'AUCUNE CORRESPONDANCE N'A ETE TROUVEE
					sof=so1;
					sou=so1;
				}
				StockageOeuvres.compteurNext=0;
				StockageOeuvres.compteurTemp=0;
				initFiltrage=false;

				if (!sou.equals(so1)){ // SI LA RESSOURCE UTILISE N'EST PAS LA RESSOURCE TOTALE => ON PASSE EN MODE FILTRAGE
					modeFiltrage=true;
				}
			}

			/*RECHERCHE*/
			//la recherche etant une fonction dynamique du programme, une grande partie du code ci dessous sert a
			//ecrire le resultat en JSON pour le script js
			if (request.getParameter("researchText")!=null){

				modeRecherche=true;
				resultat = "{";
				valeur = request.getParameter("researchText");
				if(!valeur.trim().equals("")){
					INNERHTML_FILTRE = valeur.trim().split(" ");
					sor=Research.recherche(sou,so1, INNERHTML_FILTRE);
				}
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setHeader("Cache-Control", "no-cache");

				if(sor.getListeEntity1().size()>0){
					/*CONSTRUCTION JSON : {"n°oeuvre" : { "n°List" : { "prop1":"val1" , "prop2":"val2"}}.. }*/
					for ( int i = 0 ; i< sor.getListeEntity1().size();i ++){
						String URIl1=sor.getListeEntity1().get(i);
						String URIl2=sor.getListeEntity2().get(i);
						resultat+= "\""+so1.getListeEntity1().indexOf(URIl1)+"\" :{";
						resultat+=" \"List1\": {";
						for (String prop : StockageOeuvres.getListeProp()){
							if (checkedBox.keySet().contains(prop)){
								if (so1.getOeuvres().get(URIl1).containsKey(prop)){
									resultat += "\""+prop+"\" :";
									resultat += "\"" + so1.getOeuvres().get(URIl1).get(prop).replaceAll("###", "</br>")+ "\",";
								}
							}
						}
						resultat=resultat.substring(0, resultat.length()-1);
						resultat+="},\"List2\": {";
						for (String prop : StockageOeuvres.getListeProp()){
							if (checkedBox.keySet().contains(prop)){
								if (so1.getOeuvres().get(URIl2).containsKey(prop)){
									resultat += "\""+prop+"\" :";
									resultat += "\"" + so1.getOeuvres().get(URIl2).get(prop).replaceAll("###", "</br>")+ "\",";
								}
							}
						}
						resultat=resultat.substring(0, resultat.length()-1);
						resultat+="},\"Decision\": ["+"\"" +so1.getDecision().get(so1.getListeEntity1().indexOf(URIl1))+"\"" ;
						resultat+="]},";
					}
					resultat=resultat.substring(0, resultat.length()-1);
					resultat+="}";
				}

				else {
					resultat="{}";
				}

				response.getWriter().write(resultat);
				/*if(!request.getParameter(CHAMP_RECHERCHE).trim().equals("")){
					INNERHTML_FILTRE = request.getParameter(CHAMP_RECHERCHE).trim().split(" ");
					sor=Research.recherche(sou, INNERHTML_FILTRE);
					for(int i=0; i < sor.getListeEntity1().size();i++){

					}
					//SAUVEGARDER AFFICHAGE COMBO BOX
					this.getServletContext().getRequestDispatcher( "/ResultatRecherche.jsp" ).forward( request, response );
				}*/
			}
			else{
				//Si nous ne sommes ni en recherche, ni en filtrage.
				/*INITCHECKBOX*/
				//Sert a garder en memoire la configuration d'affichage. A chaque tour de boucle on reinitialise la liste des checkbox.
				//Puis on verifie lesquels ont été cochees avant de valider (same/different/undecided). Celles-ci seront ajoutees dans la HashMap
				//Cette HashMap nous dira quelles info afficher pour ce tour de boucle.
				checkedBox=new HashMap<String,String>();
				names = request.getParameterNames();
				while (names.hasMoreElements()) {
					String name = names.nextElement();
					if (name.contains("CB")){
						String s=name.substring(2);
						checkedBox.put(s,"checked");
					}
				}

				//TROUVER UN MOYEN DE REGROUPER TOUT CA
				//lorsque qu'on fini le filtre et qu'on passe en mode normal, le programme passe 2 fois par ce scriptlet.
				//et donc enregistre 2 fois d'affiler les infos de validation. pour eviter cela => la variable finFiltrage
				if(request.getAttribute("finFiltrage")==null){
					if(request.getParameter(CHAMP_ID)!=null){
						//si l'oeuvre etait "todefine", alors il reste une oeuvre de moins a traiter apres traitement.
						//sinon c'est juste un changement de decision et il reste autant d'oeuvres a traiter
						if (so1.getDecision().get(numeroOeuvre).equals("todefine")){
							StockageOeuvres.compteurRestant-=1;
						}
						//on change la valeur des decisions
						so1.getDecision().remove(numeroOeuvre);
						so1.getDecision().add(numeroOeuvre,"same" );
						StockageOeuvres.getListeTripletResultat().remove(numeroOeuvre);
						StockageOeuvres.getListeTripletResultat().add(numeroOeuvre,"("+URI1+",sameAS,"+URI2+")");
						//si nous etions sur la derniere oeuvre en cours => on passe a l'oeuvre non traitée suivante
						if(StockageOeuvres.compteurNext==StockageOeuvres.compteurTemp){
							StockageOeuvres.compteurNext+=1;
							StockageOeuvres.compteurTemp+=1;
						}
						//sinon on retourne sur la derniere oeuvre en cours.
						else{
							StockageOeuvres.compteurNext=StockageOeuvres.compteurTemp;
						}
						modeRetour=false;
						modeRecherche=false;
					}
					//fonctionne pareil qu'au dessus
					if(request.getParameter(CHAMP_DIF)!=null){
						if (so1.getDecision().get(numeroOeuvre).equals("todefine")){
							StockageOeuvres.compteurRestant-=1;
						}
						so1.getDecision().remove(numeroOeuvre);
						so1.getDecision().add(numeroOeuvre,"different" );
						StockageOeuvres.getListeTripletResultat().remove(numeroOeuvre);
						StockageOeuvres.getListeTripletResultat().add(numeroOeuvre,null);
						if(StockageOeuvres.compteurNext==StockageOeuvres.compteurTemp){
							StockageOeuvres.compteurNext+=1;
							StockageOeuvres.compteurTemp+=1;
						}
						else{
							StockageOeuvres.compteurNext=StockageOeuvres.compteurTemp;
						}
						modeRetour=false;
						modeRecherche=false;
					}
					//fonctionne pareil qu'au dessus
					if(request.getParameter(CHAMP_IND)!=null){
						if (so1.getDecision().get(numeroOeuvre).equals("todefine")){
							StockageOeuvres.compteurRestant-=1;
						}
						so1.getDecision().remove(numeroOeuvre);
						so1.getDecision().add(numeroOeuvre,"todefine" );
						StockageOeuvres.getListeTripletResultat().remove(numeroOeuvre);
						StockageOeuvres.getListeTripletResultat().add(numeroOeuvre,null);
						if(StockageOeuvres.compteurNext==StockageOeuvres.compteurTemp){
							StockageOeuvres.compteurNext+=1;
							StockageOeuvres.compteurTemp+=1;
						}
						else{
							StockageOeuvres.compteurNext=StockageOeuvres.compteurTemp;
						}
						modeRetour=false;
						modeRecherche=false;
					}
				}
				/*if ((StockageOeuvres.compteurNext!=StockageOeuvres.compteurTemp || StockageOeuvres.compteurNext>0) && INNERHTML_RECHERCHE==null){ // /!\ Evaluation passive
				for (String s: so1.getListeProp()){
					if (so1.getOeuvres().get(URI1).containsKey(s))
				}

				if(request.getParameter("rechCB")!=null || StockageOeuvres.compteurNext==StockageOeuvres.compteurTemp){
					StockageOeuvres.compteurNext=0;
				}
				else{
					StockageOeuvres.compteurNext+=1;
				}

				while (StockageOeuvres.compteurNext<=StockageOeuvres.compteurTemp && !sp1.getCompositeur().get(so1.getListeEntity1().get(StockageOeuvres.compteurNext)).toLowerCase().contains(INNERHTML_RECHERCHE.toLowerCase())&& !sp2.getCompositeur().get(so1.getListeEntity2().get(StockageOeuvres.compteurNext)).toLowerCase().contains(INNERHTML_RECHERCHE.toLowerCase())){

					StockageOeuvres.compteurNext+=1;
				}

				if(StockageOeuvres.compteurNext>=StockageOeuvres.compteurTemp){
					StockageOeuvres.compteurNext=StockageOeuvres.compteurTemp;
					INNERHTML_RECHERCHE=null;
					//AFFICHER UN MESSAGE DE NON TROUVAILLE
				}
			}*/

				/*Pour retourner a une oeuvre precedente via la progress bar*/

				names = request.getParameterNames();

				while (names.hasMoreElements()) {
					String name = names.nextElement();
					if (name.contains(CHAMP_RETURN)){
						int j = Integer.parseInt(name.substring(6));
						j=sou.getListeEntity1().indexOf(so1.getListeEntity1().get(j));
						StockageOeuvres.compteurNext=j;
						if(!modeRecherche){
							modeRetour=true;
						}
					}
				}

				/*ArrayList<Part> parts = new ArrayList<>(request.getParameters());

			for (int i=0; i<parts.size(); i++){
				if (parts.get(i).getName().contains(CHAMP_RETURN)){
					int j = Integer.parseInt(parts.get(i).getName().substring(6));
					StockageOeuvres.compteurNext=j;
					modeRetour=true;
				}
			}*/

				/*HAVING A BAR THAT DISPLAYS ONLY THE 5 LAST PARTS*/

				/*if (StockageOeuvres.compteurTemp<5){
				start=0;
			}
			else{
				start=StockageOeuvres.compteurTemp-4;

			}*/

				if((!modeRetour && !modeRecherche) && request.getParameter(CHAMP_MAJ)==null){

					//Cette partie sert a ne mettre la progress bar a jour que si le couple n'est deja pas dans la progress bar
					verifList=true;
					for(int i=0;i<4;i++){
						if (lastVisited[i]==numeroOeuvre){
							verifList=false;
						}
					}

					if(verifList){
						for(int i=0;i<3;i++){
							lastVisited[i]=lastVisited[i+1];
						}
						lastVisited[3]=numeroOeuvre;
					}

					/*DO NOT DISPLAY A DECIDED COUPLE*/
					/*Il faut mettre a jour la valeur de numeroOeuvre avant de verifier s'il l'oeuvre a deja été validée ou non*/
					/*Attention a ce que numeroOeuvre ne dépasse pas la taille de sou*/
					if(StockageOeuvres.compteurNext<sou.getListeEntity1().size()){
						numeroOeuvre = so1.getListeEntity1().indexOf(sou.getListeEntity1().get(StockageOeuvres.compteurNext));
					}
					while (StockageOeuvres.compteurTemp<sou.getListeEntity1().size()){ //!\\ EVALUTATION PASSIVE
						if (!redisplay.contains(so1.getDecision().get(numeroOeuvre))){
						StockageOeuvres.compteurNext+=1;
						StockageOeuvres.compteurTemp+=1;
						if(StockageOeuvres.compteurNext<sou.getListeEntity1().size()){
							numeroOeuvre = so1.getListeEntity1().indexOf(sou.getListeEntity1().get(StockageOeuvres.compteurNext));
							numeroOeuvreTemp=so1.getListeEntity1().indexOf(sou.getListeEntity1().get(StockageOeuvres.compteurTemp));
						}
						}
						else{break;}
					}
				}
				/*DISPLAY CURRENT SET (CURRENT = StockageOeuvres.compteurNext)*/

				if(StockageOeuvres.compteurNext<sou.getListeEntity1().size()){
					//Reinitialisation checkBox a chaque pour eviter les interferences.
					URI1=sou.getListeEntity1().get(StockageOeuvres.compteurNext);
					URI2=sou.getListeEntity2().get(StockageOeuvres.compteurNext);
					numeroOeuvre=so1.getListeEntity1().indexOf(URI1);

					numeroOeuvreTemp=so1.getListeEntity1().indexOf(sou.getListeEntity1().get(StockageOeuvres.compteurTemp));

					for (String s : checkedBox.keySet()){
						request.setAttribute(s, ""+s.substring(0,1).toUpperCase()+s.substring(1)+": </br></t>");
						if (so1.getOeuvres().get(URI1).containsKey(s)){
							request.setAttribute(s+"1",so1.getOeuvres().get(URI1).get(s).replaceAll("###", "</br>"));
						}
						if (so1.getOeuvres().get(URI2).containsKey(s)){
							request.setAttribute(s+"2",so1.getOeuvres().get(URI2).get(s).replaceAll("###", "</br>"));
						}
					}

					request.setAttribute("so1", so1);
					request.setAttribute("decision", so1.getDecision());
					request.setAttribute("checkedServ", checkedBox);

					request.setAttribute("compteurMax",StockageOeuvres.compteur );
					request.setAttribute("compteurRestant", StockageOeuvres.compteurRestant );
					request.setAttribute("compteurFiltre",sou.getListeEntity1().size());
					request.setAttribute("cptNext",numeroOeuvre );

					request.setAttribute("filtrage", modeFiltrage);

					request.setAttribute("lV",lastVisited.length);
					request.setAttribute("lastV", lastVisited);
					request.setAttribute("cptTemp", numeroOeuvreTemp );
					request.setAttribute("ratio", so1.getRate().get(numeroOeuvre));
					request.setAttribute("listeProp", StockageOeuvres.getListeProp());

					this.getServletContext().getRequestDispatcher( "/SameAsAffichage.jsp" ).forward( request, response );
				}


				else{//Si compteurNext > a la taille de la liste
					if(modeFiltrage){ // mode filtrage => on sort du mode filtrage
						modeFiltrage=false;
						sou=so1;
						StockageOeuvres.compteurNext=0;
						StockageOeuvres.compteurTemp=0;

						request.setAttribute("so1", so1);
						request.setAttribute("decision", so1.getDecision());
						request.setAttribute("checkedServ", checkedBox);
						request.setAttribute("finFiltrage",true);//a cause du double passage

						request.setAttribute("compteurMax",StockageOeuvres.compteur );
						request.setAttribute("compteurFiltre",sou.getListeEntity1().size());
						request.setAttribute("compteurRestant", StockageOeuvres.compteurRestant );
						request.setAttribute("cptNext",StockageOeuvres.compteurNext );

						request.setAttribute("lV",lastVisited.length);
						request.setAttribute("lastV", lastVisited);
						request.setAttribute("cptTemp", numeroOeuvreTemp );
						request.setAttribute("ratio", so1.getRate().get(StockageOeuvres.compteurNext));
						request.setAttribute("listeProp", StockageOeuvres.getListeProp());

						this.getServletContext().getRequestDispatcher( "/SameAsAffichage" ).forward( request, response );

					}
					else{ // si mode normal => on passe en mode sauvegarde
						request.setAttribute("checkedServ", checkedBox);
						request.setAttribute("so1", so1);

						this.getServletContext().getRequestDispatcher( "/SameAsSave" ).forward( request, response );
					}
				}
			}
		}
	}
}



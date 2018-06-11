package methods;

import java.util.HashMap;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import beans.StockageOeuvres;


public class AR {
	

	private static String addNewStringHashMap(String s, HashMap<String,String> hm){
		String res="";
		res= hm.get(s);
		if(hm.get(s)== null){
			res="";
		}
		return res;
	}
	
	
	public static HashMap<String,String> getAttributes(String resource){
		//premiere partie, le compositeur qui fait partie des predecesseurs
		HashMap<String,String> proprietes = new HashMap<>();
		String qsString;
		String qsStringSwitch;

		//Le programme n'étant pas générique, la liste des propriétés a traverser est codée en dur dans la requête SQL
		String sparqlQueryStringComp = "PREFIX efrbroo: <http://erlangen-crm.org/efrbroo/> "+
				"PREFIX ecrm:  <http://erlangen-crm.org/current/> "+
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> "+
				"SELECT DISTINCT ?res "+
				"WHERE { "+
				"?nom efrbroo:R17_created <"+resource+">. "+ 
				"?nom ecrm:P9_consists_of ?consist. "+ 
				"?consist ecrm:P14_carried_out_by ?compo. "+
				"?compo ecrm:P131_is_identified_by ?res}";
		Query myQueryComp=QueryFactory.create(sparqlQueryStringComp);
		QueryExecution qexecComp = QueryExecutionFactory.sparqlService("http://data.doremus.org/sparql", myQueryComp);
		ResultSet queryResultsComp = qexecComp.execSelect();
		while(queryResultsComp.hasNext()){
			QuerySolution qs = queryResultsComp.nextSolution();
			proprietes.put("compositor", qs.getLiteral("?res").toString() +"###");
		}
		qexecComp.close();

		//Deuxieme partie, les proprietes qui sont dans les successeurs
		String sparqlQueryString = "PREFIX efrbroo: <http://erlangen-crm.org/efrbroo/> "+
				"PREFIX ecrm:  <http://erlangen-crm.org/current/> "+
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> "+
				"SELECT DISTINCT ?n ?m ?p " +
				"WHERE { " +
				"<"+resource+"> ?m ?n." +
				" OPTIONAL { ?n  ?o ?p} .}";
		Query myQuery=QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://data.doremus.org/sparql", myQuery);
		ResultSet queryResults = qexec.execSelect();

		while(queryResults.hasNext()){
			String temp;
			QuerySolution qs = queryResults.nextSolution();
			qsString = qs.getResource("?m").toString();
			if(qsString.contains("/ontology#U")||qsString.contains("P3_has_note")){ //Tri les proprietes du Reste (celles qui nous interesse dans le cadre de Doremus sont soit les U__ soit le P3 has note)
				
				//on découpe l'url de la propriété afin d'en retirer le nom. Ne marche que pour Doremus, le mieux étant d'aller chercher le label.
				qsStringSwitch=qsString.substring(qsString.lastIndexOf("has")+4).replace("_"," ");
				if(qsString.contains("had")){
					qsStringSwitch=qsString.substring(qsString.lastIndexOf("had")+4).replace("_"," ");
				}
				//pour une propriete donnee, on ajoute toutes les valeurs lui correspondant dans une seule et meme string, en séparant chaque valeur par "###"
				//ce qui nous permettra parla suite de récuperer independamment chaque valeur en splittant sur le schema "###"
				if(qs.get("?n").isLiteral() && qs.get("?n")!=null){//Si n est un literal
					temp=addNewStringHashMap(qsStringSwitch,proprietes);
					temp+=qs.getLiteral("?n").toString()+"###";
					proprietes.put(qsStringSwitch,temp);
				}
				else{//Si n URI
					if(qs.get("?p")!=null && qs.get("?p").isLiteral()){//si p literal
						if(qs.getLiteral("?p").toString().contains("^^")){ //TROUVER UNE AUTRE SOLUTION
							temp=addNewStringHashMap(qsStringSwitch,proprietes);
							temp+=qs.getLiteral("?p").toString().replaceAll("(.*)^.*$", "") +"";
							proprietes.put(qsStringSwitch,temp);
						}
						else{
							temp=addNewStringHashMap(qsStringSwitch,proprietes);
							temp+=qs.getLiteral("?p").toString()+ "###";
							proprietes.put(qsStringSwitch,temp);
						}
					}
				}
			}
		}
		//Comptage du nombre d'occurence de propriete pour afficher a l'utilisateur dans l'ordre du + présent au moins présent.
		for (String s : proprietes.keySet()){
			if(!StockageOeuvres.getScoreProp().keySet().contains(s)){
				StockageOeuvres.getScoreProp().put(s,1.0);
			}
			else{
				StockageOeuvres.getScoreProp().put(s,StockageOeuvres.getScoreProp().get(s)+1.0);
			}
		}
		return proprietes;
	}
}

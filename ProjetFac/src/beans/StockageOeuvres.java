package beans;

import java.util.ArrayList;
import java.util.HashMap;


/*On va surtout jouer sur les indices pour s'y retrouver : un couple d'oeuvre sera determiné par un indice i.
 * L'oeuvre 1 sera l'indice i de listeEntity1, l'oeuvre 2 sera l'indice i de listeEntity2, la decision sera l'indice i de decision etc..
 */
public class StockageOeuvres {
	private ArrayList<String> listeEntity1=new ArrayList<>();
	private ArrayList<String> listeEntity2=new ArrayList<>();
	public ArrayList<String> rdfsType = new ArrayList<>();
	
	/*Une oeuvre est stocké ainsi : {oeuvre1 - {prop11-obj11, prop12-obj12 ...}, oeuvre2 - {prop21-obj21, prop22-obj22,...},...}*/
	private HashMap<String, HashMap<String,String>> oeuvres=new HashMap <>();
	
	private static HashMap<String,Double> scoreProp = new HashMap<>();
	private static ArrayList<String> listeProp= new ArrayList<>();
	public static HashMap <String, Double>statProp = new HashMap<>();
	
	private ArrayList<String> rate=new ArrayList<>();
	public static int compteurNext=-1; //oeuvre en cours
	public static double compteurRestant=0;
	public static int compteurTemp=0; //numero derniere oeuvre vu
	public static double compteur=0; //nbr total oeuvre
	
	private ArrayList<String> decision=new ArrayList<>();
	
	private static ArrayList<String> listeTripletResultat=new ArrayList <>();
	
	
	public StockageOeuvres(){}

	public ArrayList<String> getListeEntity1() {
		return listeEntity1;
	}

	public void setListeEntity1(ArrayList<String> listeEntity1) {
		this.listeEntity1 = listeEntity1;
	}

	public ArrayList<String> getListeEntity2() {
		return listeEntity2;
	}

	public void setListeEntity2(ArrayList<String> listeEntity2) {
		this.listeEntity2 = listeEntity2;
	}

	public ArrayList<String> getRate() {
		return rate;
	}

	public void setRate(ArrayList<String> rate) {
		this.rate = rate;
	}

	public static ArrayList<String> getListeTripletResultat() {
		return listeTripletResultat;
	}

	public static void setListeTripletResultat(ArrayList<String> listeTripletResultat) {
		StockageOeuvres.listeTripletResultat = listeTripletResultat;
	}

	public HashMap<String, HashMap<String, String>> getOeuvres() {
		return oeuvres;
	}

	public void setOeuvres(HashMap<String, HashMap<String, String>> oeuvres) {
		this.oeuvres = oeuvres;
	}

	public static ArrayList<String> getListeProp() {
		return StockageOeuvres.listeProp;
	}

	public static void setListeProp(ArrayList<String> listeProp) {
		StockageOeuvres.listeProp = listeProp;
	}

	public static HashMap<String, Double> getScoreProp() {
		return scoreProp;
	}

	public static void setScoreProp(HashMap<String, Double> scoreProp) {
		StockageOeuvres.scoreProp = scoreProp;
	}

	public ArrayList<String> getDecision() {
		return decision;
	}

	public void setDecision(ArrayList<String> decision) {
		this.decision = decision;
	}
	
	
}

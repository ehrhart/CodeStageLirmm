package methods;

import beans.StockageOeuvres;
import org.apache.commons.lang3.StringUtils;

public class Research {

	//Pour le filtre : des mots clef et des niveaux d'indice de confiance. Les mots clef sont exclusif: si parmis les proprietes de l'oeuvre, un seul des mots 
	//est retrouvé, l'oeuvre est ajouté au filtre.
		
	public static StockageOeuvres filtrage(StockageOeuvres so1, String[] INNERHTML, double filtreIndiceMax, double filtreIndiceMin){
		StockageOeuvres sof = new StockageOeuvres();
		double indiceConfiance;
		for (int i=0; i<so1.getListeEntity1().size();i++){
			indiceConfiance=Double.parseDouble(so1.getRate().get(i))*100;
			for (String s: StockageOeuvres.getListeProp()){ //pour chaque propriete existante
				for(int j=0;j<INNERHTML.length;j++){ //pour chaque mot du filtre
					if(so1.getOeuvres().get(so1.getListeEntity1().get(i)).containsKey(s)){ //si l'oeuvre possede la propriete S
						//si la valeur de la propriete contient le mot INNERHTML[j]
						if (StringUtils.containsIgnoreCase(so1.getOeuvres().get(so1.getListeEntity1().get(i)).get(s), INNERHTML[j]) && !sof.getListeEntity1().contains(so1.getListeEntity1().get(i))){
							//si l'oeuvre est contenu dans la fourchette d'indice de confiance
							if(indiceConfiance<=filtreIndiceMax && indiceConfiance>=filtreIndiceMin){
								//alors on ajoute l'oeuvre au filtre
								sof.getListeEntity1().add(so1.getListeEntity1().get(i));
								sof.getListeEntity2().add(so1.getListeEntity2().get(i));
							}
						}
					}
					//pareil pour les oeuvre de listeEntity2..
					if(so1.getOeuvres().get(so1.getListeEntity2().get(i)).containsKey(s)){
						if (StringUtils.containsIgnoreCase(so1.getOeuvres().get(so1.getListeEntity2().get(i)).get(s), INNERHTML[j]) && !sof.getListeEntity2().contains(so1.getListeEntity2().get(i))){
							if(indiceConfiance<=filtreIndiceMax && indiceConfiance>=filtreIndiceMin){
								sof.getListeEntity1().add(so1.getListeEntity1().get(i));
								sof.getListeEntity2().add(so1.getListeEntity2().get(i));
							}
						}
					}
				}
			}
		}
		return sof;
	}
	
	//pour la recherche, fonctionne pareil que pour le filtre, mais sans indice de confiance
	
	public static StockageOeuvres recherche(StockageOeuvres so,StockageOeuvres sourceComplete, String[] INNERHTML){
		StockageOeuvres sor = new StockageOeuvres();
		boolean accept1;
		boolean accept2;
		
		for (int i=0; i<so.getListeEntity1().size();i++){
			int j = 0;
			int k = 0;
			for (String s: StockageOeuvres.getListeProp()){ 
				accept1 = false;
				accept2 = false;
				if(sourceComplete.getOeuvres().get(so.getListeEntity1().get(i)).containsKey(s)){
					j=0;
					k=0;
					while (j<INNERHTML.length){
						if(StringUtils.containsIgnoreCase(sourceComplete.getOeuvres().get(so.getListeEntity1().get(i)).get(s), INNERHTML[j])){
							accept1 = true;
						}
						j++;
					}
					
					if (accept1 && !sor.getListeEntity1().contains(so.getListeEntity1().get(i))){
						sor.getListeEntity1().add(so.getListeEntity1().get(i));
						sor.getListeEntity2().add(so.getListeEntity2().get(i));
					}
				}
				if(sourceComplete.getOeuvres().get(so.getListeEntity2().get(i)).containsKey(s)){
					while (k<INNERHTML.length){
						if(StringUtils.containsIgnoreCase(sourceComplete.getOeuvres().get(so.getListeEntity2().get(i)).get(s), INNERHTML[k])){
							accept2 = true;
						}
						k++;
					}
					if (accept2 && !sor.getListeEntity2().contains(so.getListeEntity2().get(i))){
						sor.getListeEntity1().add(so.getListeEntity1().get(i));
						sor.getListeEntity2().add(so.getListeEntity2().get(i));
					}
				}
			}
		}
		return sor;
	}
}


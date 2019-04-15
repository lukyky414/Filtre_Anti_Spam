package filtre;

public class FiltreAntiSpam {

	public FiltreAntiSpam() {
		
	}
	
	public static void main (String args[]) {
		Filtre_mail classifieur = new Filtre_mail();
		classifieur.charger_dictionnaire(args[0]);
		/*
		for (String s : classifieur.getDico()) {
			System.out.println(s);
		}
		System.out.println("Mot dans le dictionnaire : " + classifieur.getDico().size());
		*/
	}
	
	
}

package filtre;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FiltreAntiSpam {

	public FiltreAntiSpam() {
		
	}

	private static ArrayList<String> getAllFiles(String directory){
		File folder = new File(directory);
		ArrayList<String> res = new ArrayList<String>();

		if(folder.isFile()) {
			res.add(folder.getPath());
			return res;
		}

		File[] listOfFiles = folder.listFiles();


		for (int i = 0; i < listOfFiles.length; i++)
			if (listOfFiles[i].isFile())
				res.add(listOfFiles[i].getPath());

		return res;
	}
	
	public static void main (String args[]) {
		if(args.length < 2){
			System.out.println("Il manque des parametres:");
			System.out.println("  1- le dictionnaire.");
			System.out.println("  2- le fichier/dossier de mail.");
			return;
		}


		Filtre_mail classifieur = new Filtre_mail();
		classifieur.charger_dictionnaire(args[0]);

		/*
		for (String s : classifieur.getDico()) {
			System.out.println(s);
		}
		System.out.println("Mot dans le dictionnaire : " + classifieur.getDico().size());
		*/

		ArrayList<String> mails = getAllFiles(args[1]);

		for (String mail : mails){

			System.out.println(mail);

			boolean[] representation = classifieur.lire_message(mail);

			for(int i=0; i < classifieur.getDico().size(); i++){
				System.out.print(representation[i] ? '1' : '0');
			}
			System.out.println();
		}
	}
	
	
}

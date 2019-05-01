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
			res.addAll(getAllFiles(listOfFiles[i].getPath()));

		return res;
	}

	private static void printHelp(){
		System.out.println("Programme lance sans ou avec des mauvais parametres.");
		System.out.println("  -h / --help : Afficher ce message.");
		System.out.println("  -d : Le chemin du dictionnaire. (obligatoire)");
		System.out.println("  -ba : Le chemin de la Base d'Apprentissage.");
		System.out.println("  -bt : Le chemin de la Base de Test.");
		System.out.println("  -ns : Nombre de Spam a charger de la base d'apprentissage.");
		System.out.println("  -nh : Nombre de Ham a charger de la base d'apprentissage.");
		System.exit(0);
	}
	
	public static void main (String args[]) {

		String dico_path = "", base_app_path = "", base_test_path = "";
		int nbSpam = 0, nbHam = 0;

		{
			int i = 0;
			while (i < args.length) {
				switch (args[i].toLowerCase()) {
					case "-d":
						i++;
						if (i >= args.length)
							printHelp();
						dico_path = args[i];
						break;

					case "-ba":
						i++;
						if (i >= args.length)
							printHelp();
						base_app_path = args[i];
						break;

					case "-bt":
						i++;
						if (i >= args.length)
							printHelp();
						base_test_path = args[i];
						break;

					case "-ns":
						i++;
						if (i >= args.length)
							printHelp();
						nbSpam = Integer.parseInt(args[i]);
						break;

					case "-nh":
						i++;
						if (i >= args.length)
							printHelp();
						nbHam = Integer.parseInt(args[i]);
						break;

					case "-h":
					case "--help":
					default:
						printHelp();
				}
				i++;
			}

			if (dico_path == "")
				printHelp();
		}

		Filtre_mail classifieur = new Filtre_mail();
		classifieur.charger_dictionnaire(dico_path);

		/*
		for (String s : classifieur.getDico()) {
			System.out.println(s);
		}
		System.out.println("Mot dans le dictionnaire : " + classifieur.getDico().size());
		//*/

		ArrayList<String> mails = getAllFiles(base_test_path);

		for (String mail : mails){

			System.out.println(mail);

			boolean[] representation = classifieur.lire_message(mail);


			for (int i = 0; i < classifieur.getDico().size(); i++) {
				System.out.print(representation[i] ? '1' : '0');
			}
			System.out.println();

		}
	}
	
	
}

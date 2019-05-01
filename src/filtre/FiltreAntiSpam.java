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

	public static ArrayList<String> getAllFiles(String directory){
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
		System.out.println("  -t : Chemin vers un dossier ou un fichier test");
		System.exit(0);
	}
	
	public static void main (String args[]) {

		String dico_path = "../dictionnaire1000en.txt", base_app_path = "../baseapp/", base_test_path = "../basetest/", mail_test = "";
		int nbSpam = 200, nbHam = 200;

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

					case "-t":
						i++;
						if (i >= args.length)
							printHelp();
						mail_test = args[i];
						break;


					case "-h":
					case "--help":
					default:
						printHelp();
				}
				i++;
			}
		}

		Classifieur classifieur = new Classifieur(dico_path, nbHam, nbSpam);


		System.out.println("Apprentissage...");
		classifieur.apprentissage(base_app_path);

		//classifieur.prediction(base_test_path);



		//Test final
		if(!mail_test.equals("")){
			System.out.println("Les numeros des mails ne correspondent pas aux noms des fichiers.");
			int i, nH=0, nS=0;
			//Cherche recursivement tous les fichiers du dossier
			ArrayList<String> files = FiltreAntiSpam.getAllFiles(mail_test);
			for(i=0; i<files.size(); i++){
				boolean res = classifieur.predire(files.get(i));
				if(res)nS++;
				else nH++;

				System.out.println("Mail numero "+i+" identifie comme un "+(res?"SPAM":"HAM"));
			}
			System.out.println();
			System.out.println("Nous avons predit "+nH+" Ham(s) et "+nS+" Spam(s).");
		}
	}
	
	
}

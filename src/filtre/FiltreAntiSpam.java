package filtre;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FiltreAntiSpam {

	public static boolean PROB_DICO = false;
	public static boolean PRED_HAM = false;
	public static boolean PRED_SPAM = false;
	public static boolean TEST_ERR = false;
	public static boolean PROB_PRED = false;


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
		System.out.println("Programme lance sans ou avec des mauvais parametres:");
		System.out.println("  -h / --help : Afficher ce message.");
		System.out.println("  -d : Le chemin du dictionnaire.");
		System.out.println("  -ba : Le chemin de la Base d'Apprentissage.");
		System.out.println("  -bt : Le chemin de la Base de Test.");
		System.out.println("  -ns : Nombre de Spam a charger de la base d'apprentissage. (0=ALL)");
		System.out.println("  -nh : Nombre de Ham a charger de la base d'apprentissage.  (0=ALL)");
		System.out.println("  -t : Chemin vers un dossier ou un fichier test.");
		System.out.println("");
		System.out.println("Les chemins doivent etre de la forme:");
		System.out.println("  /path/to/folder/");
		System.out.println("");
		System.out.println("Activer certains affichages:");
		System.out.println("  -at : Afficher le resultat du classifieur.");
		System.out.println("  -ad : Affichage de la probabilite d'apparition d'un mot du dico dans les ham ou les spam.");
		System.out.println("  -ap : Affichage des probabilites lors de la prediction.");
		System.out.println("  -aph : Affichage de la verification des predictions des ham.");
		System.out.println("  -aps : Affichage de la verification des predictions des spam.");
		System.out.println("(conseil: -at -aph -aps)");
		System.out.println("");
		System.out.println("Valeurs par defaut:");
		System.out.println(" -d : ../dictionnaire1000en.txt");
		System.out.println(" -ba: ../baseapp/");
		System.out.println(" -bt: ../basetest/");
		System.out.println(" -ns: 0");
		System.out.println(" -nh: 0");
		System.out.println(" -t : [none]");
		System.exit(0);
	}
	
	public static void main (String args[]) {

		String dico_path = "../dictionnaire1000en.txt", base_app_path = "../baseapp/", base_test_path = "../basetest/", mail_test = "";
		int nbSpam = 0, nbHam = 0;

		{
			int i = 0;
			while (i < args.length) {
				switch (args[i].toLowerCase()) {
					case "-at":
						TEST_ERR=true;
						break;

					case "-ad":
						PROB_DICO=true;
						break;

					case "-ap":
						PROB_PRED=true;
						break;

					case "-aph":
						PRED_HAM=true;
						break;

					case "-aps":
						PRED_SPAM=true;
						break;

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

		long startTime = System.currentTimeMillis();

		Classifieur classifieur = new Classifieur(dico_path, nbHam, nbSpam);


		System.out.println("Apprentissage...");
		classifieur.apprentissage(base_app_path);

		System.out.println("\n\nTest...");
		classifieur.prediction(base_test_path);



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

		long stopTime = System.currentTimeMillis();

		double elapsedTime = (stopTime - startTime)/1000.0;
		System.out.println("\n\nTemps d'execution: " + elapsedTime + "s.");
	}
	
	
}

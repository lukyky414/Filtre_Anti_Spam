package filtre;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;

public class Classifieur {

	private ArrayList<String> dico;
	private int nbHam, nbSpam;
	private double[] probaMotHam, probaMotSpam;
	private int size;
	private double pHam, pSpam;

	public static int epsilon = 1;
	
	public Classifieur(String dico_path, int nbHam, int nbSpam) {
		dico = new ArrayList<String>(1000);

		charger_dictionnaire(dico_path);
		this.size = dico.size();

		probaMotHam = new double[size];
		probaMotSpam = new double[size];

		this.nbHam = nbHam;
		this.nbSpam = nbSpam;

		pHam = (double) nbHam / (double) (nbHam+nbSpam);
		pSpam = (double) nbSpam / (double) (nbHam+nbSpam);
	}
	
	protected ArrayList<String> getDico() {
		return dico;
	}

	private boolean directory_exist(String path){
		File folder = new File(path);
		ArrayList<String> res = new ArrayList<String>();
		boolean h=false, s=false;

		if(!folder.isDirectory()) {
			return false;
		}

		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				if(listOfFiles[i].getName().equals("ham"))
					h=true;
				if(listOfFiles[i].getName().equals("spam"))
					s=true;
			}
		}

		return h&&s;
	}

	/**
	 * Lance l'apprentissage
	 * @param base_app_path - Chemin du dossier d'apprentissage. Doit contenir un dossier HAM et un dossier SPAM
	 */
	public void apprentissage(String base_app_path){
		if(!directory_exist(base_app_path)){
			System.out.println("La base d'apprentissage ne contient pas de dossier \"/ham/\" ou \"/spam/\".");
			return;
		}

		int i;

		int[] motHam = new int[size];
		apprendre(base_app_path+"ham/", nbHam, motHam);

		for(i=0; i<size; i++)
			probaMotHam[i] = (double) (motHam[i] + epsilon ) / (double)(nbHam + 2 * epsilon);

		int[] motSpam = new int[size];
		apprendre(base_app_path+"spam/", nbSpam, motSpam);

		for(i=0; i<size; i++)
			probaMotSpam[i] = (double)( motSpam[i] + epsilon ) / (double)(nbSpam + 2 * epsilon);

		//Decomenter pour afficher
		//Voir la probabilite d'apparition d'un mot du dico dans les ham ou les spam
		/*
		int tmp;
		for (i = 0; i < size; i++) {
			System.out.print(dico.get(i) + "\t");
			if (dico.get(i).length() < 8) System.out.print("\t");

			tmp = (int) (probaMotHam[i] * 100);
			System.out.print("\t- h:" + tmp + "%");
			tmp = (int) (probaMotSpam[i] * 100);
			System.out.println("\t  s:" + tmp + "%");
		}//*/
	}

	private void apprendre(String chemin, int nb, int[] tab){
		ArrayList<String> files = FiltreAntiSpam.getAllFiles(chemin);

		if(nb == 0)
			nb = files.size();

		if(files.size() < nb){
			System.out.println("Pas assez de fichiers d'apprentissage dans "+chemin);
			System.exit(1);
		}

		int i, j;
		for(i=0; i<nb; i++){
			boolean[] representation = lire_message(files.get(i));

			for(j=0; j < size; j++)
				if(representation[j])
					tab[j]++;
		}
	}

	/**
	 * Lance la prediction pour une base de test
	 * @param base_test_path - Chemin du dossier de test. Doit contenir un dossier HAM et un dossier SPAM
	 */
	public void prediction(String base_test_path){
		if(!directory_exist(base_test_path)){
			System.out.println("La base de test ne contient pas de dossier \"/ham/\" ou \"/spam/\".");
			return;
		}
		int i;
		int errS=0, errH=0;
		int nbH=0, nbS=0;


		ArrayList<String> files = FiltreAntiSpam.getAllFiles(base_test_path+"ham/");
		for(i=0; i<files.size(); i++){
			nbH++;
			boolean res;
			res = predire(files.get(i));

			if(res)errH++;

			//Decomenter pour afficher
			//Affichage de la verification des predictions
			/*
			System.out.print("HAM numero "+i+" identifie comme un "+(res?"SPAM":"HAM"));
			if(res)
				System.out.println(" ***erreur***");
			else
				System.out.println();//*/
		}


		files = FiltreAntiSpam.getAllFiles(base_test_path+"spam/");
		for(i=0; i<files.size(); i++){
			nbS++;
			boolean res;
			res = predire(files.get(i));

			if(!res)errS++;


			//Decomenter pour afficher
			//Affichage de la verification des predictions
			/*
			System.out.print("SPAM numero "+i+" identifie comme un "+(res?"SPAM":"HAM"));
			if(!res)
				System.out.println("    ***erreur***");
			else
				System.out.println();//*/
		}

		//Decomenter pour afficher
		//Afficher les erreurs du classifieur lors du test

		System.out.println("\n\n");
		int tmp;
		tmp = (int)((double) (errH*100) / (double)nbH);
		System.out.println("Erreur de test sur les " + nbH + " HAM : " + tmp + "%");
		tmp = (int)((double) (errS*100) / (double)nbS);
		System.out.println("Erreur de test sur les " + nbS + " SPAM : " + tmp + "%");
		tmp = (int)((double)((errH+errS)*100)/(double)(nbH+nbS));
		System.out.println("Erreur de test globale sur " + (nbH+nbS) + " mails : "+ tmp + "%");//*/

	}

	/**
	 * Detection de spam
	 * @param file_path le chemin du mail
	 * @return true si le fichier est considere comme un spam
	 */
	public boolean predire(String file_path){
		boolean[] representation = lire_message(file_path);
		double probH = pHam;
		double probS = pSpam;
		int i;

		for(i=0; i<size; i++){
			if(representation[i]){
				probH *= probaMotHam[i];
				probS *= probaMotSpam[i];
			}else{
				probH *= 1-probaMotHam[i];
				probS *= 1-probaMotSpam[i];
			}
		}


		//Decomenter pour afficher
		//Affichage des probabilites lors de la prediction.
		/*
		System.out.print("P(Y=SPAM | X=x) = " + probS);
		System.out.print("\tP(Y=HAM | X=x) = " + probH);
		System.out.println("\t=> identifie comme un " + (probS > probH?"SPAM" : "HAM"));//*/

		return probS > probH;
	}
	
	/**
	 * Charge les mots de plus de 3 lettres depuis un fichier dictionnaire
	 * @param chemin Nom du fichier contenant le dictionnaire
	 */
	protected void charger_dictionnaire(String chemin) {
		try (Stream<String> stream = Files.lines(Paths.get(chemin)).filter(s -> s.length() > 3)) {
			stream.forEach(e -> dico.add(e.toUpperCase()));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parcourt tout les mots d'un mail et verifie leur presence ou non par rapport au dictionnaire
	 * @param path Nom du fichier contenant le message
	 * @return Un vecteur de boolean indiquant la presence ou non d'un mot du dictionnaire
	 */
	protected boolean[] lire_message(String path) {
		boolean[] representation = new boolean[getDico().size()];

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));

			String strCurrentLine;
			while ((strCurrentLine = reader.readLine()) != null)
				verifieMot(strCurrentLine, representation);

		}catch ( Exception e ){
			e.printStackTrace();
		}
		
		return representation;
	}

	/**
	 * Casse la ligne en plusieurs mot et verifie leur presence ou non par rapport au dictionnaire
	 * @param line Ligne de mot
	 */
	private void verifieMot(String line, boolean[] representation) {
		String[] splitted = line.split(" ");
		ArrayList<String> dico = getDico();
		for (String mot : splitted) {
			mot = filter(mot);

			if(mot != null) {
				for (int i = 0; i < dico.size(); i++) {
					if (mot.equals(dico.get(i))) {
						representation[i] = true;
					}
				}
			}
		}
	}

	/**
	 * Filtre les mots d'un message.
	 * @param word le mod a filtrer
	 * @return null si word.size() < 3, word en UpperCase sinon.
	 */
	private String filter(String word){
		//Permet d'enlever toute ponctuation ou charactere autre que des lettres.
		word = word.replaceAll("[0-9]|\\.|,|;|:|@|<|>|\\(|\\)|\\?|'|\"|\\-|\\+|/|\\[|\\]|#|=|`|\\*|!|\\$|~|\\t|\\n|\\r|\\\\|&|\\{|\\}|_|%|\\||\\^","");

		if(word.length() <= 3)
			return null;

		return word.toUpperCase();
	}
	
}

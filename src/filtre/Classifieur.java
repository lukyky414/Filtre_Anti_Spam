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
	private int[] probaMotSpam, probaMotHam; 
	
	public Classifieur() {
		dico = new ArrayList<String>(1000);
		probaMotSpam = new int[1000];
		probaMotHam = new int[1000];
	}
	
	protected ArrayList<String> getDico() {
		return dico;
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
				verifieMot(strCurrentLine.toUpperCase(), representation);

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
	 * @return null si word.size() < 3, word sans ',' '.' ou autre ponctuation sinon.
	 */
	private String filter(String word){
		if(word.length() <= 3)
			return null;
		//TODO plus de filtrations.
		return word;
	}
	
}

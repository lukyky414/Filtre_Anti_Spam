package filtre;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Filtre_mail {

	private ArrayList<String> dico;
	
	public Filtre_mail() {
		dico = new ArrayList<String>(1000);
	}
	
	protected ArrayList<String> getDico() {
		return dico;
	}
	
	/**
	 * Charge les mots de plus de 3 lettres depuis un fichier dictionnaire
	 * @param chemin Nom du fichier contenant le dictionnaire
	 */
	protected void charger_dictionnaire(String chemin) {
		//TODO doit etre insensible a la casse, il faudrait ajouter un .toLowerCase() mais je ne sais a quel endroit.
		try (Stream<String> stream = Files.lines(Paths.get(chemin)).filter(s -> s.length() > 3)) {
			stream.forEach(e -> dico.add(e.toUpperCase()));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parcourt tout les mots d'un mail et verifie leur presence ou non par rapport au dictionnaire
	 * @param message Nom du fichier contenant le message
	 * @return Une liste de boolean indiquant la presence ou non d'un mot du dictionnaire
	 */
	protected ArrayList<Boolean> lire_message(String message) {
		ArrayList<Boolean> representation = new ArrayList<>(getDico().size());
		try (Stream<String> stream = Files.lines(Paths.get(message))) {
			stream.forEach(e -> verifieMot(e.toUpperCase()));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return representation;
	}

	/**
	 * Casse la ligne en plusieurs mot et verifie leur presence ou non par rapport au dictionnaire
	 * @param line Ligne de mot
	 */
	private void verifieMot(String line) {
		line = line.toUpperCase();
		String[] splitted = line.split(" ");
		ArrayList<String> dico = getDico();
		for (String mot : splitted) {
			for (int i = 0 ; i < dico.size() ; i++) {
				if (dico.get(i) == mot) {
					//Passe le boolean de presence à l'index i à True/1
				}
			}
		}
	}
	
}

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
			stream.forEach(e -> dico.add(e));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

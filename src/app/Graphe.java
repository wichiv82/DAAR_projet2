package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Graphe {
	
	ArrayList<String> filenames;
	Double[][] dist_jaccard;
	double seuil;
	
	Graphe(int nbDocuments, String list_of_documents, double seuil) {
		
		/** Recuperation des filename de chaque docuements depuis un fichier qui les listes **/
		filenames = new ArrayList<String>();
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(list_of_documents))) {
			while ((line = br.readLine()) != null) {
				String words[] = line.trim().split("\\s+");
				for(String w : words) filenames.add(w);
			}
		} catch (IOException e) { e.printStackTrace(); }
		
		
		/** Remplissage de la matrice de distance de Jaccard**/
		dist_jaccard = new Double[nbDocuments][nbDocuments];
		this.seuil = seuil;
		for (int i = 0; i < dist_jaccard.length; i++) {
			for (int j = 0; j < dist_jaccard.length; j++) {
				HashMap<String, Integer> d1 = Jaccard.getOccurences(filenames.get(i));
				HashMap<String, Integer> d2 = Jaccard.getOccurences(filenames.get(j));
				dist_jaccard[i][j] = Jaccard.distanceJaccard(d1, d2);
			}
		}
	}

}

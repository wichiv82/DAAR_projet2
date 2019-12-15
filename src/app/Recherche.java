package app;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import projet1.RadixTree;
import util.Outils;

public class Recherche {
	
	public Recherche() {}
	
	/**
	 * Effectue une recherche de documents qui possedent au moins 1 occurrence du mot desire
	 * Renvoie une Map de Node avec un entier associe qui represente le nb d'occurrences du mot entre
	 * @param nodes
	 * @param mot
	 * @return
	 */
	public HashMap<Node, Integer> rechercheClassique(ArrayList<Node> nodes, String mot) {
		HashMap<Node, Integer> filename_result = new HashMap<Node, Integer>();
		
		for(Node n : nodes) {
			try {
				RadixTree r = new RadixTree(n.getIndex());
				int occurrences = r.searchMotif(mot).size();
				if(occurrences > 0)
					filename_result.put(n, occurrences);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		filename_result = (HashMap<Node, Integer>) Outils.sortByValueInDescendingOrder(filename_result);
		
		return filename_result;
	}
	
	/**
	 * A partir d'une recherche, renvoie une liste de documents triee par indice de centralite (closeness ici)
	 * @param resultat_recherche
	 * @return
	 */
	public Set<Node> classementParCentralite(HashMap<Node, Integer> resultat_recherche){
		// Contient l'indice de centralite de tous les documents dans resulat recherche
		HashMap<Node, Double> indice_centralite = new HashMap<Node, Double>();
		
		// Contient le nom des fichiers du resultat d'une recherche (resultat recherche)
		ArrayList<Node> files_resultat_recherche =  new ArrayList<>();
		files_resultat_recherche.addAll(resultat_recherche.keySet());
		
		for(Node n : files_resultat_recherche) {
			indice_centralite.put(n,GrapheJaccard.closeness_stream(n, files_resultat_recherche));
		}
		
		indice_centralite = (HashMap<Node, Double>) Outils.sortByValueInDescendingOrder(indice_centralite);
		
		return indice_centralite.keySet();
	}
	
	/**
	 * A partir d'une recherche, renvoie les voisins des 3 documents les plus pertinents dans le graphe de Jaccard
	 * et eux-memes
	 * @param resultat_recherche
	 * @return
	 */
	public ArrayList<Node> suggestions(HashMap<Node, Integer> resultat_recherche){
		ArrayList<Node> liste_suggestions = new ArrayList<Node>();
		
		// Contient le nom des fichiers du resultat d'une recherche (resultat recherche)
		ArrayList<String> filenames_resultat_recherche =  new ArrayList<>();
		
		resultat_recherche.keySet()
		.stream()
		.forEach(x -> filenames_resultat_recherche.add(x.getName()));
		
		Iterator<Entry<Node, Integer>> ite = resultat_recherche.entrySet().iterator();
		
		for(int i=0; i<3; i++)
			liste_suggestions.add(ite.next().getKey());
		
		int nb_documents = liste_suggestions.size();
		
		for(int i=0; i<Math.min(3, nb_documents); i++)
			liste_suggestions.get(i).getVoisins().keySet()
			.stream()
			.filter(x -> !liste_suggestions.contains(x))
			.forEach(x -> liste_suggestions.add(x));
			
		
		return liste_suggestions;
	}
	
}

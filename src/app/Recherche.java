package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
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
	public HashMap<Node, Integer> rechercheClassique(GrapheJaccard graphe, String mot) {
		HashMap<Node, Integer> filename_result = new HashMap<Node, Integer>();
		
		for(Node n : graphe.getSommets()) {
			Object result = n.getIndex().get(mot);
			
			if(result instanceof Integer) {
				if((int)result > 0)
					filename_result.put(n, (int) result);
			}
		}
		
		filename_result = (HashMap<Node, Integer>) Outils.sortByValueInDescendingOrder(filename_result);
		
		return filename_result;
	}
	
	/**
	 * Effectue un appel "egrep -l RegEx *" pour trouver tous les fichiers qui matchent la RegEx voulue
	 * @param nodes
	 * @param regEx
	 * @return
	 */
	public HashMap<Node, Integer> rechercheParRegEx(GrapheJaccard g, String regEx) {
		HashMap<Node, Integer> filename_result = new HashMap<Node, Integer>();
		
		Egrep e = new Egrep();
		e.egrep(regEx);
		ArrayList<String> files = e.getReponse_egrep();
		for(String file: files) {
			Node n = g.getNode(file);
			if(n != null)
				filename_result.put(n, 0);
		}
		return filename_result;
	}
	
	/**
	 * A partir d'une recherche, renvoie une liste de documents triee par indice de centralite (closeness ici)
	 * @param resultat_recherche
	 * @return
	 */
	public Set<Node> classementParCentralite(Set<Node> resultat_recherche){
		// Contient l'indice de centralite de tous les documents dans resulat recherche
		HashMap<Node, Double> indice_centralite = new HashMap<Node, Double>();
		
		// Contient le nom des fichiers du resultat d'une recherche (resultat recherche)
		ArrayList<Node> files_resultat_recherche =  new ArrayList<>();
		files_resultat_recherche.addAll(resultat_recherche);
		
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
	public ArrayList<Node> suggestions(Set<Node> resultat_recherche){
		ArrayList<Node> liste_suggestions = new ArrayList<Node>();
		
		// Contient le nom des fichiers du resultat d'une recherche (resultat recherche)
		ArrayList<String> filenames_resultat_recherche =  new ArrayList<>();
		
		resultat_recherche
		.stream()
		.forEach(x -> filenames_resultat_recherche.add(x.getName()));
		
		Iterator<Node> ite = resultat_recherche.iterator();
		
		for(int i=0; i<3; i++) {
			if(ite.hasNext())
				liste_suggestions.add(ite.next());
		}
		
		int nb_documents = liste_suggestions.size();
		
		for(int i=0; i<Math.min(3, nb_documents); i++)
			liste_suggestions.get(i).getVoisins().keySet()
			.stream()
			.filter(x -> !liste_suggestions.contains(x))
			.forEach(x -> liste_suggestions.add(x));
			
		
		return liste_suggestions;
	}
	
}

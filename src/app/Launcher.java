package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Launcher {

	public static void main (String[] args) {
		
//		Test de la classe Jacquard		
		HashMap<String, Integer> doc1 = Jaccard.getOccurences("test1");
		HashMap<String, Integer> doc2 = Jaccard.getOccurences("test2");
		HashMap<String, Integer> doc3 = Jaccard.getOccurences("epic");
		HashMap<String, Integer> doc4 = Jaccard.getOccurences("edgar");
		HashMap<String, Integer> doc5 = Jaccard.getOccurences("democracy1.txt");
		HashMap<String, Integer> doc6 = Jaccard.getOccurences("democracy2.txt");
		
		System.out.println(doc3.size());
		System.out.println(doc4.size());
		int nb = 0;
		for (String mot : doc3.keySet())
			nb += doc3.get(mot);
		System.out.println(nb);
		
		String mot = "";
		System.out.println("TEST FIREBSASE { " + mot + " : " + doc4.get(mot) +" }");
		
		ArrayList<String> filename = new ArrayList<String>();
		filename.add("test1");
		filename.add("test2");
		filename.add("edgar");
		filename.add("epic");
		filename.add("democracy1.txt");
		filename.add("democracy2.txt");
		
		
		System.out.println("------- Jaccard ------");
		System.out.println(Jaccard.distanceJaccard_stream(doc1, doc2));
		
		System.out.println(Jaccard.distanceJaccard_stream(doc4, doc3));
		
		System.out.println("------ Closeness -----");
		
		for (int i=0; i<filename.size(); i++) {
			System.out.println(Jaccard.closeness_stream(filename.get(i), filename));
		}
		
		System.out.println("------- Graphe de Jaccard ------");
		double seuil = 1.0;
		GrapheJaccard g = new GrapheJaccard(filename, seuil);
		System.out.println(g);
		
		System.out.println("--------- Recherche -----------");
		Recherche r = new Recherche();
		
		System.out.println("------ Recherche classque ------");
		HashMap<Node, Integer> rc = r.rechercheClassique(g.getSommets(), "America");
		for(Node n : rc.keySet())
			System.out.println(n.getName() + " : " + rc.get(n));
		
		System.out.println("--- Classement par Centralite ---");
		Set<Node> cpc = r.classementParCentralite(rc);
		for(Node n : cpc)
			System.out.println(n.getName());
		
		System.out.println("--------- Suggestions ----------");
		Set<Node> s = r.suggestions(rc, 0.1);
		for(Node n : s)
			System.out.println(n.getName());
		
//		Test de la classe Graphe
//		Graphe G = new Graphe(4, "list_of_documents", 0);
//		for (int i = 0; i < G.dist_jaccard.length; i++) {
//			for (int j = 0; j < G.dist_jaccard.length; j++) {
//				System.out.println("Jaccard entre : " + G.filenames.get(i) + " et " + G.filenames.get(j));
//				System.out.println(G.dist_jaccard[i][j]);
//				System.out.println();
//			}
//		}
	}

}

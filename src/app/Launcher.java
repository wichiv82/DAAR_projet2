package app;

import java.util.HashMap;

public class Launcher {

	public static void main (String[] args) {
		
//		Test de la classe Jacquard		
		HashMap<String, Integer> doc1 = Jaccard.getOccurences("test1.txt");
		HashMap<String, Integer> doc2 = Jaccard.getOccurences("test2.txt");
		System.out.println(Jaccard.distanceJaccard(doc2, doc1));
		System.out.println(Jaccard.distanceJaccard_stream(doc2, doc1));
		
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

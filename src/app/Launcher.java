package app;

import java.util.ArrayList;
import java.util.HashMap;

public class Launcher {

	public static void main (String[] args) {
		
//		Test de la classe Jacquard		
		HashMap<String, Integer> doc1 = Jaccard.getOccurences("test1.txt");
		HashMap<String, Integer> doc2 = Jaccard.getOccurences("test2.txt");
		HashMap<String, Integer> doc3 = Jaccard.getOccurences("edgar.txt");
		HashMap<String, Integer> doc4 = Jaccard.getOccurences("epic.txt");
		
		ArrayList<String> filename = new ArrayList<String>();
		filename.add("test1.txt");
		filename.add("test2.txt");
		filename.add("edgar.txt");
		filename.add("epic.txt");
		
		System.out.println(Jaccard.distanceJaccard(doc2, doc1));
		System.out.println(Jaccard.distanceJaccard_stream(doc1, doc2));
		
		System.out.println(Jaccard.distanceJaccard(doc3, doc4));
		System.out.println(Jaccard.distanceJaccard_stream(doc4, doc3));
		
		System.out.println("------ Closeness -----");
		
		for (int i=0; i<filename.size(); i++) {
			System.out.println(Jaccard.closeness(filename.get(i), filename));
			System.out.println(Jaccard.closeness_stream(filename.get(i), filename));
		}
		
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

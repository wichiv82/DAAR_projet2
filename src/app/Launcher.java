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
		
		ArrayList<String> filename = new ArrayList<String>();
		filename.add("test1");
		filename.add("test2");
		filename.add("edgar");
		filename.add("epic");
		filename.add("democracy1.txt");
		filename.add("democracy2.txt");
		filename.add("american-beers.txt");
		filename.add("breewing-beer.txt");
		filename.add("telephony-telegraphy.txt");
		filename.add("the-telephone.txt");
		
		
		System.out.println("------- Jaccard ------");
		System.out.println(Jaccard.distanceJaccard_stream(doc1, doc2));
		
		System.out.println(Jaccard.distanceJaccard_stream(doc4, doc3));
		
		System.out.println("\n------ Closeness -----");
		
		for (int i=0; i<filename.size(); i++) {
			System.out.println(Jaccard.closeness_stream(filename.get(i), filename));
		}
		
		System.out.println("\n------- Graphe de Jaccard ------");
		double seuil = 0.9;
		GrapheJaccard g = new GrapheJaccard(filename, seuil);
		System.out.println(g);
		
		System.out.println("--------- Recherche -----------");
		Recherche r = new Recherche();
		
		System.out.println("------ Recherche classique ------");
		String mot_a_chercher = "America";
		HashMap<Node, Integer> rc = r.rechercheClassique(g, mot_a_chercher);
		System.out.println("Nombre d'occurrences du mot " + mot_a_chercher);
		for(Node n : rc.keySet())
			System.out.println(n.getName() + " : " + rc.get(n));
		
		System.out.println("\n------ Recherche par RegEx -------");
		
		String os = System.getProperty("os.name");
		System.out.println("Systeme actuel : " + os);
		
		if(os.startsWith("Windows")) {
			System.out.println("La recherche par RegEx n'est pas supporte sur Windows");
		} else {
			String regEx = ".*";
			HashMap<Node, Integer> rpre = r.rechercheParRegEx(g, regEx);
			System.out.println(rpre.size() +" Fichiers matchant la RegEx " + regEx );
			for(Node n : rpre.keySet())
				System.out.println(n.getName());
		}
				
		System.out.println("\n--- Classement par Centralite ---");
		Set<Node> cpc = r.classementParCentralite(rc.keySet());
		for(Node n : cpc)
			System.out.println(n.getName());
		
		System.out.println("\n--------- Suggestions ----------");
		ArrayList<Node> s = r.suggestions(rc.keySet());
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

package app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

import util.Outils;

public class Launcher2 {
	
	@SuppressWarnings("unchecked")
	public static void main (String[] args) {
		// chemin du dossier contenant les livres
		//String path = "C:/Users/Willy/Documents/UPMC/DAAR/books-master/";
		String path = "/Vrac/books_daar/books-master/";
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		int limite = 3;
		double tailleFichierMinimale = 61440.0; 
		double tailleFichierMaximale = 307200.0;
		
		ArrayList<String> files = new ArrayList<String>();
		
		for (int i=0; i<listOfFiles.length; i++) {
			if(listOfFiles[i].length() >= tailleFichierMinimale && listOfFiles[i].length() <= tailleFichierMaximale) {
				files.add(path + listOfFiles[i].getName());
				System.out.println(listOfFiles[i].length()/1024.0);
			}
			
			if(files.size() >= limite) {
				break;
			}
		}
		
		System.out.println(files.size() + " fichiers selectionnes");
		
		folder = null;
		listOfFiles = null;
		
		System.out.println("CONSTRUCTION DU GRAPHE");
		GrapheJaccard g = new GrapheJaccard(files, 0.75);
		System.out.println("Nodes construits");
		g.getAllJaccardDistances();
		
		JSONObject JSON_final = new JSONObject();
		
		// PARTIE INDEXAGE
		System.out.println("DEBUT INDEXAGE");
		HashMap<String, Object> all_index = new HashMap<>(
			g.getSommets().parallelStream().collect(Collectors.toMap(n -> n.getName(), n -> Outils.HashMapToJSONObject(n.getIndex())))
		);
		
		//JSONObject all_index_json = Outils.HashMapToJSONObject(all_index);
		JSON_final.put("indexage", Outils.HashMapToJSONObject(all_index));
		all_index.clear();
		all_index = null;
		
		// PARTIE JACCARD 
		System.out.println("DEBUT JACCARD");
		
//		HashMap<String, Object> all_jaccard = new HashMap<>(
//			g.getSommets().parallelStream().collect(
//					Collectors.toMap(n -> n.getName(), n -> Outils.HashMapToJSONObjectForNodeNeighbours(n.getVoisins()))
//		));
		
		JSONObject all_jaccard_json = new JSONObject();
		g.getSommets()
		.parallelStream()
		.forEach(n -> all_jaccard_json.put(n.getName(),  Outils.HashMapToJSONObjectForNodeNeighbours(n.getVoisins(), g.getEdgeThreshold())));
		//= Outils.HashMapToJSONObject(all_jaccard);
		
		// PARTIE CLOSENESS
		System.out.println("DEBUT Closeness");
		//JSONObject all_closeness_json = Outils.HashMapToJSONObject(g.getAllCloseness());
		JSON_final.put("closeness", Outils.HashMapToJSONObject(g.getAllCloseness()));
		
		System.out.println("PRODUCTION DU JSON en cours");
		
		JSON_final.put("graphe", all_jaccard_json);
//		JSON_final.put("indexage", all_index_json);
//		JSON_final.put("closeness", all_closeness_json);
		
		Outils.JSONObjectToJSONFile(JSON_final, "Json/daar-projet3.json");
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
}

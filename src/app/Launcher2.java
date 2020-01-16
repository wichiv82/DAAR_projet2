package app;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

import util.Outils;

public class Launcher2 {
	
	@SuppressWarnings("unchecked")
	public static void main (String[] args) {
		// chemin du dossier contenant les livres
		String path = "C:/Users/Willy/Documents/UPMC/DAAR/books-master/";
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		int limite = 3;
		double tailleFichierMinimale = 50000.0; 
		
		ArrayList<String> files = new ArrayList<String>();
		
		for (int i=0; i<listOfFiles.length; i++) {
			if(listOfFiles[i].length() >= tailleFichierMinimale) {
				files.add(path + listOfFiles[i].getName());
				//System.out.println(listOfFiles[i].length()/1024.0);
			}
			
			if(files.size() >= limite) {
				//System.out.println("Cassé à : " + i);
				break;
			}
		}
		
		GrapheJaccard g = new GrapheJaccard(files, 0.75);
		List<List<Double>> tab = g.getAllJaccardDistances();
		
		// AFFICHER LA MATRICE
		boolean test = false;
		if(test) {
			for(List<Double> l : tab) {
				for(Double d : l) {
					System.out.print(d + " | ");
				}
				System.out.println();
			}
			
			return;
		}
		
		// PARTIE INDEXAGE
		System.out.println("DEBUT INDEXAGE");
		HashMap<String, Object> all_index = new HashMap<>(
			g.getSommets().stream().parallel().collect(Collectors.toMap(n -> n.getName(), n -> Outils.HashMapToJSONObject(n.getIndex())))
		);
		
		JSONObject all_index_json = Outils.HashMapToJSONObject(all_index);
		
		//map(n -> n.getName(), Outils.HashMapToJSONObject(n.getIndex()) ));
		
		//Outils.JSONObjectToJSONFile(all_index_json, "Json/indexage.json");
		
		// PARTIE JACCARD 
		System.out.println("DEBUT JACCARD");
		
//		for(int i=0; i<tab.size(); i++) {
//			JSONObject node = new JSONObject();
//			for(int j=0; j<tab.size(); j++) {
//				if(i != j)
//					node.put(g.getSommets().get(j).getName(), tab.get(i).get(j));
//			}
//			all_jaccard_json.put(g.getSommets().get(i).getName(), node);
//		}
		
		HashMap<String, Object> all_jaccard = new HashMap<>(
			g.getSommets().stream().parallel().collect(
					Collectors.toMap(n -> n.getName(), n -> Outils.HashMapToJSONObjectForNodeNeighbours(n.getVoisins()))
		));
		
		JSONObject all_jaccard_json = Outils.HashMapToJSONObject(all_jaccard);
		
		//Outils.JSONObjectToJSONFile(all_jaccard_json, "Json/graphe.json");
		
		// PARTIE CLOSENESS
		System.out.println("DEBUT Closeness");
		JSONObject all_closeness_json = Outils.HashMapToJSONObject(g.getAllCloseness());
		
		//Outils.JSONObjectToJSONFile(all_closeness_json, "Json/closeness.json");
		System.out.println("PRODUCTION DU JSON en cours");
		
		JSONObject JSON_final = new JSONObject();
		JSON_final.put("graphe", all_jaccard_json);
		JSON_final.put("indexage", all_index_json);
		JSON_final.put("closeness", all_closeness_json);
		
		Outils.JSONObjectToJSONFile(JSON_final, "Json/daar-projet3.json");
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
}

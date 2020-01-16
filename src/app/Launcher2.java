package app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import util.Outils;

public class Launcher2 {
	
	@SuppressWarnings("unchecked")
	public static void main (String[] args) {
		// chemin du dossier contenant les livres
		String path = "C:/Users/Willy/Documents/UPMC/DAAR/books-master/";
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		int limite = 1800;
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
		
		
		
		if(false) {
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
		JSONObject all_index_json = new JSONObject();
		g.getSommets().stream().forEach(n -> all_index_json.put(n.getName(), Outils.HashMapToJSONObject(n.getIndex())));
		
		//Outils.JSONObjectToJSONFile(all_index_json, "Json/indexage.json");
		
		// PARTIE JACCARD 
		JSONObject all_jaccard_json = new JSONObject();
		
		for(int i=0; i<tab.size(); i++) {
			JSONObject node = new JSONObject();
			for(int j=0; j<tab.size(); j++) {
				if(i != j)
					node.put(g.getSommets().get(j).getName(), tab.get(i).get(j));
			}
			all_jaccard_json.put(g.getSommets().get(i).getName(), node);
		}
		
		//Outils.JSONObjectToJSONFile(all_jaccard_json, "Json/graphe.json");
		
		// PARTIE CLOSENESS
		JSONObject all_closeness_json = Outils.HashMapToJSONObject(g.getAllCloseness());
		
		//Outils.JSONObjectToJSONFile(all_closeness_json, "Json/closeness.json");
		
		JSONObject JSON_final = new JSONObject();
		JSON_final.put("graphe", all_jaccard_json);
		JSON_final.put("indexage", all_index_json);
		JSON_final.put("closeness", all_closeness_json);
		
		Outils.JSONObjectToJSONFile(JSON_final, "Json/daar-projet3.json");
		
		
		
		
		
		
		
		
		
		
		
		
	}
}

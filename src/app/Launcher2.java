package app;

import java.io.File;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import util.Outils;

public class Launcher2 {
	
	@SuppressWarnings("unchecked")
	public static void main (String[] args) {
		// chemin du dossier contenant les livres
		String path = "C:/Users/Willy/Documents/UPMC/DAAR/books-master/";
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		int limite = 50;
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
		double[][] tab = g.getAllJaccardDistances();
		
		// PARTIE INDEXAGE
		System.out.println("DEBUT INDEXAGE");
		JSONObject all_index_json = new JSONObject();
		g.getSommets().stream().forEach(n -> all_index_json.put(n.getName(), Outils.HashMapToJSONObject(n.getIndex())));
		
		Outils.JSONObjectToJSONFile(all_index_json, "Json/indexage.json");
		
		// PARTIE JACCARD 
		JSONObject all_jaccard_json = new JSONObject();
		
		for(int i=0; i<tab.length; i++) {
			JSONObject node = new JSONObject();
			for(int j=0; j<tab.length; j++) {
				if(i != j)
					node.put(g.getSommets().get(j).getName(), tab[i][j]);
			}
			all_jaccard_json.put(g.getSommets().get(i).getName(), node);
		}
		
		Outils.JSONObjectToJSONFile(all_jaccard_json, "Json/graphe.json");
		
		// PARTIE CLOSENESS
		JSONObject all_closeness_json = Outils.HashMapToJSONObject(g.getAllCloseness(tab));
		
		Outils.JSONObjectToJSONFile(all_closeness_json, "Json/closeness.json");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}

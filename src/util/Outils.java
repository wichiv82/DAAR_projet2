package util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import app.Node;

public class Outils {
	
	/**
	 * Methode auxilaire qui trie une HashMap en fonction de la valeur (ordre croissant)
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueInAscendingOrder(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();
		
		map.entrySet()
	    .stream()
	    .sorted(Map.Entry.comparingByValue())
	    .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
		
		return result;
	}
	
	/**
	 * Methode auxilaire qui trie une HashMap en fonction de la valeur (ordre decroissant)
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueInDescendingOrder(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();
		
		map.entrySet()
	    .stream()
	    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
	    .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
		
		return result;
	}
	
	/**
	 * Creee un fichier JSON a partir d'un objet JSON
	 * @param json
	 * @param filename
	 */
	public static void JSONObjectToJSONFile(JSONObject json, String filename) {
		try (FileWriter file = new FileWriter(filename)) {

			file.write(json.toJSONString());
			file.flush();
			System.out.println("Fichier JSON "+ filename + " produit !");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <K, V> JSONObject HashMapToJSONObject(HashMap<K, V> map) {
		JSONObject json = new JSONObject();
		map.keySet().stream().forEach(x -> json.put(x, map.get(x)));
		
		return json;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject HashMapToJSONObjectForNodeNeighbours(HashMap<Node, Double> map) {
		JSONObject json = new JSONObject();
		map.keySet().stream().forEach(x -> json.put(x.getName(), map.get(x)));
		
		return json;
	}
	
}

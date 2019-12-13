package util;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

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
}

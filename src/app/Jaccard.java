package app;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Jaccard {
	
	public static HashMap<String, Integer> getOccurences(String filename){
		HashMap<String, Integer> occurences = new HashMap<String, Integer>();
		String line = "";

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			while ((line = br.readLine()) != null) {

				String words[] = line.trim().split("[^a-zA-Z]");
				for(String w : words) {
					if(occurences.containsKey(w))
						occurences.put(w, occurences.get(w) + 1);
					else 
						occurences.put(w, 1);
				}
				
			}
			occurences.remove("");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return occurences;
		
	}
	
	
	public static int[] getMaxMin(HashMap<String, Integer> d1, HashMap<String, Integer> d2, String mot) {
		int[] result = new int[2];
		
		if(d1.containsKey(mot) && d2.containsKey(mot)) {
			result[0] = Math.max(d1.get(mot), d2.get(mot));
			result[1] = Math.min(d1.get(mot), d2.get(mot));
		} else if(d1.containsKey(mot)) {
			result[0] = d1.get(mot);
			result[1] = 0;
		} else if (d2.containsKey(mot)) {
			result[0] = d2.get(mot);
			result[1] = 0;
		} else {
			result[0] = 0;
			result[1] = 0;
		}
		
		return result;
	}
	
	public static double distanceJaccard(HashMap<String, Integer> d1, HashMap<String, Integer> d2) {
		HashSet<String> mots = new HashSet<String>(d1.keySet());
		mots.addAll(d2.keySet());
		
		double numerateur = 0;
		double denominateur = 0;
		
		for(String mot : mots) {
			int[] max_min = getMaxMin(d1, d2, mot);
			numerateur += max_min[0] - max_min[1];
			denominateur += max_min[0];
		}
		
		if(denominateur == 0)
			return 0;
		
		return numerateur/denominateur;
	}
	
	public static double distanceJaccard_stream(HashMap<String, Integer> d1, HashMap<String, Integer> d2) {
		HashSet<String> mots = new HashSet<String>(d1.keySet());
		mots.addAll(d2.keySet());
		
		double numerateur = 
		mots.stream()
			.map(mot -> getMaxMin(d1, d2, mot)[0] - getMaxMin(d1, d2, mot)[1])
			.reduce(0, Integer::sum);
		
		double denominateur = 
		mots.stream()
			.map(mot -> getMaxMin(d1, d2, mot)[0])
			.reduce(0, Integer::sum);
		
		if(denominateur == 0)
			return 0;
		
		//System.out.println(numerateur + " / " + denominateur);
		return numerateur/denominateur;
	}
	
	
	public static double closeness(String doc, ArrayList<String> files) {
		@SuppressWarnings("unchecked")
		ArrayList<String> documents = (ArrayList<String>) files.clone();
		double denominateur = 0;
		
		// Cas ou le document a etudier est dans la liste
		documents.remove(doc);
		
		HashMap<String, Integer> doc_occurences = getOccurences(doc);
		
		for(String file : documents)
			denominateur += distanceJaccard(doc_occurences, getOccurences(file));
		
		if(denominateur == 0)
			return 0;
		
		return (documents.size()) / denominateur;
	}
	
	public static double closeness_stream(String doc, ArrayList<String> files) {
		@SuppressWarnings("unchecked")
		ArrayList<String> documents = (ArrayList<String>) files.clone();
		
		// Cas ou le document a etudier est dans la liste
		documents.remove(doc);
		
		HashMap<String, Integer> doc_occurences = getOccurences(doc);
		
		double denominateur = 
				documents.stream()
					.map(x -> distanceJaccard_stream(doc_occurences, getOccurences(x)))
					.reduce(0.0, Double::sum);
		
		if(denominateur == 0.0)
			return 0;
		
		return (documents.size()) / denominateur;
	}
	
}

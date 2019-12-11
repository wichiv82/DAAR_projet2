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
	
	/**MAYBE TO DO**/
	public static double betweenness(ArrayList<String> documents, double edgeThreshold) {
		double numerateur = 0;
		double denominateur = 0;
		
		
		int[][] paths=new int[documents.size()][documents.size()];
		for (int i=0;i<paths.length;i++) for (int j=0;j<paths.length;j++) paths[i][j]=i;
		
		double[][] dist=new double[documents.size()][documents.size()];

	    for (int i=0;i<paths.length;i++) {
	    	for (int j=0;j<paths.length;j++) {
	    		if (i==j) {
	    			dist[i][i]=0; 
	    			continue;
	    		}
	        
	    		HashMap<String, Integer> d1 = getOccurences(documents.get(i));
	    		HashMap<String, Integer> d2 = getOccurences(documents.get(j));
	    		double d = distanceJaccard(d1, d2);
	        
	    		if (d <= edgeThreshold)
	    			dist[i][j] = d; 
	    		else 
	    			dist[i][j] = Double.POSITIVE_INFINITY;
	        
	    		paths[i][j]=j;
	    	}
	    }
	    
	    HashMap<Integer, Double> ppcs = new HashMap<>();  
	    
	    for (int k=0;k<paths.length;k++) {
	    	for (int i=0;i<paths.length;i++) {
	    		for (int j=0;j<paths.length;j++) {
	    			if (dist[i][j]>dist[i][k] + dist[k][j]){
	    				dist[i][j]=dist[i][k] + dist[k][j];
	    				paths[i][j]=paths[i][k];

	    			}
	    		}
	    	}
	    }
	    
	    for (int k=0;k<paths.length;k++) {
	    	for (int i=0;i<paths.length;i++) {
	    		for (int j=0;j<paths.length;j++) {
	    			if (dist[i][j]>dist[i][k] + dist[k][j]){
	    				if(ppcs.get(k) != null)
	    					ppcs.put(k, 1.0);
	    				else {
	    					double b = ppcs.get(k);
	    					ppcs.put(k, b++);
	    				}
	    			} else {
	    				ppcs.put(k, nb_ppc_passant_par(paths, dist, i, j, k));
	    			}
	    		}
	    	}
	    }
	    
	    /** Si a faire , reprendre ici **/
	    		
		if(denominateur == 0)
			return 0;
		
		return numerateur/ denominateur; // FAUX, RETOURNER LA FORMULE DU FUTUR AVEC LES PPC :
		
		// B(v) = Somme(#ppc (s,t passent par v) / #ppc (s,t) ) 
		// avec s != t € V
		// 		s != v
		//		t != v
	}
	
	/** Retourne le nb de ppc possible de depart a arrivee qui passe par point **/
	public static double nb_ppc_passant_par(int[][] paths, double[][]dist, int depart, int arrivee, int point) {
		double b = 0;
		int x = depart;
		while(x != point) {
			// x vaut le point suivant
			// on incr�mente b
		}
		return b;
	}
	
	/** Retourne le nb de ppc possible de depart a arrivee **/
	public static int nb_ppc_(int[][] paths, int[][]dist, int depart, int arrivee) {
		
		return 0;
	}
}

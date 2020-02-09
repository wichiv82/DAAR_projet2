package app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


import util.Outils;

public class Node {
	private String name;
	private HashMap<String, Integer> index;
	private HashMap<Node, Double> voisins; 
	
	public Node(String filename) {
		String[] chemin = filename.split("/");
		name = chemin[chemin.length-1].replace('.', '_');
		index = getOccurences(filename);
		voisins = new HashMap<Node, Double>();
	}
	
	public String getName() {
		return name;
	}
	
	public HashMap<String, Integer> getIndex(){
		return index;
	}
	
	public HashMap<Node, Double> getVoisins(){
		return voisins;
	}
	
	public void addVoisin(Node n, Double distance) {
		voisins.put(n, distance);
	}
	
	/**
	 * Construit la liste des voisins
	 * @param nodes
	 * @param edgeThreshold
	 */
	public void setVoisins(ArrayList<Node> nodes, double edgeThreshold){
		voisins = (HashMap<Node, Double>) Outils.sortByValueInAscendingOrder(searchVoisins(nodes, edgeThreshold));
	}
	
	/**
	 * Indexage d'un document
	 * @param filename
	 * @return
	 */
	public HashMap<String, Integer> getOccurences(String filename){
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
	
	/**
	 * Methode auxiliaire pour distanceJaccard
	 * @param d1
	 * @param d2
	 * @param mot
	 * @return
	 */
	public int[] getMaxMin(HashMap<String, Integer> d1, HashMap<String, Integer> d2, String mot) {
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
	
	/**
	 * Renvoie la distance de Jaccard 
	 * @param d
	 * @return
	 */
	public double distanceJaccard(HashMap<String, Integer> d) {
		HashSet<String> mots = new HashSet<String>(index.keySet());
		mots.addAll(d.keySet());
		
		double numerateur = 
		mots.parallelStream()
			.map(mot -> getMaxMin(index, d, mot)[0] - getMaxMin(index, d, mot)[1])
			.reduce(0, Integer::sum);
		
		double denominateur = 
		mots.parallelStream()
			.map(mot -> getMaxMin(index, d, mot)[0])
			.reduce(0, Integer::sum);
		
		//System.out.println(getName() + " fin distanceJaccard");
		
		if(denominateur == 0)
			return 0;
		
		//System.out.println(numerateur + " / " + denominateur);
		return numerateur/denominateur;
	}
	
	/**
	 * Cherche les documents voisins avec une distance <= a un certain seuil de distance
	 * @param nodes
	 * @param edgeThreshold
	 * @return
	 */
	public HashMap<Node, Double> searchVoisins(ArrayList<Node> nodes, double edgeThreshold){
		HashMap<Node, Double> result = new HashMap<Node, Double>();
		for(Node n : nodes) {
			if(n.getName().equals(this.name))
				continue;
			
			double distance = distanceJaccard(n.getIndex());
			
			if(distance <= edgeThreshold)
				result.put(n, distance);
			
		}
		
		return result;
	}
	
	
	
	
}

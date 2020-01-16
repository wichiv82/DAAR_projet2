package app;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GrapheJaccard {
	private ArrayList<Node> sommets;

	public GrapheJaccard(ArrayList<String> files, double edgeThreshold) {
		sommets = new ArrayList<Node>();

		for (String filename : files)
			sommets.add(new Node(filename));
		/*
		if(createVoisins)
			for (int i = 0; i < sommets.size(); i++)
				sommets.get(i).setVoisins(sommets, edgeThreshold);
		*/
	}

	public ArrayList<Node> getSommets() {
		return sommets;
	}

	public Node getNode(String nom) {
		for (Node n : sommets)
			if (n.getName().equals(nom))
				return n;
		return null;
	}

	public static double closeness_stream(Node n, ArrayList<Node> nodes) {
		@SuppressWarnings("unchecked")
		ArrayList<Node> documents = (ArrayList<Node>) nodes.clone();
		System.out.println(nodes.indexOf(n));

		// Cas ou le document a etudier est dans la liste
		documents.remove(n);

		double denominateur = documents.stream().parallel().map(x -> x.distanceJaccard(n.getIndex())).reduce(0.0, Double::sum);

		if (denominateur == 0.0)
			return 0;

		return (documents.size()) / denominateur;
	}
	
	public HashMap<String, Double> getAllCloseness(){
		HashMap<String, Double> result = new HashMap<>(
			sommets.stream().parallel().collect(Collectors.toMap(x -> x.getName(), x -> closeness_stream(x, sommets)))
		);

		return result;
	}
	
	/*
	public HashMap<String, Double> getAllCloseness(double[][] tab){
		HashMap<String, Double> result = new HashMap<>();
		
		for(int i=0; i<tab.length; i++) {
			double denominateur = 0.0;
			
			for(int j=0; j<tab.length; j++) {
				if(i!=j) {
					denominateur += tab[i][j];
				}
			}
			
			if(denominateur == 0.0)
				result.put(sommets.get(i).getName(), 0.0);
			else
				result.put(sommets.get(i).getName(), (sommets.size()-1)/denominateur);
		}
		
		return result;
	}
	*/
	
	public List<List<Double>> getAllJaccardDistances(){
		AtomicInteger cpt = new AtomicInteger(0);
		
		List<List<Double>> distances = sommets.stream().parallel().map(
				x -> sommets.stream().parallel().map(voisin -> jaccard(x, voisin)).collect(Collectors.toList())			
		).peek(e -> System.out.println(cpt.getAndIncrement())).collect(Collectors.toList());

		return distances;
	}
	
	public double jaccard(Node x, Node voisin) {
		if(x.equals(voisin)) {
			return -1.0;
		} else {
			double distance = x.distanceJaccard(voisin.getIndex());
			x.addVoisin(voisin, distance);
			return distance;
		}
	}

	public String toString() {
		String result = "";
		for (Node n : sommets) {
			result += n.getName() + "  =>  ";
			HashMap<Node, Double> voisins = n.getVoisins();
			for (Node voisin : voisins.keySet()) {
				result += voisin.getName() + " : " + Math.round(voisins.get(voisin) * 1000.0) / 1000.0 + "  ";
			}
			result += "\n";
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public void toJSONFile_graphe() {
		JSONArray graphe = new JSONArray();
		for (Node n : sommets) {
			JSONObject document = new JSONObject();
			document.put("titre", n.getName());

			JSONArray voisins_json = new JSONArray();
			HashMap<Node, Double> voisins = n.getVoisins();
			for (Node voisin : voisins.keySet()) {
				JSONObject voisin_json = new JSONObject();
				voisin_json.put("nom", voisin.getName());
				voisin_json.put("distance", Math.round(voisins.get(voisin) * 1000.0) / 1000.0);
				voisins_json.add(voisin_json);
			}

			document.put("voisins", voisins_json);
			graphe.add(document);
		}

		try (FileWriter file = new FileWriter("grapheJaccard.json")) {

			file.write(graphe.toJSONString());
			file.flush();
			System.out.println("Graphe JSON produit");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void toJSONFile_indexage() {
		JSONArray index = new JSONArray();
		for (Node n : sommets) {
			JSONObject document = new JSONObject();
			document.put("titre", n.getName());

			JSONArray occurrences_json = new JSONArray();
			HashMap<String, Integer> occurrences = n.getIndex();
			for (String mot : occurrences.keySet()) {
				JSONObject occurrence = new JSONObject();
				occurrence.put("mot", mot);
				occurrence.put("occurrence", occurrences.get(mot));
				occurrences_json.add(occurrence);
			}

			document.put("voisins", occurrences_json);
			index.add(document);
		}

		try (FileWriter file = new FileWriter("indexage.json")) {

			file.write(index.toJSONString());
			file.flush();
			System.out.println("Indexage JSON produit");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

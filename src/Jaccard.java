import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Jaccard {
	
	
	public static HashMap<String, Integer> getOccurences(String filename){
		HashMap<String, Integer> occurences = new HashMap<String, Integer>();
		String line = "";

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			while ((line = br.readLine()) != null) {

				String words[] = line.trim().split("[^a-zA-Z]");
				for(String w : words) {
					if(occurences.containsKey(w))
						occurences.put(w, occurences.get(w) +1);
					else 
						occurences.put(w, 1);
				}
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return occurences;
		
	}
	
	public static double distanceJaccard(HashMap<String, Integer> d1, HashMap<String, Integer> d2) {
		double numerateur = 0;
		double denominateur = 0;
		
		for(String key : d1.keySet()) {
			if(d2.containsKey(key)) {
				int kMax = Math.max(d1.get(key), d2.get(key));
				int kMin = Math.min(d1.get(key), d2.get(key));
				numerateur += kMax - kMin;
				denominateur += kMax;
			}
		}
		
		if(denominateur == 0)
			return 0;
		
		return numerateur/denominateur;
	}
	
	public static void main (String[] args) {
		HashMap<String, Integer> doc1 = getOccurences("test1.txt");
		HashMap<String, Integer> doc2 = getOccurences("test2.txt");
		System.out.println(distanceJaccard(doc2, doc1));
	}
}

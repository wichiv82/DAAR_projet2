package projet1;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RadixTree {

	HashMap<String, RadixTree> fils = new HashMap<String, RadixTree>();
	ArrayList<Point> occurences = new ArrayList<Point>();
    boolean isWord = true;
	String source_textfile_name;
	
	/** Constructeur par defaut **/
	public RadixTree() {}
	
	/** Construction a partir d'un fichier d'indexage **/
	public RadixTree(String indexfilename) throws IOException {
		String line = "";

		try (BufferedReader br = new BufferedReader(new FileReader(indexfilename))) {
			source_textfile_name = indexfilename.split("-")[1];
					
			while ((line = br.readLine()) != null) {
				if (line.isEmpty()) {
					continue;
				}
				
				ArrayList<Point> pos = new ArrayList<Point>();
				
				String[] ligne = line.split(" : ");
				String[] points = ligne[1].split(" ");
				
				for (int i=0; i<points.length; i++) {
					String[] coord = points[i].split(",");
					if(coord.length == 2)
						pos.add(new Point(Integer.parseInt(coord[0]), Integer.parseInt(coord[1])));
				}
				
				addRadixTree(ligne[0], pos);	
			}
			
			br.close();
		}
	}

	/**
	 * Ajoute un mot au RadixTree
	 * @param mot
	 * @param pos
	 */
	public void addRadixTree(String mot, ArrayList<Point> pos) {
		boolean creer_fils = true;
		String k = "";
		String cle = "";
		String suffixe = "";
		
		for (String key : fils.keySet()) {
			char[] lettres = key.toCharArray();
			char[] prefixes = mot.toCharArray();

			int bon_char = 0;
			
			if (mot.equals(key)) {
				fils.get(key).isWord = true;
				fils.get(key).occurences = pos;
				return;
			}

			for (int i = 0; i < lettres.length; i++) {
				
				if (i >= mot.length())
					break;

				if (prefixes[i] == lettres[i]) {
					bon_char++;
					
					if(bon_char == prefixes.length)
						break;
				
				}else if (prefixes[i] != lettres[i] && bon_char > 0) {
					
					RadixTree t = new RadixTree();
					t.isWord = false;
					
					RadixTree u = new RadixTree();
					u.fils = fils.get(key).fils;
					u.occurences = fils.get(key).occurences;
					
					t.fils.put(key.substring(bon_char), u);
					t.occurences = fils.get(key).occurences;
					
					
					RadixTree v = new RadixTree();
					v.occurences = pos;
					t.fils.put(mot.substring(bon_char), v);
					
					fils.put(key.substring(0, bon_char), t);
					
					k = mot.substring(bon_char);
					cle = key.substring(0, bon_char);
					fils.remove(key);
					creer_fils = false;
					return;
				}else {
					break;
				}

			}

			if (bon_char == lettres.length) {
				creer_fils = false;
				k = key;
				suffixe = mot.substring(lettres.length);
				break;
			} else if (bon_char == prefixes.length && mot.length() < key.length()) {
				RadixTree t = new RadixTree();
				String suffixe2 = key.substring(mot.length());
				
				t.occurences = pos;
				t.isWord = isWord;

				fils.put(mot, t);
				t.fils.put(suffixe2, fils.get(key));
				fils.remove(key);

				return;
			}
		}

		if (creer_fils) {
			RadixTree t = new RadixTree();
			t.occurences = pos;
			fils.put(mot, t);
		} else if (cle != "") {
			fils.get(cle).fils.get(k).addRadixTree(suffixe, pos);
			
		} else {
			fils.get(k).addRadixTree(suffixe, pos);
		}

	}

	/**
	 * Affichage textuel
	 * @param prefix
	 * @param profondeur
	 */
	public void affichage(String prefix, int profondeur) {
		for (String key : fils.keySet()) {
			if (fils.get(key).isWord) {
				System.out.print(prefix + "("+key + ") : " + profondeur + " | ");
				for (Point p : fils.get(key).occurences)
					System.out.print(p + " ");
				System.out.println();

			}
			fils.get(key).affichage(prefix + key, profondeur + 1);
		}
	}
	
	/**
	 * Affichage textuel
	 * @param prefix
	 * @param profondeur
	 */
	public void affichage() {
		affichage("", 0);
	}

	/**
	 *  Recherche un mot dans le radixTree et renvoie sa liste de positions des occurences
	 *  Si le mot n'existe pas, renvoie une liste vide
	 * @param mot
	 * @param suffixe
	 * @param temoin
	 * @return
	 */
	public ArrayList<Point> searchMotif(String mot, String suffixe, String temoin) {
		ArrayList<Point> points = new ArrayList<Point>();
		boolean trouve = false;
		
		for (String key : fils.keySet()) {
			
			if((temoin+key).equals(mot)) {
				return fils.get(key).occurences;
			}
			
			if (key.length() <= suffixe.length()) {
				char[] key_array = key.toCharArray();
				char[] suffixe_array = suffixe.toCharArray();
				int bon_char = 0;
	
				for (int i = 0; i < key_array.length; i++) {
					if (key_array[i] == suffixe_array[i]) {
						bon_char++;
					}
					
					if (bon_char == key_array.length) {
						trouve = true;
						temoin += suffixe.substring(0, key_array.length);
						suffixe = suffixe.substring(key_array.length);
						points = (fils.get(key).searchMotif(mot, suffixe, temoin));
						break;
					}
				}
				if (trouve)
					break;
			}
		}
		return points;
	}
	
	public ArrayList<Point> searchMotif(String mot){
		return searchMotif(mot, mot, "");
	}
	

}
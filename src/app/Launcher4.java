package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Launcher4 {
	
	/**
	 * Decoupe un fichier JSON en plusieurs fichiers JSON
	 * pour faire rentrer les donnees dans le Firestore
	 */
	public static void main(String[] args) {
		String path = "Json/";
		String filename = "daar-projet3-indexage.json";
		String section = "indexage";
		
		JSONObject gutenberg = readJSONFile(path, filename, section);
		
		String destinationPath = "Json/Parts/";
		String filenames = "40-indexage";
		String[] others = {"indexage", "closeness"};
		int taillePaquet = 50;
		boolean deleteFolder = true;
		
		writeMultipleJSONFile(gutenberg, destinationPath, filenames, section, others, taillePaquet, deleteFolder);
		
		System.out.println("DONE !");
	}
	
	
	
	public static JSONObject readJSONFile(String path, String filename, String section) {
		JSONParser jsonParser = new JSONParser();
		
		JSONObject obj = new JSONObject();
		
		try (FileReader reader = new FileReader(path + filename)) {
			obj = (JSONObject) ((JSONObject)((JSONObject)jsonParser.parse(reader)).get("Base de Gutenberg")).get(section);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	public static void writeMultipleJSONFile(JSONObject json, String path ,String filenames, String section, String[] others, int taillePaquet, boolean delete) {
		if(delete)
			deleteFolder(new File(path));
		
		int cpt = 0;
		int cpt_file = 1;
		JSONObject buffer = new JSONObject();
		
		for(Object key : json.keySet()) {
			buffer.put((String)key, json.get(key));
			cpt++;
			
			if(cpt%taillePaquet == 0 || cpt == json.keySet().size()) {
				try (FileWriter file = new FileWriter(path + filenames + "_" + cpt_file + ".json")) {
					JSONObject res = new JSONObject();
					
					for(String s : others)
						res.put(s, new JSONObject());
					
					res.put(section, buffer);
					JSONObject base = new JSONObject();
					base.put("Base de Gutenberg", res);
					
		            file.write(base.toJSONString());
		            file.flush();
		            
		            System.out.println("Paquet " + cpt_file + " cree");
		            
		            buffer = new JSONObject();
		 
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				cpt_file++;
			}
		}
	}
	
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { 
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	}
}

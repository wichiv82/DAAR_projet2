package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Egrep {
	
	private ArrayList<String> reponse_egrep;
	
	public Egrep() {
		reponse_egrep = new ArrayList<String>();
	}
	
	public ArrayList<String> getReponse_egrep() {
		return reponse_egrep;
	}
	
	public void egrep(String regEx) {
		Runtime runtime = Runtime.getRuntime();
		String[] commande = {"/bin/sh", "-c", "egrep -l "+ regEx +" *"};
		
		try {
			final Process process = runtime.exec(commande);
			Thread t = new Thread() {
				public void run() {
			        try {
			            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			            String line = "";
			            try {
			            	reponse_egrep.clear();
			                while((line = reader.readLine()) != null) {
			                    reponse_egrep.add(line);
			                }
			            } finally {
			                reader.close();
			            }
			        } catch(IOException ioe) {
			            ioe.printStackTrace();
			        }
			   }
			};
			try {
				t.start();
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestEgrep {
	
	static void egrep(String[] commande) {
		Runtime runtime = Runtime.getRuntime();
		try {
			final Process process = runtime.exec(commande);
			new Thread() {
			    public void run() {
			        try {
			            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			            String line = "";
			            String reponse = "";
			            try {
			                while((line = reader.readLine()) != null) {
			                    reponse += line + "\n";
			                }
			            } finally {
			                reader.close();
			                System.out.println("reponse =  \n" + reponse);
			            }
			        } catch(IOException ioe) {
			            ioe.printStackTrace();
			        }
			    }
			}.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		String[] commande = {"/bin/sh", "-c", "egrep Gutenberg democracy1.txt"};
		//String[] commande = { "/bin/sh", "-c", "ls" };
		//String[] commande = {"/bin/sh", "-c", "cat test1"};
		egrep(commande);
	}
}

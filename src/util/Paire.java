package util;

import java.util.ArrayList;

import app.Node;

public class Paire {
	public Node node;
	public static ArrayList<Node> sommets;
	
	public Paire(Node n, ArrayList<Node> s) {
		this.node = n;
		sommets = s;
	}
}

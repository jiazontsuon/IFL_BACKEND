package com.example.demo.model;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Quantifier {
	private String variable;
	private Set<String> W;

	public Quantifier(String expr) {
		// assert the expr is a valid quantifier e.g. A(x/y,z)
		int LeftBracketPos = 0;
		int RightBracketPos = 0;
		int SlashPos = 0;
		for (int i = 0; i < expr.length(); i++) {
			if (expr.charAt(i) == '(')
				LeftBracketPos = i;
			else if (expr.charAt(i) == '/')
				SlashPos = i;
			else if (expr.charAt(i) == ')')
				RightBracketPos = i;
		}
		this.variable = expr.substring(LeftBracketPos + 1, SlashPos).strip();
		String[] ws = expr.substring(SlashPos + 1, RightBracketPos).strip().split(",");
		W = new HashSet<>();
		if (ws.length != 1 || !ws[0].equals("")) {
			for (String w : ws) {
				W.add(w);
			}
		}
	}

	public String getVariable() {
		return variable;
	}

	public Set<String> getW() {
		return W;
	}

	public String toString() {
		return String.format("Variable: %s, W: %s", this.variable, this.W.toString());
	}

	public static void main(String[] args) {
		String test = "/)";
		System.out.println(Arrays.toString(test.substring(1, 1).split(",")));
		System.out.println(Arrays.toString("1234".split(",")));

		String test1 = "A(x/a,b,c,  d ,zasd)";
		Quantifier q = new Quantifier(test1);
		System.out.println(q.toString());
		
		Set<String> x = q.getW();
		for (String s : x) {
			System.out.println(s.length());
		}

	}

}

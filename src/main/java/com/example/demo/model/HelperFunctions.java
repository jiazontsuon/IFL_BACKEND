package com.example.demo.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperFunctions {
	
	
	public static List<Map<String,String>> expand(List<Map<String,String>> assignments,String variable, List<String> vals){
		List<Map<String,String>> expandedAssignments = new ArrayList<>();
		if (assignments.size() == 0) {
			assignments.add(new HashMap<>());
		}
		for(Map<String,String> m:assignments){			
			for(String val : vals) {
				// make a deep copy of the map
				Map<String,String> deepcopy = new HashMap<>(m);
				deepcopy.put(variable,val);
				expandedAssignments.add(deepcopy);
			}
		}
		return expandedAssignments;
	}
	
	public static void removeAssignments(List<Map<String,String>> assignments, String variable) {
		for (Map<String,String> m : assignments) {
			assert(m.containsKey(variable));
			m.remove(variable);
		}
	}
	
	public static String toString(List<Map<String,String>> assignments) {
		String s = "";
		for (Map<String,String> m: assignments) {
			s+=m.toString()+"\n";
		}
		return s;
	}
	
	/**
	 * @param expr The whole expression in where the quantifier is
	 * @param start index of the first element in the quantifier
	 * @param end   (index of the last element in the quantifier) + 1
	 * @return int[]{x1,x2} x1 is the index of the left bracket while x2 is the index of the right bracket.
	 * return null if the quantifier is not wrapped by a bracket.
	 */
	public static int[] quantifiersBracket(String expr, int start, int end) {
		int[] res = new int[2];
		
		// find res[0]: the index of the left bracket
		int ptr1 = start-1;
		int numOfLeftBracket = 0;
		while (ptr1 >= 0) {
			if (expr.charAt(ptr1) == '(') numOfLeftBracket++;
			else if (expr.charAt(ptr1) == ')') numOfLeftBracket--;
			if (numOfLeftBracket == 1) {
				res[0] = ptr1;
				break;
			}
			ptr1--;
		}
		
		// find res[1]: the index of the right bracket
		ptr1 = end;
		numOfLeftBracket = 0;
		while (ptr1<expr.length()) {
			if (expr.charAt(ptr1) == '(') numOfLeftBracket++;
			else if (expr.charAt(ptr1) == ')') numOfLeftBracket--;
			if (numOfLeftBracket == -1) {
				res[1] = ptr1;
				break;
			}
			ptr1++;
		}
		return (res[0] == res[1])? null : res;
	}
	
	public static int findRightBracket(String expr, int leftBracketIdx) {
		int count = 1;
		for (int i = leftBracketIdx +1;i<expr.length();i++) {
			if (expr.charAt(i) == '(') count++;
			else if (expr.charAt(i) == ')') count--;
			if (count == 0) return i;
		}
		return -1;
	}
	public static int findPosOfCorrespondingLeftBracket(String str,int pos) {
		// indicates the idx of the right bracket in str
		int rightBracketCount = 1;
		
		for (int i = pos-1;i>=0;i--) {
			if (str.charAt(i) == '(') {
				if (rightBracketCount == 1) return i;
				rightBracketCount--;
			}
			else if (str.charAt(i) == ')') {
				rightBracketCount++;
			}
		}
		return -1;
	}
	
	public static int findNearestConnectivesOnTheLeft(String str, int pos) {
		// if not found return -1;
		int ptr = pos-1;
		while (ptr>=0 && str.charAt(ptr)!='V' && str.charAt(ptr)!='&' && str.charAt(ptr)!='>') {
			ptr--;
		}
		return ptr;
	}
	
	public static int findNearestConnectivesOnTheRight(String str,int pos) {
		int ptr = pos+1;
		while(ptr<str.length() && str.charAt(ptr)!='V' && str.charAt(ptr)!='&') {
			ptr++;
		}
		return ptr;
	}
	
	public static boolean hasQuatifier(String expr) {
		String QuantifierRegex = "[A|E]\\([a-z][0-9]*/([a-z][0-9]*,)*[a-z]*[0-9]*\\)";
		Pattern hasQuantifier = Pattern.compile(QuantifierRegex);
		Matcher m = hasQuantifier.matcher(expr);
		return m.find();
	}
	
	public static int getTypeForConnectives(char c) {
		switch(c) {
			case 'V':
				return ExprStructure.type3;
			case '&':
				return ExprStructure.type4;
			case '>':
				return ExprStructure.type7;
		}
		return  -1;
	}
	
	
	
	
	public static <T> void subset(List<T> x){
		int n = x.size();
		
		for (int i = 0;i<(1<<n);i++) {		
			for (int j = 0;j<n;j++) {
				if (((i>>j)&1) == 1) {
					System.out.print(x.get(j));
				}
			}
			System.out.println();
		}
		
	}
	
	public static void main(String[] args) {
//		List<Integer> ls = new ArrayList<>();
//		ls.add(1);
//		ls.add(2);
//		ls.add(3);
//		
//		subset(ls);s
//		String str = "P(a,s) V ( E(y/) P(y,1) ) & P(x,z)";
//		Pattern hasQuantifier = Pattern.compile("[A|E]\\([a-z]/([a-z],)*[a-z]*\\)");
//		Matcher m3 = hasQuantifier.matcher(str);
//		if (m3.find()) {
//			int[] res = quantifiersBracket(str,m3.start(),m3.end());
//			System.out.println(str.substring(res[0],res[1]+1));
//		}
		String test = "  (E(y/x) Equal(x,y))";
		System.out.println(hasQuatifier(test));
	}

}

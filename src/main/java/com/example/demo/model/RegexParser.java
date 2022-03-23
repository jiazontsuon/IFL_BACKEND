package com.example.demo.model;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParser {
	
	public String relationRegex;
	public String universeRegex;
	private String QuantifierRegex = "[A|E]\\([a-z][0-9]*/([a-z][0-9]*,)*[a-z]*[0-9]*\\)";
	
	
	public RegexParser(String relationRegex,String universeRegex) {
		this.relationRegex = relationRegex;
		this.universeRegex = universeRegex;
	}
	
	
	public ExprStructure Parse(String str) throws Exception {
		/*
		 * return the structure of the logic expression
		 */
		//System.out.println("Evaluating "+ str);
		String expr = str.strip();
		String subExpr1 = "";
		String subExpr2 = "";
		int type = -1;
		
		//the expr is of the form: "(....)"
		if (expr.charAt(0) == '(') {
			int rightBracketIdx = HelperFunctions.findRightBracket(expr, 0);
			// trivial bracket
			if (rightBracketIdx == expr.length()-1) return Parse(expr.substring(1,rightBracketIdx));
//			int rightConnective = HelperFunctions.findNearestConnectivesOnTheRight(expr,rightBracketIdx);
//			subExpr1 = expr.substring(1, rightBracketIdx);
//			subExpr2 = expr.substring(rightConnective+1);
//			type = expr.charAt(rightConnective) == 'V' ? ExprStructure.type3 : ExprStructure.type4;
//			return new ExprStructure(subExpr1,subExpr2,type);
		}
		// the expr is of the form: "~(...)"
		Pattern negationOfFormula = Pattern.compile("^~\\s*\\(.*\\)$");
		Matcher m0 = negationOfFormula.matcher(expr);
		if (m0.find()) {
			int leftBracketIdx = expr.indexOf('(');
			int rightBracketIdx = HelperFunctions.findRightBracket(expr, leftBracketIdx);
			if (rightBracketIdx == expr.length()-1) {
				subExpr1 = expr.substring(leftBracketIdx+1,rightBracketIdx);
				return new ExprStructure(subExpr1,subExpr2,ExprStructure.type6);
			}
		}
		
		// the expr is a literal
		Pattern isSingleRelation = Pattern.compile(String.format("^~{0,1}%s\\((\\w+,)*\\w*\\)$",relationRegex));
		Matcher m1 = isSingleRelation.matcher(expr);
		if (m1.find()) {
//			System.out.println(String.format("%s is a literal", expr));
			subExpr1 = expr.substring(m1.start(), m1.end());
			return new ExprStructure(subExpr1,subExpr2,ExprStructure.type5);
		}
		// the expr is of form [A|E](../...) phi
		Pattern beginWithQuantifier = Pattern.compile(String.format("^%s", QuantifierRegex));
		Matcher m2 = beginWithQuantifier.matcher(expr);
		
		if (m2.find()) {
			if (expr.charAt(0)=='A') {
				type = ExprStructure.type2;
			}
			else {
				type = ExprStructure.type1;
			}
			subExpr1 = expr.substring(m2.start(), m2.end());
			subExpr2 = expr.substring(m2.end());
			return new ExprStructure(subExpr1,subExpr2,type);
		}
		
		// the expr is of the form: expr1 [V|&|>] expr2
		// case 1: expr1 [V|&|>] Q(/) ...
		Pattern hasQuantifier = Pattern.compile(QuantifierRegex);
		Matcher m3 = hasQuantifier.matcher(expr);
		while (m3.find()) {
			// Consider this scenerio: expr1 [V|&|>] (...Q(/)...)
			int[] ptrs = HelperFunctions.quantifiersBracket(expr, m3.start(), m3.end());
			// The quantifier is not wrapped in a bracket
			if (ptrs == null) {
				int connectivePos = HelperFunctions.findNearestConnectivesOnTheLeft(expr,m3.start());
				subExpr1 = expr.substring(0,connectivePos);
				subExpr2 = expr.substring(m3.start());
				type = HelperFunctions.getTypeForConnectives(expr.charAt(connectivePos));
				return new ExprStructure(subExpr1,subExpr2,type);
			}
//			// the bracket wraps the whole expr
//			if (ptrs[0] == 0 && ptrs[1]+1 == expr.length()) {
//				return Parse(expr.substring(1,ptrs[1]));
//			}
			// the expr is of the form: ".... (...Q(/)...)"
			if (ptrs[1]+1 == expr.length()) {
				int connectivePos = HelperFunctions.findNearestConnectivesOnTheLeft(expr,ptrs[0]);
				subExpr1 = expr.substring(0,connectivePos);
				subExpr2 = expr.substring(ptrs[0]+1,ptrs[1]);
				type = HelperFunctions.getTypeForConnectives(expr.charAt(connectivePos));
				return new ExprStructure(subExpr1,subExpr2,type);
			}
			
		}
		
		// case 2: expr1 [V|&|>] R(..,..,....,..)
//		System.out.println(String.format("^~{0,1}\\s*%s\\((\\w+,)*\\w+\\)$",relationRegex));
		Pattern LastTermIsRelation = Pattern.compile(String.format("~{0,1}\\s*%s\\((\\w+,)*\\w+\\)$", relationRegex));
		Matcher m4 = LastTermIsRelation.matcher(expr);
		if (m4.find()) {
			int connectivePos = HelperFunctions.findNearestConnectivesOnTheLeft(expr,m4.start());
			subExpr1 = expr.substring(0,connectivePos);
			subExpr2 = expr.substring(m4.start());
			type = HelperFunctions.getTypeForConnectives(expr.charAt(connectivePos));
			return new ExprStructure(subExpr1,subExpr2,type);
		}
		
		// case 3: expr1 [V|&|>] (expr2)
		if (expr.charAt(expr.length()-1) == ')') {
			int leftBracketPos = HelperFunctions.findPosOfCorrespondingLeftBracket(expr,expr.length()-1);
			int connectivePos = HelperFunctions.findNearestConnectivesOnTheLeft(expr,leftBracketPos);
			subExpr1 = expr.substring(0, connectivePos);
			subExpr2 = expr.substring(leftBracketPos+1,expr.length()-1);
			type = HelperFunctions.getTypeForConnectives(expr.charAt(connectivePos));
			return new ExprStructure(subExpr1,subExpr2,type);
		}
		
		throw new Exception();
		// System.out.println("Evaluating "+ str + " END!");
	}
	
//	public static String ObtainRelationRegex(Map<String,String> map) {
//		StringBuilder sb = new StringBuilder();
//		Set<String> keyset = map.keySet();
//		if (keyset.size()>0) {
//			sb.append('[');
//			for (String str : keyset) {
//				sb.append(str+"|");
//			}
//			sb.replace(sb.length()-1, sb.length(), "]");
//		}
//		return sb.toString();
//	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		RegexParsing rgp = new RegexParsing();
		System.out.println(Model.getRelationRegex());
		Stack<Integer>s2 = new Stack<>();
		


	}

}

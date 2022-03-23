package com.example.demo.model;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Evaluator {
	Stack<String> messageStack = new Stack<>();
	RegexParser RP = new RegexParser(Model.getRelationRegex(),Model.getUniverseRegex());
	Map<String,ExprStructure> parsingHistory = new HashMap<>();
	Model model;
	
	public Evaluator(Model m) {
		model = m;
	}
	
	
	public EvaluationResult Evaluate(String expr) {
		messageStack = new Stack<>();
		List<Map<String,String>> initialAssignments = new ArrayList<>();
		String player = "Eloise";
		boolean resultEloise = Evaluate(expr,initialAssignments,player,true,false);
		if (resultEloise) {
			return new EvaluationResult(player,messageStack);
		}
		// reset variables
		messageStack.clear();
		initialAssignments.clear();
		player = "Abelard";
		boolean resultAbelard = Evaluate(expr,initialAssignments,player,true,true);
		if (resultAbelard) {
			return new EvaluationResult(player,messageStack);
		}
		// no one has a winning strategy
		return new EvaluationResult("No one",messageStack);
	}
	
	private ExprStructure parse(String expr) throws Exception {
		
		// parsingHistory plays role of cache
		if (parsingHistory.containsKey(expr)) {
			return parsingHistory.get(expr);
		}
		ExprStructure es = RP.Parse(expr);
		parsingHistory.put(expr,es);
		return es;
	}
	
	public boolean Evaluate(String expr,List<Map<String,String>> assignments,String player,boolean containsQuantifier,boolean isNegation){
		try {
			ExprStructure es = parse(expr);
			String expr1 = es.getSubExpr1();
			String expr2 = es.getSubExpr2();
			int type = es.getType();
			
			if (isNegation) {
				// negate subexpressions
//				expr1 = "~("+expr1+")";
//				expr2 = "~("+expr2+")";
				// change connectives
				if (type == ExprStructure.type1){
					type = ExprStructure.type2;
				}
				else if (type == ExprStructure.type2) {
					type = ExprStructure.type1;
				}
				else if (type == ExprStructure.type3) {
					type = ExprStructure.type4;
				}
				else if (type == ExprStructure.type4) {
					type = ExprStructure.type3;
				}
				else if (type == ExprStructure.type6) {
					return Evaluate(expr1,assignments,player,containsQuantifier,!isNegation);
				}
			}
			
			// expr is a literal
			if (type == ExprStructure.type5) {
				return EvaluateLiteralUnderTeamOfAssignments(expr1,assignments,RP.relationRegex,isNegation);
			}
			if (containsQuantifier && !HelperFunctions.hasQuatifier(expr)) {
				// the expression contains no quantifiers
				return Evaluate(expr,assignments,player,false,isNegation);
			}
			// expr = E(../..) expr2
			else if (type == ExprStructure.type1) {
				Quantifier q = new Quantifier(expr1);
				
				// partition team of assignments into groups according to their W-uniformity
				WFunctionIterator WFIterator = new WFunctionIterator(assignments,q.getVariable(),model.getUniversal_List(),q.getW());
				
				while(WFIterator.hasNext()){
					List<Map<String,String>> newAssignments = WFIterator.get();
					if (Evaluate(expr2,newAssignments,player,containsQuantifier,isNegation)) {
						this.messageStack.add(HelperFunctions.toString(newAssignments));
						this.messageStack.add(String.format("It is %s's turn to choose %s which is independent from %s\n",player, q.getVariable(),q.getW().toString()));
						HelperFunctions.removeAssignments(newAssignments,q.getVariable());
						return true;
					}
					HelperFunctions.removeAssignments(newAssignments,q.getVariable());
				}
				this.messageStack.clear();
				return false;
			}
			// expr = A(../..) expr2
			else if (type == ExprStructure.type2){

				Quantifier q = new Quantifier(expr1);
				List<Map<String,String>> expandedAssignments = HelperFunctions.expand(assignments, q.getVariable(), model.getUniversal_List());
				return Evaluate(expr2,expandedAssignments,player,containsQuantifier,isNegation);
			}
			// expr = expr1 v expr2
			else if (type == ExprStructure.type3) {
				if (!containsQuantifier) {
					List<Map<String,String>> ls = new ArrayList<>();
					for (Map<String,String> assignment:assignments) {
						ls.clear();
						ls.add(assignment);
						boolean expr1Res = Evaluate(expr1,ls,player,containsQuantifier,isNegation);
						if (expr1Res) continue;
						boolean expr2Res = Evaluate(expr2,ls,player,containsQuantifier,isNegation);
						if (!expr2Res) return false;
					}
					return true;
				}
				SubsetIterator<Map<String,String>> subSetIterator = new SubsetIterator<>(assignments);
				while(subSetIterator.hasNext()) {
					List[] partitions = subSetIterator.get();
					if (Evaluate(expr1,partitions[0],player,containsQuantifier,isNegation) && Evaluate(expr2,partitions[1],player,containsQuantifier,isNegation)) {
						return true;
					}
				}
				return false;
			}
			// expr = expr1 & expr2
			else if (type == ExprStructure.type4) {	
				return Evaluate(expr2,assignments,player,containsQuantifier,isNegation) && Evaluate(expr1,assignments,player,containsQuantifier,isNegation);
			}
			// expr = expr1 > expr2
			else if (type == ExprStructure.type7) {
				String newExpr = "~("+expr1+") V ("+expr2 + ")";
//				String newExpr = "~"+expr1+" V ("+expr2 + ")";
				return Evaluate(newExpr,assignments,player,containsQuantifier,isNegation);
			}
//			// expr = ~ expr1
			else if (type == ExprStructure.type6) {
				return Evaluate(expr1,assignments,player,containsQuantifier,!isNegation);
			}
			throw new Exception();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean EvaluateLiteralUnderTeamOfAssignments(String literal, List<Map<String,String>> assignments,String relationRegex,boolean negated) {
		
		Pattern p = Pattern.compile(relationRegex);
		Matcher m = p.matcher(literal);
		
		boolean negation = negated?!literal.contains("~"):literal.contains("~");
		int posOfLeftBracket = literal.indexOf("(");
		String relation = m.find() ? literal.substring(m.start(),m.end()):"";
		RelationMapping rm = model.getRelation_mappings().get(relation);
		String parameters = literal.substring(posOfLeftBracket+1,literal.lastIndexOf(")"));
		
		for (Map<String,String> assignment: assignments) {
			if (negation) {
				if (rm.evaluate(parameters, assignment)) {
					return false;
				}
			}
			else {
				if (!rm.evaluate(parameters, assignment)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		String literal = " Real(1,2)";
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.txt"));  
        Model m = (Model)in.readObject();  
        in.close();
		Evaluator e = new Evaluator(m);
//		e.Evaluate(literal);
		
//		String expr1 = "A(x/) E(y/x) Equal(x,y) V Less(x,y) V Equal(y,x)";
//		e. Evaluate(expr1);
//		
//		String expr2 = "A(x/) A(y/) Equal(x,y)";
//		e.Evaluate(expr2);
//		
//		String expr3 = "E(x/) A(y/x) (Less(y,x) V E(z/x) Equal(y,z) )";
//		e.Evaluate(expr3);
//		
//		String expr4 = "E(x/) A(y/) Less(y,x) V A(a/x,z) H(1,y) V H(3,a)";
//		e.Evaluate(expr4);
//		
//		String expr5 = "A(x/)A(y/) (~Equal(x,y) V E(z/x) Equal(x,z))";
//		e.Evaluate(expr5);
//		
		String expr6 = "A(x/) (E(y/x) Equal(x,y)) V (E(z/x) Equal(x,z)) V (E(e/x) Equal(x,e)) V (E(f/x) Equal(x,f))";
		e.Evaluate(expr6);
//		

//		String expr7 = "A(x/)A(y/)E(u/y)E(v/x,u) (Equal(x,y) > Equal(u,v)) & (Equal(u,y) > Equal(v,x)) & ~Equal(x,u)";
//		e.Evaluate(expr7);
	}

}

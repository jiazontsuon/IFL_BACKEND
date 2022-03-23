package com.example.demo.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WFunctionIterator {
	
	private final List<List<Map<String,String>>> partitions;
	private final String variableToAssign;
	private final List<String> universe;
	private final int n;
	private long start;
	private final long end;
	private final int[] assignmentDict;
	private final List<Map<String,String>> assignments;
	
	public WFunctionIterator(List<Map<String,String>> assignments, String variableToAssign, List<String> universe,Set<String> W) {
//		this.partitions = partitions;
		this.variableToAssign = variableToAssign;
		this.universe = universe;
		this.n = universe.size();
		this.assignments = assignments;
		if (assignments.isEmpty()) {
			this.assignments.add(new HashMap<String,String>());
		}
		this.partitions = partition(assignments,W);
				
		this.start = 0;
		this.end = (long) Math.pow(this.n, partitions.size());
		
		// assignmentDict[i] = j implies that 
		// the ith partition has the following additional assignment : variableToAssign = universe[j]
		assignmentDict = new int[partitions.size()];
		assignmentDict[0] = -1;
	}
	
	public boolean hasNext() {
		return start<end;
	}
	
	public List<Map<String,String>> get(){
		assert(hasNext());
		// update the assignmentDict
		update(0);
		
		// Re-assign the variable through the new assignmentDict
		for (int i = 0; i < assignmentDict.length;i++) {
			List<Map<String,String>> group = partitions.get(i);
			
			for (Map<String,String> g: group) {
				g.put(this.variableToAssign, this.universe.get(this.assignmentDict[i]));
			}
		}
		start++;
		return this.assignments;
	}
	
	private void update(int idx) {
		if (assignmentDict[idx]+1< n) {
			assignmentDict[idx]++;
		}
		else {
			assignmentDict[idx] = 0;
			update(idx+1);
		}
	}
	public static boolean isWUniform(Map<String,String> a1, Map<String,String> a2, Set<String> W) {
		/*
		 * This function checks if a1 ~W a2, which implies that a1 and a2 are exactly the same 
		 * without considering variables in W
		 */
		// we assume a1 and a2 have the same number of variables to assign <-> a1.keySet() == a2.keySet()
		for (String var : a1.keySet()) {
			if (!W.contains(var)) {
				if (!a1.get(var).equals(a2.get(var))) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static List<List<Map<String,String>>> partition(List<Map<String,String>> assignments, Set<String> W){
		List<List<Map<String,String>>> res = new ArrayList<>();
		
		for (Map<String,String> m: assignments) {
			boolean hasFoundGroup = false;
			for (List<Map<String,String>> group : res) {
				Map<String,String> a1 = group.get(0);
				Map<String,String> a2 = m;
				if (isWUniform(a1,a2,W)) {
					hasFoundGroup = true;
					group.add(a2);
					break;
				}
			}
			if (!hasFoundGroup) {
				List<Map<String,String>> group = new ArrayList<>();
				group.add(m);
				res.add(group);
			}
		}
		return res;	
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long x = 1;
		for (int i = 0;i<35;i++) {
			x = (x<<1);
		}
		System.out.println(x);
		long start = System.currentTimeMillis();
		long a = 0;
		for (long i = 0;i<x;i++) {
			a+=1;
		}
		long end = System.currentTimeMillis();
		System.out.println(a);
		System.out.println(start-end);

	}

}

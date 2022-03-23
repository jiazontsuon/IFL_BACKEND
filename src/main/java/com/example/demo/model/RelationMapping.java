package com.example.demo.model;


import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class RelationMapping implements Serializable {
	
	private static final long serialVersionUID = 1L;
	Set<String> map = new HashSet<>();
	public RelationMapping(String mapping) {
		// e.g. mapping = "{1,2},{2,3},{5,6}"
		map = extract(mapping);
	}
	
	public boolean evaluate(String parameters, Map<String,String> assignment){
	
		//the parameters is of the form "x1,x2,...,xn"
		String[] strs = parameters.split(",");
		
		for (int i = 0 ;i<strs.length;i++) {
			strs[i] = assignment.getOrDefault(strs[i], strs[i]);
		}
		
		return map.contains(String.join(",", strs));
	}
	
	private Set<String> extract(String str) {
		/*
		 * This fucntion extracts the sets of the realtion's inputs from the text
		 * e.g str = "{1,2},{3,4},{5,6}"
		 * 	   return  ["1,2","3,4","5,6"] 
		 */
		
		// assert the str is in valid form
		// initial capacity = 20
		Set<String> hs = new HashSet<>(20);
		int leftBracketPos = 0;
		
		for (int i =0;i<str.length();i++) {
			if (str.charAt(i) == '{') {
				leftBracketPos = i;
			}
			else if (str.charAt(i) == '}') {
				hs.add(str.substring(leftBracketPos+1, i ));	
			}
		}
		return hs;
	}
	
	public static void main(String[] args) {
		String mapping = "{1,2,3},{4,5,6},{974,184,234},{324,43,54},{23,756,253},{23,45,656},{235,46,546}";
		long start = System.currentTimeMillis();
		RelationMapping rm = new RelationMapping(mapping);
		Map<String,String> hm = new HashMap<>(){
			{
				put("x","43");
				put("y","54");
				put("u","324");
			}
		};
		for (int i =0;i<200;i++) {
			rm.evaluate("y,756,5",hm);
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		System.out.println(rm.evaluate("u,x,1",hm));
	}
}

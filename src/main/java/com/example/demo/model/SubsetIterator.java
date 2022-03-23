package com.example.demo.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubsetIterator<T> {
	private int total;
	private int count;
	private int numOfAssignments;
	private List<T> assignments;
	
	public SubsetIterator(List<T> assignments) {
		count = 0;
		this.numOfAssignments = assignments.size();
		total = 1<<numOfAssignments;
		this.assignments = assignments;
	}
	
	public boolean hasNext() {
		return count< total;
	}
	
	public List[] get(){
		assert(hasNext());
		List[] res = new List[2];
		List<T> ls1 = new ArrayList<>();
		List<T> ls2 = new ArrayList<>();
		
		for (int i = 0;i<numOfAssignments;i++) {
			if (((count>>i)&1) == 1) {
				ls1.add(assignments.get(i));
			}
			else {
				ls2.add(assignments.get(i));
			}
		}
		count++;
		res[0] = ls1;
		res[1] = ls2;
		return res;
	}
	
	public static void main(String[] args) {
		List<Integer> ls = new ArrayList<>();
		ls.add(0);
		ls.add(1);
		ls.add(2);
		
		SubsetIterator<Integer> si = new SubsetIterator<>(ls);
		while(si.hasNext()) {
			List[] lss = si.get();
			List<Integer> ls1 = lss[0];
			List<Integer> ls2 = lss[1];
			System.out.println(String.format("ls1: %s; ls2: %s", ls1.toString(),ls2.toString()));
			
		}

	}
}

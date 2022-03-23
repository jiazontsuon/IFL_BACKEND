package com.example.demo.model;

import java.util.Stack;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class EvaluationResult {
	
	private String winner;
	private Stack<String> ms;
	public EvaluationResult(String player, Stack<String> messageStack) {
		winner = player;
		ms = messageStack;
		System.out.println(winner+" has a winning strategy!!");
		// while(!messageStack.isEmpty()) {
		// 	System.out.print(messageStack.pop());
		// }
	}
	
	public String toString(){
		String res = String.format("%s has a winning strategy\n", this.winner);
		while (!this.ms.isEmpty()){
			res+=ms.pop();
		}
		return res;
	}
	public EvaluationResult(String player) {
		winner = player;
		System.out.println(winner+" has a winning strategy!!");
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((winner == null) ? 0 : winner.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EvaluationResult other = (EvaluationResult) obj;
		if (winner == null) {
			if (other.winner != null)
				return false;
		} else if (!winner.equals(other.winner))
			return false;
		return true;
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Stack<String> s = new Stack<>();
		EvaluationResult er = new EvaluationResult("Absd",s);
		EvaluationResult er1 = new EvaluationResult("Absdsd",s);
		System.out.println(er.equals(er1));
		
	}

}

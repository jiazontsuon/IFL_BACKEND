package com.example.demo.model;

/*
 * The types of the expression structure:
 * 1. E(..) expr1
 * 2. A(..) expr1
 * 3. expr1 V expr2
 * 4. expr1 & expr2
 * 5. literal which is also a single relation function in this case, e.g. R(x,y)
 * 6. ~(..)
 * 7. expr1 > expr2
 */
public class ExprStructure {
	public static final int type1 = 1;
	public static final int type2 = 2;
	public static final int type3 = 3;
	public static final int type4 = 4;
	public static final int type5 = 5;
	public static final int type6 = 6;
	public static final int type7 = 7;
	private final String subExpr1;
	private final String subExpr2;
	private int type;
	
	public ExprStructure(String subExpr1,String subExpr2, int type) {
		this.subExpr1 = subExpr1;
		this.subExpr2 = subExpr2;
		this.type = type;
	}

	@Override
	public String toString() {
		return "ExprStructure [subExpr1=" + subExpr1 + ", subExpr2=" + subExpr2 + ", type=" + type + "]";
	}

	public String getSubExpr1() {
		return subExpr1;
	}

	public String getSubExpr2() {
		return subExpr2;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subExpr1 == null) ? 0 : subExpr1.hashCode());
		result = prime * result + ((subExpr2 == null) ? 0 : subExpr2.hashCode());
		result = prime * result + type;
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
		ExprStructure other = (ExprStructure) obj;
		if (subExpr1 == null) {
			if (other.subExpr1 != null)
				return false;
		} else if (!subExpr1.equals(other.subExpr1))
			return false;
		if (subExpr2 == null) {
			if (other.subExpr2 != null)
				return false;
		} else if (!subExpr2.equals(other.subExpr2))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	

}

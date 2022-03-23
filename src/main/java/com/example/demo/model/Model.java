package com.example.demo.model;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Model implements Serializable{

	private static final long serialVersionUID = 1L;
	public List<String> universal_List;
	// @JsonIgnore
	@JsonSerialize
	public Map<String,RelationMapping> relation_mappings;
	
	public Model(List<String> universal_List,Map<String,RelationMapping> relation_mappings) {
		this.universal_List = universal_List;
		this.relation_mappings = relation_mappings;
	}
	
	public List<String> getUniversal_List() {
		return universal_List;
	}

	public Map<String, RelationMapping> getRelation_mappings() {
		return relation_mappings;
	}

	public void setUniversal_List(List<String> universal_List) {
		this.universal_List = universal_List;
	}

	public void setRelation_mappings(Map<String, RelationMapping> relation_mappings) {
		this.relation_mappings = relation_mappings;
	}

	public static List<String> universalList = new ArrayList<>(){
		{
			add("1");
			add("2");
			add("3");
			add("4");
//			add("5");
//			add("6");
		}
	};
	public static Map<String,RelationMapping> relationMappings = new HashMap<>(){
		{
			put("Less",new RelationMapping("{1,2},{2,3},{3,4},{1,3},{1,4},{2,4}"));
			put("H",new RelationMapping("{1,4,1},{1,2,5}")); //H(1,4,1) = true 
			put("F",new RelationMapping("{1,2,3},{2,2,3}"));
			put("IsEven",new RelationMapping("{2},{4}"));
			put("IsOdd",new RelationMapping("{1},{3}"));
			put("Equal",new RelationMapping("{2,2},{1,1},{3,3},{4,4}"));
			put("R",new RelationMapping("{2,3},{3,2}"));
			put("P",new RelationMapping("{1},{2},{4}"));
			put("Q",new RelationMapping("{1},{2},{3}"));
			put("Edge",new RelationMapping("{1,3},{3,1},{2,3},{3,2},{1,2},{2,1},"
					+ "{3,4},{4,3}"));
		}
	};
	
	public static String EnumerateElemRegex(Iterable<String> ls) {
		StringBuilder sb = new StringBuilder();
		boolean flag = false;
		for (String str : ls) {
			if (!flag) {
				flag = true;
				sb.append("(");
			}
			sb.append(str+"|");
		}
		if (flag) {
			sb.replace(sb.length()-1, sb.length(), ")");
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return "Model [universal_List=" + universal_List.toString() + ", relation_mappings=" + relation_mappings.toString() + "]";
	}

	public static String getRelationRegex() {
		return EnumerateElemRegex(relationMappings.keySet());
	}
	
	public static String getUniverseRegex() {
		return EnumerateElemRegex(universalList);
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		Model m = new Model(Model.universalList,Model.relationMappings);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("perfectMatching4Vertices.txt"));  
        out.writeObject(m);  
        out.close();  
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("perfectMatching4Vertices.txt"));  
        Model m2 = (Model)in.readObject();  
        in.close();  
        
        System.out.println("After:\n" + m2.relation_mappings.get("Equal").map.toString());  
        System.out.println("After:\n" + m2.getUniversal_List().toString());  
		
	}
}

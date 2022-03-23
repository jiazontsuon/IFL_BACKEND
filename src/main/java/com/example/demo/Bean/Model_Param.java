package com.example.demo.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.model.EvaluationResult;
import com.example.demo.model.Evaluator;
import com.example.demo.model.Model;
import com.example.demo.model.RelationMapping;

public class Model_Param {
    public String expression;
    public String domain;
    public ArrayList<Relation> domains;
    

    public Result<String> eval(){
        // universe
        String[] elems = this.domain.split("\\s*,\\s*");
        ArrayList<String> universe = new ArrayList<>();
        for (String e : elems){
            universe.add(e);
            System.out.println(e);
        }

        
        // relations
        Map<String,RelationMapping> relationMappings = new HashMap<>();
        for (Relation r : this.domains) {
            String relation_name = r.func_name.strip();
            String relation_content = r.val.strip();
            if (relation_name.length()>0){
                relationMappings.put(relation_name,new RelationMapping(relation_content));
            }
        }

        Model.universalList = universe;
        Model.relationMappings = relationMappings;

        Model m = new Model(universe, relationMappings);
        Evaluator e = new Evaluator(m);
        EvaluationResult evalRes = e.Evaluate(expression);
        return Result.buildResult(Status.OK, evalRes.toString());
    }

    @Override
    public String toString(){
        String res =  "Expression: "+ expression+ " Universe: "+ domain + "Relations: ";
        for (Relation r : domains){
            res += (r.toString() + "\n");
        }
        return res;
    }
}

package com.example.demo.controller;

import com.example.demo.Bean.Model_Param;
import com.example.demo.Bean.Result;
import com.example.demo.Bean.Status;
import com.example.demo.model.EvaluationResult;
import com.example.demo.model.Evaluator;
import com.example.demo.model.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @GetMapping("/hello")
    public Result index() {
        Result u = Result.buildResult(Status.ERROR);
        return u;
    }   
    @GetMapping("/model")
    public Model getModel(){
        Model m = new Model(Model.universalList,Model.relationMappings);
        return m;
    }
    @PostMapping("/evaluate")
    public Result<String> evaluate(@RequestBody Model_Param mp){
        try {
            // System.out.println(expr);
            // return Result.buildResult(Status.OK);
            System.out.println(mp.toString());
            return mp.eval();
        }
        catch(Exception e){
            // e.printStackTrace();
            System.out.println("Error");
			return Result.buildResult(Status.ERROR);
        }
        
    }

}
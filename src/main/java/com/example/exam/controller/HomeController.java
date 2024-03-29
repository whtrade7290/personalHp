package com.example.exam.controller;

import com.example.exam.Model.InfoModel;


import com.example.exam.service.InfoService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Log
@Controller
public class HomeController {

//테스트
    @Autowired
    private InfoService infoService;


    @RequestMapping("/")
    public String index(Model model, InfoModel infoModel) {

        infoModel = infoService.getInfo();
        model.addAttribute("infoModel", infoModel);


        return "index";
    }

}

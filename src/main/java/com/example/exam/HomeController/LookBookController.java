package com.example.exam.HomeController;

import com.example.exam.Model.LookbookModel;
import com.example.exam.service.LookbookService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Log
@Controller
public class LookBookController {

    @Autowired
    private LookbookService lookbookService;

    @RequestMapping("/lookbook")
    public String lookbook(Model model) {
        log.info("lookbook() 호출");

        List<LookbookModel> lookbooks = lookbookService.selectLookbook();

        model.addAttribute("lookbooks", lookbooks);



        return "/menu/lookbook";
    }
}
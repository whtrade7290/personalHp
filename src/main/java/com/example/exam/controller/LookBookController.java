package com.example.exam.controller;

import com.example.exam.Model.LookbookModel;
import com.example.exam.service.LookbookService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Log
@Controller
public class LookBookController {

    @Autowired
    private LookbookService lookbookService;

    @Value("${aws.s3.upload.dir.partners}")
    private String UPLOAD_DIR_PARTNERS;

    @Value("${aws.s3.upload.url.partners}")
    private String PARTNERS_URL;

    @RequestMapping("/lookbook")
    public String lookbook(Model model) {
        log.info("lookbook() 호출");

        List<LookbookModel> lookbooks = lookbookService.selectLookbook();

        model.addAttribute("lookbooks", lookbooks);

        return "/menu/lookbook";
    }

    @RequestMapping("/lookbookWrite")
    public String lookbookWrite(Model model) {
        log.info("lookbookWrite() Get 호출");


        return "/menu/lookbookWrite";
    }

    @RequestMapping(value = "/lookbookWrite", method = RequestMethod.POST)
    public String lookbookWrite(MultipartHttpServletRequest mhr,LookbookModel lookbookModel, HttpServletResponse response) throws FileNotFoundException {
        log.info("lookbookWrite() Post 호출");



        log.info("lookbookModel == " + lookbookModel);


        long result = lookbookService.lookbookInsert(mhr, lookbookModel,  PARTNERS_URL, UPLOAD_DIR_PARTNERS);
        if (result == 0L)
            try {
                response.resetBuffer();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script> alert(' history.back(); </script>");
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return "redirect:/lookbook";
    }

    @RequestMapping("/lookbookDetail")
    public String lookbookDetail(Model model, int num) {
        log.info("lookbookDetail() 호출");


//
        LookbookModel lookbookModel = lookbookService.selectLookbookDetail(num);
        log.info("num == " + num);
        log.info("lookbookModel == " + lookbookModel);

        model.addAttribute("lookbookModel", lookbookModel);

        return "/menu/lookbookDetail";
    }
}

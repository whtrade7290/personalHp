package com.example.exam.controller;

import com.example.exam.Model.InfoModel;


import com.example.exam.service.InfoService;
import lombok.extern.java.Log;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;

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

    @RequestMapping("/test")
    public String test(Model model, InfoModel infoModel, HttpServletResponse response, HttpServletRequest request) {

        XSSFWorkbook xssfWb = null;
        XSSFSheet xssfSheet = null;
        XSSFRow xssfRow = null;
        XSSFCell xssfCell = null;

        try {
            int rowNo = 0; // 행의 갯수

            xssfWb = new XSSFWorkbook(); //XSSFWorkbook 객체 생성
            xssfSheet = xssfWb.createSheet("워크 시트1"); // 워크시트 이름 설정

            // 타이틀 생성
            xssfRow = xssfSheet.createRow(rowNo++); // 행 객체 추가
            xssfCell = xssfRow.createCell((short) 0); // 추가한 행에 셀 객체 추가

            xssfCell.setCellValue("타이틀 입니다"); // 데이터 입력

            xssfSheet.createRow(rowNo++);
            xssfRow = xssfSheet.createRow(rowNo++);  // 빈행 추가


            xssfRow = xssfSheet.createRow(rowNo++);
            xssfCell = xssfRow.createCell(0);
            xssfCell.setCellValue("셀1");
            xssfCell = xssfRow.createCell( 1);
            xssfCell.setCellValue("셀2");
            xssfCell = xssfRow.createCell( 2);
            xssfCell.setCellValue("셀3");
            xssfCell = xssfRow.createCell( 3);
            xssfCell.setCellValue("셀4");
            xssfCell = xssfRow.createCell( 4);

            String localFile = "C:\\Users\\WHtra\\Downloads\\" + "excelDownTest" + ".csv";

//            File file = new File(localFile);
//            FileOutputStream fos = null;
//            fos = new FileOutputStream(file);
//            xssfWb.write(fos);
//
//            if (fos != null) fos.close();

            String title = "DownloadedExcel.xlsx";
            title = URLEncoder.encode(title, "UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel;");
            response.setHeader("Pragma", "public");
            response.setHeader("Expires", "0");
            response.setHeader("Content-Disposition", "attachment; filename = " + title);
            try {
                xssfWb.write(response.getOutputStream());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                xssfWb.close();

            }
        }catch(Exception e){

        }

        return "redirect:/";
    }
}

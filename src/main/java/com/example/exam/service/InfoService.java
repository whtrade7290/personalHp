package com.example.exam.service;

import com.example.exam.Mapper.InfoMapper;
import com.example.exam.Model.InfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoService {

    @Autowired
    private InfoMapper infoMapper;

    public InfoModel getInfo() {
        return infoMapper.getInfo();
    }
}

package com.example.exam.service;

import com.example.exam.Mapper.LookbookMapper;
import com.example.exam.Model.LookbookModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LookbookService {

    @Autowired
    private LookbookMapper lookbookMapper;
    public List<LookbookModel> selectLookbook() {
        return lookbookMapper.selectLookbook();
    }
}

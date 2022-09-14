package com.example.exam.Mapper;

import com.example.exam.Model.LookbookModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LookbookMapper {

    List<LookbookModel> selectLookbook();

    long lookbookInsert(LookbookModel lookbookModel);

    LookbookModel selectLookbookDetail(int num);
}

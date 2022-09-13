package com.example.exam.Mapper;

import com.example.exam.Model.InfoModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InfoMapper {
    InfoModel getInfo();
}

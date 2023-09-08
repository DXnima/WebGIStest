package com.example.webgistest.dao;

import com.example.webgistest.pojo.University;

import java.util.List;

public interface UniversityMapper {

    University selectByPrimaryKey(Integer gid);

    List<University> getUniversity(String name, String level, String province, String type);
}

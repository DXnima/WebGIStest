package com.example.webgistest.service;

import com.example.webgistest.common.ServerResponse;
import com.example.webgistest.pojo.University;

import java.util.List;

public interface IUniversityService {

    ServerResponse<List<University>> getUniversity(String name, String level, String province, String type);
}

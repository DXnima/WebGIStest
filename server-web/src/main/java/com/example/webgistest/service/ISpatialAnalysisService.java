package com.example.webgistest.service;

import com.example.webgistest.common.ServerResponse;

import java.util.Map;

public interface ISpatialAnalysisService {
    // 空间关系
    public ServerResponse<Map<String, Object>> SpaceRelation(String geom1, String geom2);

    // 叠加分析
    public ServerResponse<String> InterAnalysis(String geom1, String geom2);

    // 合并分析
    public ServerResponse<String> UnionAnalysis(String geom1, String geom2);

    // 差异分析
    public ServerResponse<String> DiffAnalysis(String geom1, String geom2);

    // Sym差异分析
    public ServerResponse<String> SymDiffAnalysis(String geom1, String geom2);

    // 缓冲区分析
    public ServerResponse<String> BuffAnalysis(String geom, double distance);
}

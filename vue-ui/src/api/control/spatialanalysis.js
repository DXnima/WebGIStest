import axios from "axios";

// 空间关系
export const relationAnalysis = p => {
  return axios.get('spa/relation', {params:p})
};

// 叠加分析
export const interAnalysis = p => {
  return axios.get('spa/inter', {params:p})
};

// 合并分析
export const unionAnalysis = p => {
  return axios.get('spa/union', {params:p})
};

// 差异分析
export const diffAnalysis = p => {
  return axios.get('spa/diff', {params:p})
};

// Sym差异分析
export const symDiffAnalysis = p => {
  return axios.get('spa/symdiff', {params:p})
};

// 缓冲区分析
export const buffAnalysis = p => {
  return axios.get('spa/buff', {params:p})
};

// 获取指定表数据
import axios from "axios";

// 获取矢量切片
export const getMVT = (z,x,y) => {
  return axios.get(`mvt_test/${z}/${x}/${y}.pbf`)
};

// 缓存矢量切片
export const cacheMVT = p => {
  return axios.get('mvt_test/cache', {params:p})
};

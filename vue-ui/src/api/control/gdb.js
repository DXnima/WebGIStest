import axios from "axios";

export const getGdbLayers = () => {
  return axios.get('gdb/layers')
};

export const getCalculate = () => {
  return axios.get('gdb/calculate')
};

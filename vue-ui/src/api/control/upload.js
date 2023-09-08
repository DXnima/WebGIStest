import axios from "axios";

export const uploadFile = (p, c) => {
  return axios.post('upload', p, c)
};

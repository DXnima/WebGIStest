import axios from "axios";

export const getUniversity = p => {
  return axios.get('university/get', {params:p})
};

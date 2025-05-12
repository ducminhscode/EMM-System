import axios from "axios";
import cookie from 'react-cookies'

const BASE_URL = 'http://localhost:8080/EEMAppSpring/api/';

export const endpoints = {
    'login': '/login',
    'profile': '/secure/profile'
}

export const authApis = () => {
    return axios.create({
        baseURL: BASE_URL,
        headers: {
            "Authorization": `Bearer ${cookie.load('token')}`,
            'Content-Type': 'application/json'
        }
    })
}

export default axios.create({
    baseURL: BASE_URL
});
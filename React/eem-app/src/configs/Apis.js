import axios from "axios";
import cookie from 'react-cookies'

const BASE_URL = 'http://localhost:8080/EEMAppSpring/api/';

export const endpoints = {
    'login': '/login',
    'profile': '/secure/profile',
    'updateProfile': '/secure/profile/update',
    'changePassword': '/secure/profile/change-password',
    'users': '/users',
    'problemTechnicianId': '/problem/technician/',
    'devices': '/devices',
    'updateProblemTechnicianId': '/secure/problem/technician/',
    'repairType': '/repair-type',
    'maintenanceTechnicianId': '/maintenance/technician/',
    'updateMaintenanceTechnicianId': '/secure/maintenance/technician/',
    'problem':'/secure/problem',
    'fatal-level':'/fatal-level',
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
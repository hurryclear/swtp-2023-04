// axios.js

import axios from 'axios';

// Create an Axios instance with custom configuration
const instance = axios.create({
    baseURL: 'http:backend:8080', // You can set your API base URL here
    timeout: 10000, // Timeout in milliseconds
    headers: {
        'Content-Type': 'application/json'
    },
});

// Request interceptor
instance.interceptors.request.use(
    (config) => {
        // You can modify the request config here (e.g., add authentication headers)
        return config;
    },
    (error) => {
        // Handle request error
        return Promise.reject(error);
    }
);

// Response interceptor
instance.interceptors.response.use(
    (response) => {
        // You can modify the response here (e.g., handle common errors)
        return response;
    },
    (error) => {
        // Handle response error
        return Promise.reject(error);
    }
);

export default axios;

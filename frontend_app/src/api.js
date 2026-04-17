const API_BASE_URL = 'http://localhost:8080'; // Adjust if backend runs on different port

// Axios instance configuration
const api = axios.create({
    baseURL: API_BASE_URL,
    withCredentials: true, // Crucial for JSESSIONID cookie
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded' // Spring formLogin default
    }
});

// Response interceptor to handle session expiration or unauthorized access
api.interceptors.response.use(
    response => response,
    error => {
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            // Only redirect if we're not already on the login page
            const currentPath = window.location.hash || '#/login';
            if (currentPath !== '#/login') {
                window.location.hash = '#/login';
            }
        }
        return Promise.reject(error);
    }
);

export default api;

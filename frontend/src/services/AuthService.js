// services/AuthService.js
import axios from '@/plugins/axios';

const AuthService = {
    async authenticate(username, password) {
        try {
            const response = await axios.post('/api/auth/login', {
                username: username,
                password: password,
            });

            const {token, role} = response.data;

            return {status: true, token, role};
        } catch (error) {
            console.error('Authentication failed:', error);
            throw error;
        }
    },

    async logout() {
        try {
            await axios.get('/api/auth/logout');
            return {success: true};
        } catch (error) {
            console.error('Logout failed:', error);
            throw error;
        }
    },
};

export default AuthService;
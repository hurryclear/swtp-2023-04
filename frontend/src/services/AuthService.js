import axios from '@/plugins/axios';

/**
 * Service for handling user authentication.
 * @namespace AuthService
 */
const AuthService = {
    /**
     * Authenticates the user.
     * @async
     * @memberof AuthService
     * @param {string} username - The username.
     * @param {string} password - The password.
     * @returns {Promise<Object>} - Object containing authentication status, token, and role.
     * @throws {Error} - If authentication fails.
     */
    async authenticate(username, password) {
        try {
            const response = await axios.post('/api/auth/login', {
                username: username,
                password: password,
            });

            const {token, role} = response.data;
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            return {status: true, token, role};
        } catch (error) {
            console.error('Authentication failed:', error);
            throw error;
        }
    },

    /**
     * Logs out the user.
     * @async
     * @memberof AuthService
     * @returns {Promise<Object>} - Object indicating success or failure of logout.
     * @throws {Error} - If logout fails.
     */
    async logout() {
        try {
            await axios.get('/api/auth/logout');
            delete axios.defaults.headers.common['Authorization'];
            return {success: true};
        } catch (error) {
            console.error('Logout failed:', error);
            throw error;
        }
    },
};

export default AuthService;

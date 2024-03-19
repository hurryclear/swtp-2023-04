import AuthService from '@/services/AuthService';

/**
 * Vuex store module for handling authentication.
 * @module authentication
 */

export default {
    state: {
        /** Indicates whether the user is authenticated or not. */
        isAuthenticated: false,
        /** The role of the authenticated user. Default is 'ROLE_STUDENT'. */
        userRole: 'ROLE_STUDENT',
        /** The authentication token. */
        token: null,
    },
    mutations: {
        /**
         * Sets the authentication status and token.
         * @param {Object} state - The Vuex state object.
         * @param {Object} payload - Payload containing status and token.
         * @param {boolean} payload.status - The authentication status.
         * @param {string|null} payload.token - The authentication token.
         */
        setAuthentication(state, {status, token}) {
            state.isAuthenticated = status;
            state.token = token;
        },

        /**
         * Sets the user role.
         * @param {Object} state - The Vuex state object.
         * @param {string} role - The user role.
         */
        setUserRole(state, role) {
            state.userRole = role;
        },
    },
    actions: {
        /**
         * Authenticates the user.
         * @param {Object} context - The Vuex action context.
         * @param {Object} credentials - User credentials.
         * @param {string} credentials.username - The username.
         * @param {string} credentials.password - The password.
         * @returns {Object} - Object indicating success or failure of authentication.
         */
        async authenticateUser({commit}, {username, password}) {
            try {
                const {status, token, role} = await AuthService.authenticate(username, password);
                commit('setAuthentication', {status, token});
                commit('setUserRole', role);
                localStorage.setItem('token', token);
                return {success: true};
            } catch (error) {
                console.error('Authentication failed:', error);
                return {success: false, error};
            }
        },

        /**
         * Logs out the user.
         * @param {Object} context - The Vuex action context.
         * @returns {Object} - Object indicating success or failure of logout.
         */
        async logout({commit}) {
            try {
                await AuthService.logout();
                commit('setAuthentication', {status: false, token: null});
                commit('setUserRole', 'ROLE_STUDENT');
                localStorage.removeItem('token');
                return {success: true};
            } catch (error) {
                console.error('Logout failed:', error);
                return {success: false, error};
            }
        },
    },
};

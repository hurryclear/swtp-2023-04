// authentication.js
import AuthService from '@/services/AuthService';

export default {
    state: {
        isAuthenticated: false,
        userRole: 'ROLE_STUDENT',
        token: null,
    },
    mutations: {
        setAuthentication(state, {status, token}) {
            state.isAuthenticated = status;
            state.token = token;
        },
        setUserRole(state, role) {
            state.userRole = role;
        },
    },
    actions: {
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

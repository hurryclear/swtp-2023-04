import { createStore } from 'vuex'

export default createStore({
  state: {
    isAuthenticated: false,
    userRole: 'student',
  },
  getters: {
  },
  mutations: {
    setAuthentication(state, status) {
      
      state.isAuthenticated = status;
    },
    setUserRole(state, role) {
      
      state.userRole = role;
    }
  },
  actions: {
    authenticateUser({ commit }, {status, role}) {
      commit('setAuthentication', status);
      commit('setUserRole', role);
    },
    logoutUser({ commit }) {
      commit('setAuthentication', false);
      commit('setUserRole', 'student');
    }
  },
  modules: {
  }
})

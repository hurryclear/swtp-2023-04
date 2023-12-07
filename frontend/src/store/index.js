import { createStore } from 'vuex'

export default createStore({
  state: {
    isAuthenticated: false,
  },
  getters: {
  },
  mutations: {
    setAuthentication(state, status) {
      console.log('Authentication status set to:', status);
      state.isAuthenticated = status;
    }
  },
  actions: {
    authenticateUser({ commit }, status) {
      commit('setAuthentication', status);
    },
    logoutUser({ commit }) {
      commit('setAuthentication', false);
    }
  },
  modules: {
  }
})

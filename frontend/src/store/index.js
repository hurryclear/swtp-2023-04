import { createStore } from 'vuex';

export default createStore({
  state: {
    isAuthenticated: false,
    userRole: 'ROLE_STUDENT',
    token: null, // New state property for the user's token
    forms: [], // State property for forms
  },
  getters: {
    // Getter to retrieve forms based on status
    formsByStatus: (state) => (status) => {
      if (status === 'all') {
        return state.forms;
      }
      return state.forms.filter(form => form.status === status);
    },
  },
  mutations: {
    setAuthentication(state, { status, token }) {
      state.isAuthenticated = status;
      state.token = token;
    },
    setUserRole(state, role) {
      state.userRole = role;
    },
    // Mutation to initialize forms with a status
    initializeForms(state, forms) {
      state.forms = forms.map(form => ({
        ...form,
        status: form.status || 'open' // Assign 'open' status if not present
      }));
    },
    // Mutation to update a form's status
    updateFormStatus(state, { formId, newStatus, comment }) {
      const formIndex = state.forms.findIndex(form => form.timestamp === formId);
      if (formIndex !== -1) {
        state.forms[formIndex].status = newStatus 
        if (comment) {
          state.forms[formIndex].comment = comment;
        }
      }
    },
    // Mutation to add a new form
    addForm(state, form) {
      state.forms.push({ ...form, id: form.timestamp }); // Using timestamp as the ID
    }
  },
  actions: {
    authenticateUser({ commit }, { status, role, token }) {
      commit('setAuthentication', { status, token });
      commit('setUserRole', role);
    },
    // Action to load and initialize forms
    loadForms({ commit }, forms) {
      commit('initializeForms', forms);
    },
    // Action to change form status
    changeFormStatus({ commit }, { formId, newStatus, comment }) {
      commit('updateFormStatus', { formId, newStatus, comment });
    },
    // Action to submit a new form
    submitForm({ commit }, form) {
      commit('addForm', form);
    },
    async logout({ commit }) {
      try {
        // Clear the stored token in local storage
        localStorage.removeItem('token');

        // Logout the user in the store
        commit('setAuthentication', { status: false, token: null });
        commit('setUserRole', 'ROLE_STUDENT');

        // Redirect to the login page or home
        this.$router.push('/login'); // Change to the appropriate route
      } catch (error) {
        console.error('Logout failed:', error);
      }
    },
  },
  modules: {
  }
});

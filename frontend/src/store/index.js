import { createStore } from 'vuex';

export default createStore({
  state: {
    isAuthenticated: false,
    userRole: 'student',
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
    setAuthentication(state, status) {
      state.isAuthenticated = status;
    },
    setUserRole(state, role) {
      state.userRole = role;
    },
    // Mutation to initialize forms with a status
    initializeForms(state, forms) {
      state.forms = forms.map(form => ({
        ...form,
        status: form.status || 'Offen' // Assign 'Offen' status if not present
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
    authenticateUser({ commit }, {status, role}) {
      commit('setAuthentication', status);
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
    }
  },
  modules: {
  }
});

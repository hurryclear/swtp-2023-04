// form.js

export default {
    state: {
        forms: [],
    },
    getters: {
        formsByStatus: (state) => (status) => {
            if (status === 'all') {
                return state.forms;
            }
            return state.forms.filter(form => form.status === status);
        },
    },
    mutations: {
        initializeForms(state, forms) {
            state.forms = forms.map(form => ({
                ...form,
                status: form.status || 'open'
            }));
        },
        updateFormStatus(state, {formId, newStatus, comment}) {
            const formIndex = state.forms.findIndex(form => form.timestamp === formId);
            if (formIndex !== -1) {
                state.forms[formIndex].status = newStatus;
                if (comment) {
                    state.forms[formIndex].comment = comment;
                }
            }
        },
        addForm(state, form) {
            state.forms.push({...form, id: form.timestamp});
        },
    },
    actions: {
        loadForms({commit}, forms) {
            commit('initializeForms', forms);
        },
        changeFormStatus({commit}, {formId, newStatus, comment}) {
            commit('updateFormStatus', {formId, newStatus, comment});
        },
        submitForm({commit}, form) {
            commit('addForm', form);
        },
    },
};

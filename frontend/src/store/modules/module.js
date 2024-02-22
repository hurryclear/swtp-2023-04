// module.js
import ModuleService from "@/services/ModuleService";

export default {
    state: {
        modules: [],
    },
    mutations: {
        setModules(state, modules) {
            state.modules = modules;
        },
    },
    actions: {
        async fetchModules({commit}) {
            try {
                const modules = await ModuleService.fetchModules();
                commit('setModules', modules);
                console.log('Modules fetched and stored in the store.');
            } catch (error) {
                console.error('Error fetching Modules:', error);
            }
        },
    },
};

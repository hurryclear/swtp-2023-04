// module.js
import ModuleService from "@/services/ModuleService";

export default {
    state: {
        studyPlans: [],
        modules: [],
    },
    mutations: {
        setModules(state, modules) {
            state.modules = modules;
        },
        setStudyPlans(state, studyPlans) {
            state.studyPlans = studyPlans;
        },
    },
    actions: {
        async fetchModules({commit},studyPlan) {
            try {
                console.log("Fetching Modules for course of Study", studyPlan)
                const modules = await ModuleService.fetchModules(studyPlan);
                commit('setModules', modules);
                console.log('Modules fetched and stored in the store.');
            } catch (error) {
                console.error('Error fetching Modules:', error);
            }
        },
        async fetchStudyPlans({commit}) {
            try {
                const studyPlans = await ModuleService.fetchStudyPlans();
                commit('setStudyPlans', studyPlans);
                console.log('Study Plans fetched and stored in the store.');
            } catch (error) {
                console.error('Error fetching Modules:', error);
            }
        },
    },
};

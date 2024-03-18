import ModuleService from "@/services/ModuleService";

/**
 * Vuex module for handling module data.
 * @namespace module
 */
export default {
    state: {
        /** Array of study plans. */
        studyPlans: [],
        /** Array of modules. */
        modules: [],
    },
    mutations: {
        /**
         * Sets modules data in the state.
         * @memberof module
         * @param {object} state - The Vuex state object.
         * @param {Array} modules - Array of modules data.
         */
        setModules(state, modules) {
            state.modules = modules;
        },
        /**
         * Sets study plans data in the state.
         * @memberof module
         * @param {object} state - The Vuex state object.
         * @param {Array} studyPlans - Array of study plans data.
         */
        setStudyPlans(state, studyPlans) {
            state.studyPlans = studyPlans;
        },
    },
    actions: {
        /**
         * Fetches modules data for a given study plan and commits it to the state.
         * @memberof module
         * @param {object} context - The Vuex action context object.
         * @param {string} studyPlan - The name of the study plan.
         */
        async fetchModules({commit}, studyPlan) {
            try {
                console.log("Fetching Modules for course of Study", studyPlan);
                const modules = await ModuleService.fetchModules(studyPlan);
                commit('setModules', modules);
                console.log('Modules fetched and stored in the store.');
            } catch (error) {
                console.error('Error fetching Modules:', error);
                throw error; // Propagate the error to the caller
            }
        },
        /**
         * Fetches study plans data and commits it to the state.
         * @memberof module
         * @param {object} context - The Vuex action context object.
         */
        async fetchStudyPlans({commit}) {
            try {
                const studyPlans = await ModuleService.fetchStudyPlans();
                commit('setStudyPlans', studyPlans);
                console.log('Study Plans fetched and stored in the store.');
            } catch (error) {
                console.error('Error fetching Study Plans:', error);
                throw error; // Propagate the error to the caller
            }
        },
    },
};

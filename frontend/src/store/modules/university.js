import UniversityService from '@/services/UniversityService';

/**
 * Vuex module for handling universities data.
 * @namespace university
 */
export default {
    state: {
        /** Array of universities. */
        universities: [],
    },
    mutations: {
        /**
         * Sets universities data in the state.
         * @memberof university
         * @param {object} state - The Vuex state object.
         * @param {Array} universities - Array of universities data.
         */
        setUniversities(state, universities) {
            state.universities = universities;
        },
    },
    actions: {
        /**
         * Fetches universities data and commits it to the state.
         * @memberof university
         * @param {object} context - The Vuex action context object.
         */
        async fetchUniversities({commit}) {
            try {
                console.log("Fetching Universities.");
                const universities = await UniversityService.fetchUniversities();
                commit('setUniversities', universities);
                console.log('Universities fetched and stored in the store.');
            } catch (error) {
                console.error('Error fetching universities:', error);
                throw error; // Propagate the error to the caller
            }
        },
    },
};

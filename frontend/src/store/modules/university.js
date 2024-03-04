// university.js
import UniversityService from '@/services/UniversityService';

export default {
    state: {
        universities: [],
    },
    mutations: {
        setUniversities(state, universities) {
            state.universities = universities;
        },
    },
    actions: {
        async fetchUniversities({commit}) {
            try {
                const universities = await UniversityService.fetchUniversities();
                commit('setUniversities', universities);
                console.log('Universities fetched and stored in the store.');
            } catch (error) {
                console.error('Error fetching universities:', error);
            }
        },
    },
};

import axios from '@/plugins/axios';

/**
 * Service for fetching study plans and modules.
 * @namespace ModuleService
 */
export default {
    /**
     * Fetches study plans.
     * @async
     * @memberof ModuleService
     * @returns {Promise<Array>} - Array of study plans.
     * @throws {Error} - If fetching study plans fails.
     */
    async fetchStudyPlans() {
        try {
            const response = await axios.get("/api/unidata/getMajors");
            return response.data.courses;
        } catch (error) {
            console.error(error);
            throw error;
        }
    },

    /**
     * Fetches modules based on a study plan.
     * @async
     * @memberof ModuleService
     * @param {string} studyPlan - The study plan for which to fetch modules.
     * @returns {Promise<Array>} - Array of modules.
     * @throws {Error} - If fetching modules fails.
     */
    async fetchModules(studyPlan) {
        try {
            const response = await axios.get(`/api/unidata/getModules?majorName=${studyPlan}`);
            return response.data.modules;
        } catch (error) {
            console.error(error);
            throw error;
        }
    },
};

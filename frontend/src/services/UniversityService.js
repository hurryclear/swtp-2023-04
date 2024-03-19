import axios from 'axios';

const BASE_URL = 'http://universities.hipolabs.com';

/**
 * Service for fetching universities data.
 * @namespace UniversityService
 */
export default {
    /**
     * Fetches universities data.
     * @async
     * @memberof UniversityService
     * @returns {Promise<Array>} - Array of universities data.
     * @throws {Error} - If fetching universities data fails.
     */
    async fetchUniversities() {
        try {
            const response = await axios.get(`${BASE_URL}/search`, {timeout: 10000});
            return response.data;
        } catch (error) {
            console.error('Error fetching universities:', error);
            throw error;
        }
    },
};

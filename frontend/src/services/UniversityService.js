// UniversityService.js
import axios from 'axios';

const BASE_URL = 'http://universities.hipolabs.com';

export default {
    async fetchUniversities() {
        try {
            const response = await axios.get(`${BASE_URL}/search`, {timeout: 10000});
            return response.data;
        } catch (error) {
            console.error('Error fetching universities:', error);
        }
    },
};

//FromService.js
import axios from '@/plugins/axios';

export default {
    async fetchApplicationSummary(formId) {
        try {
            const response = await axios.get('/api/student/getApplicationSummary', {params: {formId}});
            return response.data.form;
        } catch (error) {
            console.error('Error fetching Application Suummary:', error);
            throw error;
        }
    },
    async fetchPdfSummary(formId) {
        try {
            const response = await axios.get('/api/student/getPdfSummary', {params: {formId}, responseType: 'blob'});
            return response.data;
        } catch (error) {
            console.error('Error fetching PDF Summary:', error);
            throw error;
        }
    },
};
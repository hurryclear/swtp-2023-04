//FromService.js
import axios from '@/plugins/axios';

export default {
  async fetchApplicationSummary(applicationId) {
    try {
      const response = await axios.get(`/api/student/reviewApplication`, {params: {applicationID: applicationId}});
      return response.data;
    } catch (error) {
      console.error('Error fetching application summary:', error);
      throw error;
    }
  },
  async fetchPdfSummary(applicationId) {
    try {
      const response = await axios.get(`/api/student/getPdfSummary`, {params: {applicationId}}, { responseType: 'blob' });
      return response.data;
      
    } catch (error) {
      console.error('Error fetching PDF summary:', error);
      throw error;
    }
  },
  async fetchApplications() {
    try {
      const response = await axios.get('/api/application/get-all-applications');
      console.log('Applications:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error fetching applications:', error);
      throw error;
    }
  }
};

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
};

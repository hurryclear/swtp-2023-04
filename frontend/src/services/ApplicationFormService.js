// FormService.js
import axios from '@/plugins/axios';

class ApplicationFormService {
    static async submitForm(formData) {
        try {
            const response = await axios.post('/api/student/submitApplication', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            return { success: true, data: response.data };
        } catch (error) {
            console.error('Form submission failed:', error);
            return { success: false, error };
        }
    }
}

export default ApplicationFormService;
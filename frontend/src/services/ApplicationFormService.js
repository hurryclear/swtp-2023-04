import axios from '@/plugins/axios';

/**
 * Service class for submitting application forms.
 * @class
 */
class ApplicationFormService {
    /**
     * Submits the application form data.
     * @static
     * @async
     * @param {FormData} formData - The form data to submit.
     * @returns {Object} - Object indicating success or failure of form submission.
     */
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
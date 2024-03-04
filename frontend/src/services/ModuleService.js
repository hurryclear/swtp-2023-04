// ModuleService.js
import axios from '@/plugins/axios';

export default {
    async fetchStudyPlans() {
        try {
            const response = axios.get("/api/unidata/getMajors");
            return response.data.courses;
        } catch (error) {
            console.error(error)
        }
    },
    async fetchModules(studyPlan) {
        try {
            const response = axios.get(`/api/unidata/getModules?majorName=${studyPlan}`);
            return response.data.modules;
        } catch (error) {
            console.error(error)
        }
    },
};

// UniversityService.js
import axios from '@/plugins/axios';
import moduleJSON from "@/assets/module_liste.json";

export default {
    async fetchModules() {
        try {
            const response = axios.get("/i/dont/know/where/the/endpoint/is") //TODO: REPLACE WITH ACTUAL ENDPOINT
            return response.data.courses[0].modules
        } catch (error) {
            console.error(error)
            return moduleJSON.courses[0].modules;
        }
    },
};

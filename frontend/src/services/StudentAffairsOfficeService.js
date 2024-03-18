// StudentAffairsOfficeService.js
import axios from "@/plugins/axios";

const StudentAffairsOfficeService = {
    async getModules(courseOfStudy) {
        try {
            const response = await axios.get(`/api/unidata/getModules?majorName=${courseOfStudy}`);
            return response.data.modules;
        } catch (error) {
            throw new Error(`Error fetching modules: ${error.message}`);
        }
    },
    async getAllModules(courseOfStudy) {
        try {
            const response = await axios.get(`/api/unidata/getAllModules?majorName=${courseOfStudy}`);
            return response.data.modules;
        } catch (error) {
            throw new Error(`Error fetching modules: ${error.message}`);
        }
    },

    async sendFormToApproval(form) {
        try {
            const response = await axios.put("/api/application/readyForApproval", form);
            return response.data; // Adjust this as per your API response structure
        } catch (error) {
            throw new Error(`Error sending form to approval: ${error.message}`);
        }
    },

    async saveEditedForm(form) {
        try {
            const response = await axios.put("/api/application/saveEdited", form);
            return response.data; // Adjust this as per your API response structure
        } catch (error) {
            throw new Error(`Error saving edited form: ${error.message}`);
        }
    },

    async formallyRejectForm(form) {
        try {
            const response = await axios.put("/api/application/formalRejection", form);
            return response.data; // Adjust this as per your API response structure
        } catch (error) {
            throw new Error(`Error formally rejecting form: ${error.message}`);
        }
    },

    async getModulePDF(filePath) {
        try {
            const response = await axios.get("/api/application/getModulePDF", {
                params: { filePath },
                responseType: 'blob'
            });
            return response.data;
        } catch (error) {
            throw new Error(`Error fetching module PDF: ${error.message}`);
        }
    },

    async getOfficeOverview(queryString) {
        try {
            const response = await axios.get("/api/application/overviewOffice?" + queryString);
            return response.data.content;
        } catch (error) {
            throw new Error(`Error fetching office overview: ${error.message}`);
        }
    },

    async getApplication(applicationID) {
        try {
            const response = await axios.get(`/api/application/getApplication?applicationID=${applicationID}`);
            return response.data;
        } catch (error) {
            throw new Error(`Error fetching application: ${error.message}`);
        }
    },

    async searchApplication(queryString) {
        try {
            const response = await axios.get("/api/application/searchApplication?" + queryString);
            return response.data;
        } catch (error) {
            throw new Error(`Error retrieving filtered/sorted applications: ${error.message}`);
        }
    },


    //committee

    async getCommitteeOverview(queryString) {
        try {
            const response = await axios.get("/api/application/overviewCommittee?" + queryString);
            return response.data.content;
        } catch (error) {
            throw new Error(`Error fetching committee overview: ${error.message}`);
        }
    },

    async saveApprovedForm(form) {
        try {
            const response = await axios.put("/api/application/saveApproval", form);
            return response.data; // Adjust this as per your API response structure
        } catch (error) {
            throw new Error(`Error saving edited form: ${error.message}`);
        }
    },
};

export default StudentAffairsOfficeService;

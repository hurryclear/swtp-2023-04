/**
 * A service module for managing student affairs office operations.
 * @module StudentAffairsOfficeService
 */

import axios from "@/plugins/axios";

const StudentAffairsOfficeService = {
    /**
     * Fetches modules for a given course of study.
     * @param {string} courseOfStudy - The name of the course of study.
     * @returns {Promise<Array>} A promise that resolves with an array of modules.
     * @throws {Error} If an error occurs while fetching modules.
     */
    async getModules(courseOfStudy) {
        try {
            const response = await axios.get(`/api/unidata/getModules?majorName=${courseOfStudy}`);
            return response.data.modules;
        } catch (error) {
            throw new Error(`Error fetching modules: ${error.message}`);
        }
    },

    /**
     * Fetches all modules for a given course of study.
     * @param {string} courseOfStudy - The name of the course of study.
     * @returns {Promise<Array>} A promise that resolves with an array of modules.
     * @throws {Error} If an error occurs while fetching modules.
     */
    async getAllModules(courseOfStudy) {
        try {
            const response = await axios.get(`/api/unidata/getAllModules?majorName=${courseOfStudy}`);
            return response.data.modules;
        } catch (error) {
            throw new Error(`Error fetching modules: ${error.message}`);
        }
    },

    /**
     * Sends a form to approval.
     * @param {object} form - The form data to be sent for approval.
     * @returns {Promise<object>} A promise that resolves with the server response.
     * @throws {Error} If an error occurs while sending the form for approval.
     */
    async sendFormToApproval(form) {
        try {
            const response = await axios.put("/api/application/readyForApproval", form);
            return response.data;
        } catch (error) {
            throw new Error(`Error sending form to approval: ${error.message}`);
        }
    },

    /**
     * Saves an edited form.
     * @param {object} form - The edited form data to be saved.
     * @returns {Promise<object>} A promise that resolves with the server response.
     * @throws {Error} If an error occurs while saving the edited form.
     */
    async saveEditedForm(form) {
        try {
            const response = await axios.put("/api/application/saveEdited", form);
            return response.data;
        } catch (error) {
            throw new Error(`Error saving edited form: ${error.message}`);
        }
    },

    /**
     * Formally rejects a form.
     * @param {object} form - The form data to be formally rejected.
     * @returns {Promise<object>} A promise that resolves with the server response.
     * @throws {Error} If an error occurs while formally rejecting the form.
     */
    async formallyRejectForm(form) {
        try {
            const response = await axios.put("/api/application/formalRejection", form);
            return response.data;
        } catch (error) {
            throw new Error(`Error formally rejecting form: ${error.message}`);
        }
    },

    /**
     * Fetches a module PDF file.
     * @param {string} filePath - The file path of the module PDF.
     * @returns {Promise<Blob>} A promise that resolves with the binary data of the PDF.
     * @throws {Error} If an error occurs while fetching the module PDF.
     */
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

    /**
     * Fetches the office overview.
     * @returns {Promise<string>} A promise that resolves with the office overview content.
     * @throws {Error} If an error occurs while fetching the office overview.
     */
    async getOfficeOverview() {
        try {
            const response = await axios.get("/api/application/overviewOffice");
            return response.data.content;
        } catch (error) {
            throw new Error(`Error fetching office overview: ${error.message}`);
        }
    },

    /**
     * Fetches an application by ID.
     * @param {string} applicationID - The ID of the application to fetch.
     * @returns {Promise<object>} A promise that resolves with the application data.
     * @throws {Error} If an error occurs while fetching the application.
     */
    async getApplication(applicationID) {
        try {
            const response = await axios.get(`/api/application/getApplication?applicationID=${applicationID}`);
            return response.data;
        } catch (error) {
            throw new Error(`Error fetching application: ${error.message}`);
        }
    },

    /**
     * Searches for applications based on a query string.
     * @param {string} queryString - The query string for filtering/sorting applications.
     * @returns {Promise<object>} A promise that resolves with the search results.
     * @throws {Error} If an error occurs while searching for applications.
     */
    async searchApplication(queryString) {
        try {
            const response = await axios.get("/api/application/searchApplication?" + queryString);
            return response.data;
        } catch (error) {
            throw new Error(`Error retrieving filtered/sorted applications: ${error.message}`);
        }
    },

    /**
     * Fetches the committee overview.
     * @returns {Promise<string>} A promise that resolves with the committee overview content.
     * @throws {Error} If an error occurs while fetching the committee overview.
     */
    async getCommitteeOverview() {
        try {
            const response = await axios.get("/api/application/overviewCommittee");
            return response.data.content;
        } catch (error) {
            throw new Error(`Error fetching committee overview: ${error.message}`);
        }
    },

    /**
     * Saves an approved form.
     * @param {object} form - The approved form data to be saved.
     * @returns {Promise<object>} A promise that resolves with the server response.
     * @throws {Error} If an error occurs while saving the approved form.
     */
    async saveApprovedForm(form) {
        try {
            const response = await axios.put("/api/application/saveApproval", form);
            return response.data;
        } catch (error) {
            throw new Error(`Error saving edited form: ${error.message}`);
        }
    },
};

export default StudentAffairsOfficeService;

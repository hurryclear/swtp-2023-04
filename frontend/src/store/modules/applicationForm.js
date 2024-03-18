import ApplicationFormService from "@/services/ApplicationFormService";
import router from '@/router';

/**
 * Resets the form data to its initial state.
 * @param {Object} state - The Vuex state object.
 */
function resetForm(state) {
    state.form = {
        meta: {
            status: "",
            dateOfSubmission: "",
            dateLastEdited: "",
        },
        university: {
            name: null,
            country: "",
            website: "",
        },
        courseOfStudy: {
            old: "",
            new: null,
        },
        moduleMappings: [
            {
                meta: {
                    key: 0,
                },
                previousModules: [
                    {
                        meta: {
                            approval: "",
                            comments: {
                                student: "",
                            },
                            key: 0,
                        },
                        name: "",
                        description: {file: null},
                        credits: 0,
                        university: {
                            name: null,
                            country: "",
                            website: "",
                        },
                        id: "",
                        courseOfStudy: "",
                    },
                ],
                modulesToBeCredited: [],
            },
        ],
    }
}

/**
 * Vuex module for managing the application form state.
 * @module applicationForm
 */

export default {
    state: {
        /**
         * The ID of the application form.
         * @type {string|null}
         */
        applicationId: null,

        /**
         * The application form data.
         * @type {Object}
         */
        form: {
            meta: {
                /**
                 * The status of the application.
                 * @type {string}
                 */
                status: "",

                /**
                 * The date of submission of the application.
                 * @type {string}
                 */
                dateOfSubmission: "",

                /**
                 * The date when the form was last edited.
                 * @type {string}
                 */
                dateLastEdited: "",
            },
            university: {
                /**
                 * The name of the university.
                 * @type {string|null}
                 */
                name: null,

                /**
                 * The country where the university is located.
                 * @type {string}
                 */
                country: "",

                /**
                 * The website of the university.
                 * @type {string}
                 */
                website: "",
            },
            courseOfStudy: {
                /**
                 * The old course of study.
                 * @type {string}
                 */
                old: "",

                /**
                 * The new course of study.
                 * @type {string|null}
                 */
                new: null,
            },
            moduleMappings: [
                {
                    meta: {
                        /**
                         * The key of the module mapping.
                         * @type {number}
                         */
                        key: 0,
                    },
                    previousModules: [
                        {
                            meta: {
                                /**
                                 * The approval status of the module.
                                 * @type {string}
                                 */
                                approval: "",

                                /**
                                 * Comments provided by the student.
                                 * @type {Object}
                                 */
                                comments: {
                                    student: "",
                                },

                                /**
                                 * The key of the module.
                                 * @type {number}
                                 */
                                key: 0,
                            },
                            /**
                             * The name of the module.
                             * @type {string}
                             */
                            name: "",

                            /**
                             * Description of the module.
                             * @type {Object}
                             */
                            description: {file: null},

                            /**
                             * The credits assigned to the module.
                             * @type {number}
                             */
                            credits: 0,

                            /**
                             * The university offering the module.
                             * @type {Object}
                             */
                            university: {
                                /**
                                 * The name of the university offering the module.
                                 * @type {string|null}
                                 */
                                name: null,

                                /**
                                 * The country where the university offering the module is located.
                                 * @type {string}
                                 */
                                country: "",

                                /**
                                 * The website of the university offering the module.
                                 * @type {string}
                                 */
                                website: "",
                            },

                            /**
                             * The ID of the module.
                             * @type {string}
                             */
                            id: "",

                            /**
                             * The course of study related to the module.
                             * @type {string}
                             */
                            courseOfStudy: "",
                        },
                    ],

                    /**
                     * Modules to be credited.
                     * @type {Array}
                     */
                    modulesToBeCredited: [],
                },
            ],
        }
    },
    mutations: {
        /**
         * Sets the application ID in the Vuex state.
         * @param {Object} state - The Vuex state object.
         * @param {string} applicationId - The application ID to set.
         */
        setApplicationId(state, applicationId) {
            state.applicationId = applicationId;
        },

        /**
         * Updates the data of a module mapping in the form.
         * @param {Object} state - The Vuex state object.
         * @param {Object} payload - The payload containing the module mapping index and the new data.
         * @param {number} payload.moduleMappingIndex - The index of the module mapping to update.
         * @param {Object} payload.newData - The new data to update the module mapping with.
         */
        updateModuleMappingData(state, { moduleMappingIndex, newData }) {
            state.form.moduleMappings[moduleMappingIndex] = newData;
        },
        /**
         * Updates the university data in the form.
         * @param {Object} state - The Vuex state object.
         * @param {Object} newData - The new university data.
         */
        updateUniversityData(state, newData) {
            state.form.university = newData;
        },

        /**
         * Updates the course of study data in the form.
         * @param {Object} state - The Vuex state object.
         * @param {Object} newData - The new course of study data.
         */
        updateCourseOfStudyData(state, newData) {
            state.form.courseOfStudy = newData;
        },

        /**
         * Adds a new module mapping form to the form state.
         * @param {Object} state - The Vuex state object.
         * @param {number} key - The key for the new module mapping.
         */
        addModuleMappingForm(state, key) {
            state.form.moduleMappings.push(
                {
                    meta: {
                        key: key,
                        approval: "",
                        comments: {
                            student: ""
                        },
                    },
                    previousModules: [
                        {
                            meta: {
                                approval: "",
                                comments: {
                                    student: ""
                                },
                                key: 0,
                            },
                            university: state.form.university,
                            selectedUniversity: null,
                            id: "",
                            courseOfStudy: state.form.courseOfStudy.old,
                            name: "",
                            description: { file: null },
                            credits: 0,
                        },
                    ],
                    modulesToBeCredited: [
                        {
                            id: null
                        }
                    ],
                }
            )
        },

        /**
         * Removes a module mapping form from the form state.
         * @param {Object} state - The Vuex state object.
         * @param {number} index - The index of the module mapping form to remove.
         */
        removeModuleMappingForm(state, index) {
            if (state.form.moduleMappings.length > 1) {
                state.form.moduleMappings.splice(index, 1);
            }
        },

        /**
         * Adds a new module to a module mapping in the form state.
         * @param {Object} state - The Vuex state object.
         * @param {Object} payload - The payload containing the module mapping index and the key for the new module.
         * @param {number} payload.moduleMappingIndex - The index of the module mapping to add the new module to.
         * @param {number} payload.key - The key for the new module.
         */
        addModule(state, { moduleMappingIndex, key }) {
            state.form.moduleMappings[moduleMappingIndex].previousModules.push(
                {
                    meta: {
                        approval: "",
                        comments: {
                            student: "",
                        },
                        key: key,
                    },
                    university: state.form.university,
                    selectedUniversity: null,
                    id: "",
                    courseOfStudy: state.form.courseOfStudy.old,
                    name: "",
                    description: { file: null },
                    credits: 0,
                },
            );
        },

        /**
         * Removes a module from a module mapping in the form state.
         * @param {Object} state - The Vuex state object.
         * @param {Object} payload - The payload containing the module mapping index and the index of the module to remove.
         * @param {number} payload.moduleMappingIndex - The index of the module mapping.
         * @param {number} payload.moduleIndex - The index of the module to remove.
         */
        removeModule(state, { moduleMappingIndex, moduleIndex }) {
            state.form.moduleMappings[moduleMappingIndex].previousModules.splice(moduleIndex, 1);
        }

    },
    actions: {
        /**
         * Submits the entire form to the backend.
         * @param {Object} context - The Vuex action context.
         * @param {Object} payload - The payload containing the form data.
         * @param {Object} payload.state - The current state of the Vuex store.
         * @returns {Object} - The submission result.
         */
        async submitWholeForm({ state, commit }) {
            const formData = new FormData();
            state.form.moduleMappings.forEach(
                (moduleForm) => moduleForm.previousModules.forEach(
                    (module) => {
                        const id = moduleForm.meta.key + ":" + module.meta.key;
                        formData.append(`file-${id}`, module.description.file[0]);
                        delete module.description;
                    }
                )
            );
            const timestamp = new Date().toISOString();
            state.form.meta.dateLastEdited = timestamp;
            state.form.meta.dateOfSubmission = timestamp;
            formData.append('form', new Blob([JSON.stringify(state.form)], { type: 'application/json' }));

            try {
                const { success, data } = await ApplicationFormService.submitForm(formData);

                if (success) {
                    commit('setApplicationId', data.applicationID);

                    // Navigate to another route and pass application ID
                    router.push({ name: 'Review Application', query: { applicationId: data.applicationID } });
                    resetForm(state);
                } else {
                    // Handle error response
                    console.error('Error submitting form:', data);
                }

                return { success };
            } catch (error) {
                console.error('Error submitting form:', error);
                return { success: false, error };
            }
        },
    },
    getters: {
        /**
         * Checks if the entire form is filled.
         * @param {Object} state - The Vuex state object.
         * @param {Object} getters - The Vuex getters object.
         * @returns {boolean} - Indicates whether the form is filled or not.
         */
        formFilled: (state, getters) => (
            getters.moduleMappingsFilled &&
            getters.universityFormIsFilled
        ),

        /**
         * Checks if all module mappings in the form are filled.
         * @param {Object} state - The Vuex state object.
         * @returns {boolean} - Indicates whether all module mappings are filled or not.
         */
        moduleMappingsFilled: state => state.form.moduleMappings.every(
            moduleMapping => {
                return moduleMapping.modulesToBeCredited.length > 0 &&
                    moduleMapping.previousModules.every(
                        (module) =>{
                            return module.description.file !== null &&
                                module.name.trim() !== '' &&
                                module.courseOfStudy.trim() !== '' &&
                                module.id.trim() !== ''
                        }
                    )
            }
        ),

        /**
         * Checks if the university form is filled.
         * @param {Object} state - The Vuex state object.
         * @returns {boolean} - Indicates whether the university form is filled or not.
         */
        universityFormIsFilled: state => (
            state.form.courseOfStudy.old.trim() !== "" &&
            state.form.courseOfStudy.new !== null &&
            state.form.university.name.trim() !== "" &&
            state.form.university.country.trim() !== ""
        ),

        /**
         * Returns all module mappings
         * @param state
         * @returns {(function(): Array)|*|[{meta: {key: number}, previousModules: [{courseOfStudy: string, credits: number, meta: {comments: {student: string}, approval: string, key: number}, university: {country: string, website: string, name: null}, name: string, description: {file: null}, id: string}], modulesToBeCredited: []}]|[{meta: {key: number}, previousModules: [{courseOfStudy: string, credits: number, meta: {comments: Object, approval: string, key: number}, university: Object, name: string, description: Object, id: string}], modulesToBeCredited: Array}]}
         */
        moduleMappings: state => state.form.moduleMappings,

        /**
         * Checks if removal of module mappings is disabled.
         * @param {Object} state - The Vuex state object.
         * @returns {boolean} - Indicates whether removal of module mappings is disabled or not.
         */
        disableModuleMappingRemoval: state => state.form.moduleMappings.length === 1,

        /**
         * Gets a module mapping by its index.
         * @param {Object} state - The Vuex state object.
         * @returns {Function} - A function to retrieve a module mapping by its index.
         */
        getModuleMappingByIndex: (state) => (id) => state.form.moduleMappings[id]

    }
}
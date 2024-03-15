// applicationForm.js
import ApplicationFormService from "@/services/ApplicationFormService";

export default {
    state: {
        form: {
            meta: {
                status: "",
                dateOfSubmission: "",
                dateLastEdited: "",
            },
            university: {
                name: "",
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
                                name: "",
                                country: "",
                                website: "",
                            },
                            id: "",
                            courseOfStudy: "",
                        },
                    ],
                    modulesToBeCredited: [
                    ],
                },
            ],
        }
    },
    mutations: {
        updateUniversityData(state, newData) {
            state.form.university = newData;
        },
        updateCourseOfStudyData(state, newData) {
            state.form.courseOfStudy = newData
        },
        updateModuleMappingData(state, {moduleMappingIndex, newData}) {
            state.form.moduleMappings[moduleMappingIndex] = newData;
        },
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
                            university: "",
                            id: "",
                            courseOfStudy: "",
                            name: "",
                            description: {file: null},
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
        removeModuleMappingForm(state, index) {
            if (state.form.moduleMappings.length > 1) {
                state.form.moduleMappings.splice(index, 1);
            }
        },
        addModule(state, {moduleMappingIndex, key}) {
            state.form.moduleMappings[moduleMappingIndex].previousModules.push(
                {
                    meta: {
                        approval: "",
                        comments: {
                            student: "",
                        },
                        key: key,
                    },
                    university: "",
                    id: "",
                    courseOfStudy: "",
                    name: "",
                    description: {file: null},
                    credits: 0,
                },
            );
        },
        removeModule(state, {moduleMappingIndex, moduleIndex}) {

            state.form.moduleMappings[moduleMappingIndex].previousModules.splice(moduleIndex, 1);
        }
    },
    actions: {
        async submitWholeForm({state}) {
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
                const {success, data} = await ApplicationFormService.submitForm(formData);

                if (success) {
                    // Handle success response (Form-ID)
                    console.log('Whole Form submitted:', data);
                } else {
                    // Handle error response
                    console.error('Error submitting form:', data);
                }

                return {success};
            } catch (error) {
                console.error('Error submitting form:', error);
                return {success: false, error};
            }
        },
    },
    getters: {
        formFilled: (state,getters) => (
            getters.moduleMappingsFilled &&
            getters.universityFormIsFilled
        ),
        moduleMappingsFilled: state => state.form.moduleMappings.every(
            // Check if all module mappings are filled
            moduleMapping => {
                // Check if modules to be credited are filled
                console.log("\nmodules To Be Credited:",moduleMapping.modulesToBeCredited.length > 0)
                return moduleMapping.modulesToBeCredited.length > 0 &&
                    // Check if every previous module is filled
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
        universityFormIsFilled: state => (
                state.form.courseOfStudy.old.trim() !== "" &&
                state.form.courseOfStudy.new !== null &&
                state.form.university.name.trim() !== "" &&
                state.form.university.country.trim() !== ""
            ),
        moduleMappings: state => state.form.moduleMappings,
        disableModuleMappingRemoval: state => state.form.moduleMappings.length === 1,
        getModuleMappingByIndex: (state) => (id) => state.form.moduleMappings[id]
    }
}
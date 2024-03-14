// applicationForm.js
import ApplicationFormService from "@/services/ApplicationFormService";

export default {
    state: {
        form: {
            meta: {
                status: "",                 //Enum
                dateOfSubmission: "",       //String
                dateLastEdited: "",         //String
            },
            university: {
                name: "",                   //String
                country: "",                //String
                website: "",                //String
            },
            courseOfStudy: {
                old: null,
                new: null,
            },
            moduleMappings: [
                {
                    meta: {
                        key: 0,               //Integer
                    },
                    previousModules: [
                        {
                            meta: {
                                approval: "",         //Enum/Boolean
                                comments: {
                                    student: "",        //String
                                },
                            },
                            number: "",       //String (Applicable?)
                            name: "",         //String
                            description: {file: null},
                            credits: 0,       //Integer
                        },
                    ],
                    modulesToBeCredited: [
                        {
                            number: "",       //String
                            name: null,         //String
                        }
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
                            student: "",
                            office: "",
                        },
                    },
                    previousModules: [
                        {
                            meta: {
                                comments: {
                                    student: "",
                                    office: ""
                                }
                            },
                            university: "",
                            key: 0,
                            number: "",
                            name: "",
                            description: {file: null},
                            credits: 0,
                        },
                    ],
                    modulesToBeCredited: [
                        {
                            number: "",
                            name: null,
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
        addModule(state, index, key) {
            state.form.moduleMappings[index].moduleForm.previousModules.push(
                {
                    meta: {
                        comments: {
                            student: "",
                            office: ""
                        }
                    },
                    university: "",
                    key: key,
                    number: "",
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
            formData.append('form', JSON.stringify(state.form));

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
        formFilled: (state, getters) => (
            // Check if all module mappings are filled
            getters.moduleMappingsFilled &&
            // Check if university information is filled
            state.form.university.name.trim() !== "" &&
            state.form.courseOfStudy.old.trim() !== "" &&
            state.form.courseOfStudy.new.trim() !== "" &&
            state.form.university.country.trim() !== ""),
        moduleMappingsFilled: state => state.form.moduleMappings.every(
            moduleMapping => {
                // Check if modules to be credited are filled
                return moduleMapping.modulesToBeCredited !== null &&
                    // Check if every previous module is filled
                    moduleMapping.previousModules.every(
                        (module) =>
                            module.name.trim() !== '' &&
                            module.description.file !== null
                    )
            }
        ),
        moduleMappings: state => state.form.moduleMappings,
        disableModuleMappingRemoval: state => state.form.moduleMappings.length === 1,
        newCourseOfStudyIsSelected: state => state.form.courseOfStudy.new !== null
    }
}
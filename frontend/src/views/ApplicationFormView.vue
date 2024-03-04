<template>
  <UniversityForm
      :universityData="form.university"
      @updateUniversityData="updateUniversityData"
  />
  <br/>
  <ModuleFormList
      :moduleForms="form.moduleMappings"
      @updateModuleData="updateModuleData"
      @fillChange="(data) => this.moduleDataFilled=data"
  />
  <br/>
  <v-btn
      class="userInput"
      @click="submitWholeForm"
      :disabled="!this.formsFilled"
      color="primary"
  >{{ $t("applicationFormView.submit") }}
  </v-btn>

  <div v-if="applicationId">
    <!--TODO:i18n-->
    <v-text-field
        v-model="applicationId"
        label="Application ID"
        readonly
    />
    <v-btn icon="mdi-content-copy" @click="copyApplicationIdToClipboard"></v-btn>
  </div>
</template>

<script>
import UniversityForm from "@/components/UniversityForm.vue";
import ModuleFormList from "@/components/ModuleFormList.vue";
import {defineComponent} from "vue";
import {mapActions} from "vuex";
import axios from "@/plugins/axios";

export default defineComponent({
  components: {ModuleFormList, UniversityForm},
  computed: {
    universityDataFilled() {
      return (
          this.form.university.name.trim() !== "" && //
          this.form.university.courseOfStudy.trim() !== "" &&
          this.form.university.country.trim() !== ""
      );
    },
    formsFilled() {
      return this.moduleDataFilled && this.universityDataFilled;
    },
  },
  data() {
    return {
      form: {
        meta: {
          status: "",                 //Enum
          comments: {
            student: "",              //String
            office: "",               //String
          },
          dateOfSubmission: "",       //String
          dateLastEdited: "",         //String
        },
        university: {
          name: "",                   //String
          country: "",                //String
          website: "",                //String
          courseOfStudy: "",          //String
        },
        moduleMappings: [
          {
            meta: {
              key: 0,               //Integer
              approval: "",         //Enum/Boolean
              comments: {
                student: "",        //String
                office: "",         //String
              },
            },
            previousModules: [
              {
                number: "",       //String (Applicable?)
                name: "",         //String
                description: { file: null },
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
      },
      applicationId: null,
      moduleDataFilled: false,
    };
  },
  methods: {
    updateUniversityData(data) {
      this.form.university = data;
    },
    updateModuleData(data) {
      this.form.moduleMappings = data;
    },
    submitWholeForm() {
      const formData = new FormData();
      this.form.moduleMappings.forEach(
          (moduleForm, i) => moduleForm.previousModules.forEach(
              (module, j) => {
                module.description.id = i + ":" + j
                formData.append(`file-${module.description.id}`, module.description.file)
              }

          )
      );
      const timestamp = new Date().toISOString();
      this.form.meta.dateLastEdited = timestamp
      this.form.meta.dateOfSubmission = timestamp
      // Append form data to FormData object
      formData.append('form', JSON.stringify(this.form));
      // Use axios to send the FormData object to the server
      axios.post('/api/student/submitApplication', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
          .then(response => {
            // Handle success response (Form-ID)
            console.log('Whole Form submitted:', response.data);
          })
          .catch(error => {
            // Handle error response
            console.error('Error submitting form:', error);
          });
    },

    copyApplicationIdToClipboard() {
      if (this.applicationId) {
        navigator.clipboard.writeText(this.applicationId).then(() => {
          console.log("Form ID copied to clipboard!");
        }).catch(err => {
          console.error('Could not copy text: ', err);
        });
      }
    },
    ...mapActions(['submitForm']) // Map Vuex action
  }
});
</script>

<style>
/* Styling for the class 'userInput' */
.userInput {
  margin-bottom: 0.5rem;
  margin-top: 0.5rem;
}
</style>

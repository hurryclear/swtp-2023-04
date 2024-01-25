<template>
  <!-- Displaying UniversityForm and ModuleFormList components -->
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
  >{{ $t("applicationForm.submit") }}
  </v-btn>

  <div v-if="submittedFormId">
    <v-text-field
        v-model="submittedFormId"
        label="Application ID"
        readonly
    />
    <v-btn icon="mdi-content-copy" @click="copyFormIdToClipboard"></v-btn>
    <v-btn icon="mdi-download" @click="createDownloadableJSON(generateFormData())"></v-btn>
  </div>
</template>

<script>
import UniversityForm from "@/components/UniversityForm.vue";
import ModuleFormList from "@/components/ModuleFormList.vue";
import {defineComponent} from "vue";
import {mapActions} from "vuex";

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
      //NEW
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
              approval: "",         //Enum
              comments: {
                student: "",        //String
                office: "",         //String
              },
            },
            previousModules: [
              {
                number: "",       //String (Applicable?)
                name: "",         //String
                description: {
                  id: "",         //String
                  filename: "",   //String
                },
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
      //OLD
      submittedFormId: null,
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
    submitWholeForm() { //TODO
      const timestamp = new Date().toISOString();
      const formData = {
        timestamp: timestamp,
        universityData: this.form.moduleMappings,
        moduleFormsData: this.form.moduleMappings,
        status: 'open',
      };

      // Dispatch the form data to the Vuex store
      this.submitForm(formData);

      this.submittedFormId = timestamp;

      console.log('Whole Form submitted:', JSON.stringify(formData, null, 2));
    },

    createDownloadableJSON(data) {
      const blob = new Blob([JSON.stringify(data, null, 2)], {
        type: "application/json",
      });

      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = `form-${data.timestamp}.json`;

      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },

    copyFormIdToClipboard() {
      if (this.submittedFormId) {
        navigator.clipboard.writeText(this.submittedFormId).then(() => {
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

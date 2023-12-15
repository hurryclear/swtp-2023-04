<template>
  <!-- Displaying UniversityForm and ModuleFormList components -->
  <UniversityForm
    :universityData="universityData"
    @updateUniversityData="updateUniversityData"
  />
  <br/>
  <ModuleFormList
    :moduleForms="moduleForms"
    @updateModuleData="updateModuleData"
    @fillChange="(data) => this.moduleDataFilled=data"
  />
  <br />
  <v-btn
    class="userInput"
    @click="submitWholeForm"
    :disabled="!this.formsFilled"
    color="primary"
  >Submit Whole Form</v-btn>

  <div v-if="submittedFormId">
    <v-text-field
      v-model="submittedFormId"
      label="Application ID"
      readonly
    ></v-text-field>
    <v-btn icon="mdi-content-copy" @click="copyFormIdToClipboard"></v-btn>
    <v-btn icon="mdi-download" @click="createDownloadableJSON(generateFormData())"></v-btn>
  </div>
</template>

<script>
import UniversityForm from "@/components/UniversityForm.vue";
import ModuleFormList from "@/components/ModuleFormList.vue";
import { defineComponent} from "vue";
import { mapActions } from "vuex";

export default defineComponent({
  components: { ModuleFormList, UniversityForm },
  computed: {
    universityDataFilled() {
      return (
          this.universityData.universityName.trim() !== "" &&
          this.universityData.studyProgram.trim() !== "" &&
          this.universityData.country.trim() !== ""
      );
    },
    formsFilled() {
      return this.moduleDataFilled && this.universityDataFilled;
    },
  },
  data() {
    return {
      submittedFormId: null,
      universityData: {
        universityName: "",
        studyProgram: "",
        country: "",
      },
      moduleForms: [
        {
          name: "",
          comment: "",
          description: null,
          module2bCredited: null,
        },
      ],
      moduleDataFilled: false,
    };
  },
  methods: {
    updateUniversityData(data) {
      this.universityData = data;
    },
    updateModuleData(data) {
      this.moduleForms = data;
    },
    submitWholeForm() {
      const timestamp = new Date().toISOString();
      const formData = {
        timestamp: timestamp,
        universityData: this.universityData,
        moduleFormsData: this.moduleForms,
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

    generateFormData() {
      return {
        timestamp: new Date().toISOString(),
        universityData: this.universityData,
        moduleFormsData: this.moduleForms,
        status: 'open',
      };
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

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
      variant="outlined"
  >Submit Whole Form</v-btn>
</template>

<script>
// Importing necessary components
import UniversityForm from "@/components/UniversityForm.vue";
import { defineComponent } from "vue";
import ModuleFormList from "@/components/ModuleFormList.vue";

export default defineComponent({
  components: {ModuleFormList, UniversityForm},
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
      universityData: {
        universityName: '',
        studyProgram: '',
        country: '',
      },
      //TODO: Get File
      moduleForms: [
        {
          name: '',
          comment: '',
          description: null,
          module2bCredited: null}
      ],
      moduleDataFilled: false
    };
  },
  methods: {
    // Update university data when UniversityForm emits an event
    updateUniversityData(data) {
      this.universityData = data;
    },
    // Update module data when ModuleForm emits an event
    updateModuleData(data) {
      this.moduleForms = data
    },

    submitWholeForm() {
      // Combine university data and module forms data for submission
      const formData = {
        universityData: this.universityData,
        moduleFormsData: this.moduleForms,
      };

      // TODO: Submission Handling
      console.log('Whole Form submitted:', JSON.stringify(formData, null, 2));
    },
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

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
import UniversityForm from "@/components/UniversityForm.vue";
import { defineComponent } from "vue";
import ModuleFormList from "@/components/ModuleFormList.vue";

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
      };

      const filePath = "form.json";

      // Create a Blob with the JSON data
      const blob = new Blob([JSON.stringify(formData, null, 2)], {
        type: "application/json",
      });

      // Create a link element to trigger the download
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = filePath;

      // Append the link to the document and trigger the download
      document.body.appendChild(link);
      link.click();

      // Remove the link from the document
      document.body.removeChild(link);
    },
  },
});
</script>


<style>
/* Styling for the class 'userInput' */
.userInput {
  margin-bottom: 0.5rem;
  margin-top: 0.5rem;
}
</style>

<!--ApplicationFormView.vue-->
<template>
  <UniversityForm/>
  <br/>
  <ModuleFormList
      v-if="newCourseOfStudyIsSelected"
  />
  <br/>
  <v-btn
      class="userInput"
      @click="submitWholeForm"
      :disabled="!formFilled"
      color="primary"
  >{{ $t("applicationFormView.submit") }}
  </v-btn>
</template>

<script>
import UniversityForm from "@/components/UniversityForm.vue";
import ModuleFormList from "@/components/ModuleFormList.vue";

export default {
  components: {ModuleFormList, UniversityForm},
  computed: {
    formFilled() {
      return this.$store.getters.formFilled;
    },
    newCourseOfStudyIsSelected() {
      return this.$store.getters.newCourseOfStudyIsSelected;
    }
  },
  methods: {
    async submitWholeForm() {
      try {
        const {success} = await this.$store.dispatch('submitWholeForm');

        if (success) {
          // Handle success response (if needed)
          console.log('Form submitted successfully');
        } else {
          // Handle error response (if needed)
          console.error('Error submitting form');
        }
      } catch (error) {
        console.error('Error submitting form:', error);
      }
    },
  }
};
</script>

<style>
/* Styling for the class 'userInput' */
.userInput {
  margin-bottom: 0.5rem;
  margin-top: 0.5rem;
}
</style>

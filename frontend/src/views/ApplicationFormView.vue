<!--ApplicationFormView.vue-->
<template>
  <UniversityForm/>
  <br/>
  <ModuleFormList
      v-if="universityFormIsFilled"
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
/**
 * Vue component representing the main application form view.
 * This component manages the submission of the entire form.
 * @component ApplicationFormView
 */
export default {
  // Component properties
  components: { ModuleFormList, UniversityForm },

  // Computed properties
  computed: {
    /**
     * Checks if the entire form is filled.
     * @returns {boolean} Whether the form is filled or not.
     */
    formFilled() {
      return this.$store.getters.formFilled;
    },

    /**
     * Checks if the university form is filled.
     * @returns {boolean} Whether the university form is filled or not.
     */
    universityFormIsFilled() {
      return this.$store.getters.universityFormIsFilled;
    }
  },

  // Methods
  methods: {
    /**
     * Submits the entire form.
     * @async
     * @returns {Promise<void>} A promise that resolves when the form is submitted.
     */
    async submitWholeForm() {
      try {
        const { success } = await this.$store.dispatch('submitWholeForm');

        if (success) {
          console.log('Form submitted successfully');
        } else {
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

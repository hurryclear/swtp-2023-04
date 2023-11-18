<script>
// Importing necessary components
import UniversityForm from "@/components/UniversityForm.vue";
import { defineComponent } from "vue";
import ModuleFormList from "@/components/ModuleFormList.vue";

export default defineComponent({
  components: { ModuleFormList, UniversityForm },
  methods: {
    navigateTo(page) {
      if (page === 'home'){
        this.$router.push('/')
      }
    }
  }
});
</script>

<template>
  <!-- Displaying UniversityForm and ModuleFormList components -->
  <UniversityForm/>
  <br/>
  <ModuleFormList/>
  <v-btn @click="navigateTo('home')">Home</v-btn>
</template>

<style>
/* Styling for the class 'userInput' */
.userInput {
  margin-bottom: 0.5rem;
  margin-top: 0.5rem;
}
</style>

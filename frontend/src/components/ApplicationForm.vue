<script>
// Importing necessary components
import UniversityForm from "@/components/UniversityForm.vue";
import { defineComponent } from "vue";
import ModuleFormList from "@/components/ModuleFormList.vue";

export default defineComponent({
  components: { ModuleFormList, UniversityForm }
});
</script>

<template>
  <!-- Displaying UniversityForm and ModuleFormList components -->
  <UniversityForm/>
  <br/>
  <ModuleFormList/>
</template>

<style>
/* Styling for the class 'userInput' */
.userInput {
  margin-bottom: 0.5rem;
  margin-top: 0.5rem;
}
</style>

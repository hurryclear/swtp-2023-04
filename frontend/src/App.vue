<template>
    <v-app class="v-application">
      <DarkThemeToggle/>
      <br/>
      <ApplicationForm/>
    </v-app>
</template>

<script>
import DarkThemeToggle from "@/components/DarkThemeToggle.vue";
import ApplicationForm from "@/components/ApplicationForm.vue";
import {defineComponent} from "vue";

export default defineComponent({
  components: {DarkThemeToggle, ApplicationForm, }
})

</script>

<style>
  .v-application {
    transition: background-color 1s ease, color 1s ease; /*Dark Mode Transition*/
    padding: 0.5rem;
  }
</style>
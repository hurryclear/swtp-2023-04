<template>
    <v-app class="v-application">
      <v-container>
        <LanguageSwitcher/>
      </v-container>
      <br/>
      <DarkThemeToggle/>
      <br/>
      <ApplicationForm/>
    </v-app>
</template>

<script>
import DarkThemeToggle from "@/components/DarkThemeToggle.vue";
import ApplicationForm from "@/components/ApplicationForm.vue";
import LanguageSwitcher from "@/components/LanguageSwitcher.vue";
import {defineComponent} from "vue";

export default defineComponent({
  components: {DarkThemeToggle, ApplicationForm, LanguageSwitcher}
})

</script>


<style>
  .v-application {
    padding: 0.5rem;
  }
  .v-application > * {
    transition: background-color 1s ease, color 1s ease; /*Dark Mode Transition*/
  }
</style>
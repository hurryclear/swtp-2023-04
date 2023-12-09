<template>
    <v-app class="v-application">
      <v-container>
        <NavigationMenu/>
      </v-container>
      
      <div class="main-container">
        <router-view/>
      </div>
    </v-app>
</template>

<script>


import {defineComponent} from "vue";
import NavigationMenu from "@/components/NavigationMenu.vue";

export default defineComponent({
  components: {NavigationMenu}
})

</script>

<style>
  .v-application > * {
    transition: background-color 1s ease, color 1s ease; /*Dark Mode Transition*/
  }
  .main-container {
    margin:1rem
  }
</style>
<template>
    <v-app class="v-application">
      <v-container>
        <NavigationMenu/>
      </v-container>
      <div class="main-container d-flex align-center">
        <v-row>
          <v-col>
            <LanguageSwitcher/>
          </v-col>
          <v-col>
            <DarkThemeToggle
                style="
                  height:100%;
                  width:100%;
                "
            />
          </v-col>
        </v-row>
      </div>
      <v-divider thickness="3"/>
      <div class="main-container">
        <router-view/>
      </div>
    </v-app>
</template>

<script>
import DarkThemeToggle from "@/components/DarkThemeToggle.vue";
import LanguageSwitcher from "@/components/LanguageSwitcher.vue";
import {defineComponent} from "vue";
import NavigationMenu from "@/components/NavigationMenu.vue";

export default defineComponent({
  components: {NavigationMenu, DarkThemeToggle, LanguageSwitcher}
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
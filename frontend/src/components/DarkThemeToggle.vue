<template>
  <!-- Toggle button for switching between light and dark mode -->
  <v-btn @click="toggleTheme">
    {{ $t("darkMode.toggleDarkMode") }}
  </v-btn>
</template>

<script setup>
import { useTheme } from 'vuetify'

// Use the Vuetify theme functionality
const theme = useTheme()


// Function to toggle between light and dark mode
function toggleTheme() {
  // Set the theme to light if it's currently dark, and vice versa
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}

</script>

<style scoped>
/* Styles specific to this component go here if needed */
</style>

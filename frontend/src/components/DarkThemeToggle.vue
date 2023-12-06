<template>
  <v-btn @click="toggleTheme" variant="outlined" class="v-btn">
    {{ $t("darkMode.toggleDarkMode") }}
  </v-btn>
</template>

<script setup>
import { useTheme } from 'vuetify'

const theme = useTheme()

function toggleTheme() {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}
</script>
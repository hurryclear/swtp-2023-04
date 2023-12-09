<template>
  <v-btn class="button-spacing" variant="elevated" color="#262A31" :icon="themeIcon" @click="toggleTheme"></v-btn>
</template>

<script setup>
import { useTheme } from 'vuetify'
import { computed } from 'vue'

const theme = useTheme()

const themeIcon = computed(() => {
  return theme.global.current.value.dark ? 'mdi-brightness-7' : 'mdi-brightness-4';
});

function toggleTheme() {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}
</script>

<style scoped>
  .button-spacing {
  margin-left: 8px; 
  margin-right: 8px; 
  margin-top: 8px;
  margin-bottom: 8px;
  }

</style>
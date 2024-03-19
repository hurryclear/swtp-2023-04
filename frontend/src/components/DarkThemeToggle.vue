<template>
  <v-btn class="button-spacing" variant="elevated" color="#262A31" icon @click="toggleTheme">
    <v-icon>{{themeIcon}}</v-icon>
    <v-tooltip
        activator="parent"
        location="bottom"
    >
      {{ $t("darkMode.toggleDarkMode") }}
    </v-tooltip>
  </v-btn>
</template>

<script setup>
import {useTheme} from 'vuetify'
import {computed} from 'vue'

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
  margin: 8px;
}

</style>
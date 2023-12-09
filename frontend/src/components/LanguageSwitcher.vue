<template>
  <v-menu>
    <template v-slot:activator="{ props }">
      <v-btn 
        class="button-spacing"
        icon="mdi-translate" 
        variant="elevated" 
        color="#262A31"
        v-bind="props">
      </v-btn>
    </template>
      <v-list>
        <v-list-item v-for="item in languageOptions" :key="item.title" @click="changeLanguage(item.value)">
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list>
  </v-menu>
</template>

<script>
export default {
  data() {
    return {
      languageOptions: [
        { title: '🇬🇧 English', value: 'en' },
        { title: '🇩🇪 Deutsch', value: 'de' },
        { title: '🇫🇷 Français', value: 'fr' },
        { title: '🇪🇸 Español', value: 'es' },
        { title: '🇨🇳 中文', value: 'zh' },
        { title: '🇮🇹 Italiano', value: 'it' },
        { title: '🇷🇺 Русский', value: 'ru' },
        { title: '🇯🇵 日本語', value: 'ja' },
        { title: '🇰🇷 한국어', value: 'ko' },
        { title: '🇵🇹 Português', value: 'pt' },
      ],
      showSelect: false,
    };
  },

  methods: {
    changeLanguage(language) {
      this.$i18n.locale = language;
    },
  },
};
</script>

<style scoped>
  .button-spacing {
  margin-left: 8px; 
  margin-right: 8px; 
  margin-top: 8px;
  margin-bottom: 8px;
  }

</style>

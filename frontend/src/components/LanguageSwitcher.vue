<template>
  <v-menu>
    <template v-slot:activator="{ props }">
      <v-btn
          class="button-spacing"
          icon="mdi-translate"
          variant="elevated"
          color="#262A31"
          v-bind="props"/>
    </template>
    <v-list>
      <v-list-item
          v-for="language in languageOptions"
          :title="language.title"
          :key="language.value"
          @click="changeLanguage(language.value)"
      />
    </v-list>
  </v-menu>
</template>

<script>
import i18n from "@/plugins/i18n";

export default {
  data() {
    return {
      languageOptions: [
        {title: '🇬🇧 English', value: 'en'},
        {title: '🇩🇪 Deutsch', value: 'de'},
        {title: '🇫🇷 Français', value: 'fr'},
        {title: '🇪🇸 Español', value: 'es'},
        {title: '🇨🇳 中文', value: 'zh'},
        {title: '🇮🇹 Italiano', value: 'it'},
        {title: '🇷🇺 Русский', value: 'ru'},
        {title: '🇯🇵 日本語', value: 'ja'},
        {title: '🇰🇷 한국어', value: 'ko'},
        {title: '🇵🇹 Português', value: 'pt'},
      ],
      showSelect: false,
    };
  },

  methods: {
    changeLanguage(language) {
      this.$i18n.locale = language;
      document.title = i18n.global.t(`routes.${this.$route.path}`);
    },
  },
};
</script>

<style scoped>
.button-spacing {
  margin: 8px;
}

</style>

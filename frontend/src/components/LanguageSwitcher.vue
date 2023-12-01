<template>
  <v-select
      v-model="$i18n.locale"
      :items="languageOptions"
      label="Select Language"
      variant="outlined"
      hide-details
  />
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
    };
  },
};
</script>

<style scoped>
/* Add scoped styles if needed */
</style>

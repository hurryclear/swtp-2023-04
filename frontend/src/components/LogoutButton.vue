<template>
  <v-btn
      class="button-spacing"
      variant="elevated"
      color="#262A31"
      icon
      @click="logout()"
  >
    <v-icon>mdi-logout</v-icon>
    <v-tooltip
        activator="parent"
        location="bottom"
    >
      {{ $t("logoutButton") }}
    </v-tooltip>
  </v-btn>
</template>

<script>

export default {
  name: 'LogoutButton',
  methods: {
    async logout() {
      try {
        await this.$store.dispatch('logout');
      } catch (error) {
        console.error('Logout failed:', error);
      }
    },
  },
}
</script>
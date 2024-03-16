<template>
  <v-btn
      class="button-spacing"
      variant="elevated"
      color="#262A31"
      icon="mdi-logout"
      @click="logout()"
  />
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
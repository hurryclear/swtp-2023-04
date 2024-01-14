<template>
  <v-col>
    <v-row justify="center">
      <v-btn
          color="red"
          varient="outlined"
          @click="logout">
        {{ $t('logoutButton') }}
      </v-btn>
    </v-row>
  </v-col>
</template>

<script>
export default {
  name: 'LogoutButton',
  methods: {
    logout() {
      this.$store.dispatch('logoutUser');
      this.$router.push('/login');
    }
  }
}
</script>
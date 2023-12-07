<template>
  <v-container>
    <v-form @submit.prevent="submitLogin">
      <v-col>
        <v-text-field
          label="Username"
          v-model="username"
          required
          variant="outlined"
        />
        <v-text-field
          label="Password"
          :type="showPassword ? 'text' : 'password'"
          v-model="password"
          required
          :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
          @click:append-inner="togglePasswordVisibility"
          variant="outlined"
        />
        <v-alert
          v-if="loginError"
          type="error"
          dismissible
        >
          Incorrect username or password.
        </v-alert>
      </v-col>
      <v-row justify="center">
        <v-btn type="submit" variant="outlined">
          Login
        </v-btn>
      </v-row>
    </v-form>
  </v-container>
</template>

<script>
export default {
  name: 'LoginComponent',
  data() {
    return {
      username: '',
      password: '',
      showPassword: false,
      loginError: false,
    };
  },
  methods: {
    submitLogin() {
      const dummyUsername = 'admin';
      const dummyPassword = 'password';

      if (this.username === dummyUsername && this.password === dummyPassword) {
        //this.$store.dispatch('login');
        //this.loginError = false;
        this.$router.push('/review-application');
      } else {
        this.loginError = true;
      }
    },
    togglePasswordVisibility() {
      this.showPassword = !this.showPassword;
    },
  },
};
</script>

<template>
  <v-container>
    <v-form @submit.prevent="submitLogin">
      <v-col>
        <v-text-field
          :label="$t('loginComponent.usernameLabel')"
          v-model="username"
          required
          variant="outlined"
        />
        <v-text-field
          :label="$t('loginComponent.passwordLabel')"
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
        {{ $t('loginComponent.loginErrorMessage') }}
        </v-alert>
      </v-col>
      
      <v-row justify="center">
        <v-btn 
        type="submit" 
        variant="outlined"
        >
        {{ $t('loginComponent.loginButton') }}
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
      //hier api schnittstelle einbauen um richtige daten zu bekommen
      const dummyUsername = 'admin';
      const dummyPassword = 'password';

      if (this.username === dummyUsername && this.password === dummyPassword) {
        this.$store.dispatch('authenticateUser', true);
        this.$router.push('/study-office');
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

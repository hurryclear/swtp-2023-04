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
      <br/>
      <v-row justify="center">
        <v-btn
            color="blue"
            type="submit"
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
    async submitLogin() {
      try {
        await this.$store.dispatch('authenticateUser', {
          username: this.username,
          password: this.password,
        });

        // Redirect based on user role
        const role = this.$store.state.authentication.userRole;
        if (role === 'ROLE_OFFICE') {
          this.$router.push('/student-affairs-office');
        } else if (role === 'ROLE_COMMITTEE') {
          this.$router.push('/examining-committee-chair');
        }
      } catch (error) {
        this.loginError = true;
        console.error('Login failed:', error);
      }
    },
    togglePasswordVisibility() {
      this.showPassword = !this.showPassword;
    },
  },
};
</script>

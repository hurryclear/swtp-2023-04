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
import axios from "@/plugins/axios"
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
        const response = await axios.post('/api/auth/login', {
          username: this.username,
          password: this.password
        });

        const { token, role } = response.data;

        // Store the token in local storage
        localStorage.setItem('token', token);

        // Set the user role and token in the store
        this.$store.dispatch('authenticateUser', { status: true, role, token });

        // Redirect based on user role
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

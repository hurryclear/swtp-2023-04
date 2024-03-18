<template>
  <v-container>
    <v-card class="mb-4" elevation="10">
      <v-card-title :style="{fontWeight: 'bold'}">{{ $t('loginComponent.loginButton') }}</v-card-title>
          <v-card-text>
           
              <v-text-field
                  :label="$t('loginComponent.usernameLabel')"
                  v-model="username"
                  dense
                  required
                  variant="outlined"
                  @keyup.enter="submitLogin"
              />
              <v-text-field
                  :label="$t('loginComponent.passwordLabel')"
                  :type="showPassword ? 'text' : 'password'"
                  v-model="password"
                  dense
                  required
                  @keyup.enter="submitLogin"
                  :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                  @click:append-inner="togglePasswordVisibility"
                  variant="outlined"
              />
            <!-- Button to check Login -->
            <v-btn color="primary" @click="submitLogin">{{ $t('loginComponent.loginButton') }}</v-btn>
        </v-card-text>
    </v-card>
    <v-alert v-if="loginError" type="error" color="red">
      {{ $t('loginComponent.loginErrorMessage') }}
    </v-alert>
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
        const response = await this.$store.dispatch('authenticateUser', {
          username: this.username,
          password: this.password,
        });

        if(response.success){
          // Redirect based on user role
          const role = this.$store.state.authentication.userRole;
          if (role === 'ROLE_OFFICE') {
            this.$router.push('/student-affairs-office');
          } else if (role === 'ROLE_COMMITTEE') {
            this.$router.push('/examining-committee-chair');
          }
        } else {
          this.loginError = true;
          console.error('Login failed:', response.error);
        }
      } catch (error) {
        this.loginError = true;
        console.error('Unexpected Error:', error);
      }
    },
    togglePasswordVisibility() {
      this.showPassword = !this.showPassword;
    },
  },
};
</script>

<style scoped>
  .mb-4 {
    margin-bottom: 16px;
  }
</style>

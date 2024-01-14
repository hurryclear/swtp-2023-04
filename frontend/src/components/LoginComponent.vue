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
import axios from "@/plugins/axios";

export default {
  name: 'LoginComponent',
  data() {
    return {
      username: '',
      password: '',
      showPassword: false,
      loginError: false,
      //TODO: Remove after backend implementation
      dummyLogins: {
        examiningCommitteeChair: {username: 'Prüfungsausschuss', password: '1234'},
        studyOffice: {username: 'Studienbüro', password: '1234'},
      },
    };
  },
  methods: {
    submitLogin() {
      let role;
      if (this.username === this.dummyLogins.examiningCommitteeChair.username &&
          this.password === this.dummyLogins.examiningCommitteeChair.password) {
        role = 'examiningCommitteeChair';
        console.log('User role set to:', role);
      } else if (this.username === this.dummyLogins.studyOffice.username &&
          this.password === this.dummyLogins.studyOffice.password) {
        role = 'studentAffairsOffice';
        console.log('User role set to:', role);
      }

      if (role) {
        this.$store.dispatch('authenticateUser', {status: true, role});
        if (role === 'examiningCommitteeChair') {
          this.$router.push('/pruefungsausschuss');
        } else if (role === 'studentAffairsOffice') {
          this.$router.push('/study-office');
        }
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

<template>
  <v-card>
    <v-card-text style="margin:1rem;font-size: 16px; text-align: justify;">
      <!-- Dynamic Content for Different User Roles -->
      <v-row v-if="userRole === 'ROLE_STUDENT'">
        <p>
          {{ $t('homeView.welcomeStudent') }}
        </p>
      </v-row>
      <v-row v-else-if="userRole === 'ROLE_OFFICE'">
        <p>
          {{ $t('homeView.welcomeStudentAffairsOffice') }}
        </p>
      </v-row>
      <v-row v-else-if="userRole === 'ROLE_COMMITTEE'">
        <p>
          {{ $t('homeView.welcomeExaminingCommitteeChair') }}
        </p>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script>
import store from "@/store";

export default {
  computed: {
    userRole() {
      return store.state.userRole;
    },
  }
};
</script>

<template>
  <v-container>
    <!-- Dynamic Content for Different User Roles -->
    <v-row v-if="userRole === 'student'">
      <p>
        {{$t('homeView.welcomeStudent')}}
      </p>
    </v-row>
    <v-row v-else-if="userRole === 'studentAffairsOffice'">
      <p>
        {{$t('homeView.welcomeStudentAffairsOffice')}}
      </p>
    </v-row>
    <v-row v-else-if="userRole === 'examiningCommitteeChair'">
      <p>
        {{$t('homeView.welcomeExaminingCommitteeChair')}}
      </p>
    </v-row>
  </v-container>
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

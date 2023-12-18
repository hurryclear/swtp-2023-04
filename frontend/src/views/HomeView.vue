<template>
  <v-container>
    <!-- Dynamic Content for Different User Roles -->
    <v-row v-if="userRole === 'student'">
      <!-- Content for Students -->

      <p>This is the home view as student</p>
    </v-row>
    <v-row v-else-if="userRole === 'studentAffairsOffice'">
      <!-- Content for student affairs office -->

      <p>This is the home view as student affairs office</p>
    </v-row>
    <v-row v-else-if="userRole === 'examiningCommitteeChair'">
      <!-- Content for Committee Chair -->
      <p>This is the home view as committee</p>
    </v-row>

    <!-- Your Main Page Content Goes Here -->

  </v-container>
</template>

<script>
export default {
  data() {
    return {
      userRole: 'student', // Set the default user role
    };
  },
  methods: {
    setUserRole(role) {
      this.userRole = role;
    },
  },
};
</script>

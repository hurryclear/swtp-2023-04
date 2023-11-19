<template>
  <v-container>
    <!-- Dynamic Content for Different User Roles -->
    <v-row v-if="userRole === 'student'">
      <!-- Content for Students -->

      <p>This is the home view as student</p>
    </v-row>
    <v-row v-else-if="userRole === 'university'">
      <!-- Content for University -->

      <p>This is the home view as university</p>
    </v-row>
    <v-row v-else-if="userRole === 'committee'">
      <!-- Content for Committee -->
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

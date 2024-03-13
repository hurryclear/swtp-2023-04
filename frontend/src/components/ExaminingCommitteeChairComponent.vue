<template>
    <div>
        <v-btn>
            <v-icon @click="logApplications">mdi-pencil</v-icon>
        </v-btn>
    </div>
    <div>
    <v-card>
        <table>
        <thead>
          <tr>
            <th>Application ID</th>
            <th>Status</th>
            <th>Date of Submission</th>
            <th>Date Last Edited</th>
            <th>University Name</th>
            <th>Student Major</th>
            <th>University Major</th>
            <!-- Add more headers if needed -->
          </tr>
        </thead>
        <tbody>
          <tr v-for="application in applications" :key="application.applicationKeyClass.id">
            <td>{{ application.applicationKeyClass.id }}</td>
            <td>{{ application.status }}</td>
            <td>{{ application.dateOfSubmission }}</td>
            <td>{{ application.dateLastEdited }}</td>
            <td>{{ application.universityName }}</td>
            <td>{{ application.studentMajor }}</td>
            <td>{{ application.uniMajor }}</td>
            <!-- Add more data fields if needed -->
          </tr>
        </tbody>
      </table>
    </v-card>
      
    </div>
  </template>
  
  <script>
  import { mapActions } from 'vuex';
  
  export default {
    data() {
      return {
        applications: []
      };
    },
    created() {
        this.fetchApplications();
    },
    
    watch: {
      '$store.state.form.applications': {
        immediate: true,
        handler(applications) {
          this.applications = applications;
        },
      }
    },
    methods: {
      ...mapActions(['fetchApplications']),
      logApplications() {
        console.log(this.applications);
      }
    }
  };
  </script>
  
  <style>
  /* Add styles as needed */
  </style>
  
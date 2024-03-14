<template>
  <v-container>
    <v-text-field 
      v-model="searchQuery" 
      label="Search" 
      @input="searchApplications">
    </v-text-field>

    <!-- Data Table -->
    <v-data-table 
      v-if="!showDetails && !showCompare" 
      :headers="headers" 
      :items="applications" 
      class="elevation-1">

      <template v-slot:[`item.action`]="{ item }">
        <v-btn color="primary" @click="openApplication(item.applicationKeyClass.id)">
          Open
        </v-btn>
      </template>

    </v-data-table>

    <!-- Detailed Application View -->
  <v-btn @click="backToList">Back to List</v-btn>

    <v-divider></v-divider>

    <v-row v-if="showDetails">
      <v-col cols="12" >
        <v-card>
          <v-card-title>
            <h2><span style="font-weight: bold; font-size: large;">{{ $t('reviewComponent.overview') }}:</span></h2>
          </v-card-title>
          <v-card-text>
            <div><span style="font-weight: bold;">{{$t('reviewComponent.currentUniversity')}}:</span> {{ selectedApplication.original.applicationData.university }}</div>
            <div><span style="font-weight: bold;">{{$t('reviewComponent.oldCourseOfStudy')}}:</span> {{ selectedApplication.original.applicationData.oldCourseOfStudy }}</div>
            <div><span style="font-weight: bold;">{{$t('reviewComponent.newCourseOfStudy')}}:</span> {{ selectedApplication.original.applicationData.newCourseOfStudy }}</div>
            <div><span style="font-weight: bold;">{{$t('reviewComponent.dateOfSubmission')}}:</span> {{ new Date(selectedApplication.original.applicationData.dateOfSubmission).toLocaleString() }}</div>
            <div><span style="font-weight: bold;">{{$t('reviewComponent.dateLastEdited')}}:</span> {{ new Date(selectedApplication.original.applicationData.dateLastEdited).toLocaleString() }}</div>
            <div v-for="moduleForm in selectedApplication.original.moduleFormsData" :key="moduleForm.frontend_key">
              {{$t('reviewComponent.moduleBlock')}} {{ moduleForm.frontend_key + 1 }}
              <v-div v-for="module in moduleForm.modulesStudent" :key="module.frontend_key">
                <div><span style="font-weight: bold;">{{$t('reviewComponent.modulName')}}:</span> {{ module.title }} |  
                <span style="font-weight: bold;">{{$t('reviewComponent.moduleNumber')}}:</span> {{ module.number }} |  
                <span style="font-weight: bold;">{{$t('reviewComponent.moduleCredits')}}:</span> {{ module.credits }} </div>
                <v-btn small color="success" @click="updateApprovalStatus(module, 'accepted')">Accept</v-btn>
                <v-btn small color="error" @click="updateApprovalStatus(module, 'rejected')">Reject</v-btn>
              <div>
                <span style="font-weight: bold;">{{$t('reviewComponent.moduleApproval')}}: </span> {{ module.approval }}
              </div>

              

              <div v-for="moduleToCredit in moduleForm.modules2bCredited" :key="moduleToCredit.number">
                {{ moduleToCredit.name }} ({{ moduleToCredit.number }})
              </div>

              </v-div>
            </div>
            <v-btn color="primary" @click="handleSave">Save Changes</v-btn>
          </v-card-text>
          <v-card-actions>
            <v-btn color="secondary" @click="showCompareTable">Compare</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
      <v-col v-if="compareSelected" cols="12" >
        <v-card>
          <v-card-title>
            <h2>Application Details</h2>
          </v-card-title>
          <v-card-text>
            <p>ID: {{ comparisonApplication.original.applicationData.applicationID }}</p>
            <p>Status: {{ comparisonApplication.original.applicationData.status }}</p>
            <p>University Name: {{ comparisonApplication.original.applicationData.university }}</p>
            <!-- ...other details... -->
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <div v-if="showCompare">
      <v-btn @click="backToDetails">Back to Details</v-btn>

      <!-- Comparison Data Table -->
      <v-row>
        <v-col cols="12">
          <v-data-table 
            :headers="headers" 
            :items="applications" 
            class="elevation-1">
            
            <template v-slot:[`item.action`]="{ item }">
              <v-btn color="primary" @click="startCompare(item.applicationKeyClass.id)">
                Select
              </v-btn>
            </template>
          </v-data-table>
        </v-col>
      </v-row>
    </div>

  </v-container>
</template>

<script>
import { mapActions } from 'vuex';
import CommitteeService from '@/services/CommitteeService';


export default {
  data() {
    return {
      searchQuery: '',
      applications: [],
      selectedApplication: null,
      comparisonApplication: null,
      showDetails: false,
      showCompare: false,
      compareSelected: false,
      headers: [
        { text: 'Application ID', value: 'applicationKeyClass.id' },
        { text: 'Status', value: 'status' },
        { text: 'Actions', value: 'action', sortable: false },
        { text: 'University', value: 'universityName'}
      ],
    };
  },

  created() {
    this.fetchApplications();
  },

  watch: {
    '$store.state.form.applications': {
      immediate: true,
      handler(newVal) {
        this.applications = newVal; 
      }
    },
    '$store.state.form.currentForm': {
      immediate: true,
      handler(newVal) {
        this.form = newVal; 
      }
    }
  },

  methods: {
    ...mapActions(['fetchApplications', 'fetchApplication']),

    async openApplication(applicationId) {
      try {
        await this.fetchApplication(applicationId);
          if (this.form) {
            this.selectedApplication = this.form;
            this.showDetails = true;
          } else {
            console.error('No details found for the application');
          }
      } catch (error) {
          console.error('Error fetching application details:', error);
      }
    },
    
    backToList() {
      this.showDetails = false;
      this.showCompare = false;
    },

    showCompareTable() {
      this.showCompare = true;
    },  

    async startCompare(applicationId) {
      try {
        await this.fetchApplication(applicationId);
          if (this.form) {
            this.comparisonApplication = this.form;
            this.compareSelected = true;
          } else {
            console.error('No details found for the application');
          }
      } catch (error) {
        console.error('Error fetching application details:', error);
      }
    },

    backToDetails() {
      this.showCompare = false;
      this.compareSelected = false;
    },

    selectForComparison(item) {
      this.comparisonApplication = item;
    },

    async searchApplications() {
      try {
        const searchCriteria = { query: this.searchQuery };
        this.applications = await this.fetchApplications(searchCriteria);
      } catch (error) {
        console.error('Error searching applications:', error);
      }
    },
  },

  statusTranslation(approval) {
      if (approval === 'accepted') {
        return this.$t('reviewComponent.accepted');
      } else if (approval === 'rejected') {
        return this.$t('reviewComponent.rejected');
      } else if (approval === 'formally rejected') {
        return this.$t('reviewComponent.formallyRejected');
      } else {
        return 'pending';
      }
    },

    handleSave() {
      const editedApplicationData = this.getEditedApplicationData();

      CommitteeService.saveEdited(editedApplicationData)
        .then(response => {
          // Handle successful save
          console.log('Save successful:', response);
        })
        .catch(error => {
          // Handle errors in saving
          console.error('Error saving:', error);
        });
    },

};
</script>




<style scoped>
.full-height-dialog .v-card {
  height: 90vh; /* 90% of the viewport height */
}
</style>
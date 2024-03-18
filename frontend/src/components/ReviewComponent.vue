<template>
  <v-container>
    <!-- Form validation area -->
    <v-col v-if="!isDataVisible">
      <v-card class="mb-4" elevation="10">
        <!-- Card title for review application -->
        <v-card-title :style="{fontWeight: 'bold'}">{{ $t('reviewComponent.reviewApplication') }}</v-card-title>
          <v-card-text>
            <!-- Text field for entering form ID -->
            <v-text-field
                :label="$t('reviewComponent.formID')"
                v-model="formId"
                @keyup.enter="checkStatus"
                variant="outlined"
                dense
            />
            <!-- Button to check the status -->
            <v-btn @click="checkStatus" color="primary">{{ $t('reviewComponent.checkStatus') }}</v-btn>
        </v-card-text>
      </v-card>
    </v-col>
    
    <!-- Area to display data if it is visible -->
    <v-row v-if="isDataVisible">
        <v-col cols="12">
          <v-card class="mb-4" elevation="10">
            <v-card-title>
              <v-row justify="space-between" no-gutters>
                <v-col cols="auto" class="text-left">
                  <!-- Overview title -->
                  <div><span style="font-weight: bold; font-size: large;">{{ $t('reviewComponent.overview') }}:</span></div>
                </v-col>
              </v-row>
            </v-card-title>

            <v-col cols="12">
              <!-- Card showing the application status -->
              <v-card :color="determineStatusColor(form.applicationData.status)">
                <v-card-text class="text-center" style="color: white;">
                  <!-- Status information -->
                  <span style="font-weight: bold;">{{determineStatusTranslations(form.applicationData.status)}}</span>
                </v-card-text>
              </v-card>
            </v-col>

            <v-row>
              <!-- Displaying application details -->
              <v-col cols="12" sm="6">
                <v-card-text>
                  <!-- Information fields for the application -->
                  <div><span style="font-weight: bold;">{{$t('reviewComponent.currentUniversity')}}:</span> {{ form.applicationData.university }}</div>
                  <div><span style="font-weight: bold;">{{$t('reviewComponent.oldCourseOfStudy')}}:</span> {{ form.applicationData.oldCourseOfStudy }}</div>
                  <div><span style="font-weight: bold;">{{$t('reviewComponent.newCourseOfStudy')}}:</span> {{ form.applicationData.newCourseOfStudy }}</div>
                  <div><span style="font-weight: bold;">{{$t('reviewComponent.dateOfSubmission')}}:</span> {{ new Date(form.applicationData.dateOfSubmission).toLocaleString() }}</div>
                  <div><span style="font-weight: bold;">{{$t('reviewComponent.dateLastEdited')}}:</span> {{ new Date(form.applicationData.dateLastEdited).toLocaleString() }}</div>
                  <div class="d-flex align-center"><span style="font-weight: bold;">{{$t('reviewComponent.formID')}}:</span> {{ form.applicationData.applicationID }} <v-btn size="x-small" color="primary" icon="mdi-content-copy" variant="plain" @click="copyToClipboard"></v-btn></div>
                </v-card-text>
              </v-col>

              <!-- Button for downloading the application form -->
              <v-col cols="12" sm="6" class="d-flex align-center justify-center">
                <div>
                  <v-btn class="mb-4" color="primary" variant="elevated" @click="downloadForm" append-icon="mdi-download">
                    <span style="font-weight: bold;">{{ $t('reviewComponent.downloadApplication') }}</span>
                  </v-btn>
                </div>
              </v-col>
            </v-row>
          </v-card>
        </v-col>
      
      <!-- Modules Section -->
      <v-row>
        <v-col cols="12">
          <v-expansion-panels>
            <!-- Loop over module forms -->
            <v-expansion-panel 
              v-for="moduleForm in form.moduleFormsData" 
              :key="moduleForm.frontend_key"
              class="elevation-10"
            >
              <!-- Expansion panel title -->
              <v-expansion-panel-title class="hide-actions">
                <template v-slot:default>
                  <v-row no-gutters>
                    <v-col cols="8" class="d-flex align-center">
                      <!-- Module block title -->
                      {{$t('reviewComponent.moduleBlock')}} {{ moduleForm.frontend_key + 1 }}
                    </v-col>

                    <v-col cols="4">
                      <!-- Card displaying approval status of the module block -->
                      <v-card :color="getBlockApprovalColor(moduleForm.modulesStudent)">
                          <v-card-text class="text-center"><span style="font-weight: bold; color: white;"> {{ getBlockApprovalStatus(moduleForm.modulesStudent) }}</span></v-card-text>
                      </v-card>
                    </v-col>
                  </v-row>
                </template>
              </v-expansion-panel-title>
              
              <!-- Expansion panel content -->
              <v-expansion-panel-text>
                <!-- Detailed module information -->
                <div class="my-content" v-for="module in moduleForm.modulesStudent" :key="module.frontend_key">
                  <div>
                    <!-- Module details -->
                    <span style="font-weight: bold;">{{$t('reviewComponent.moduleName')}}:</span> {{ module.title }} |     
                    <span style="font-weight: bold;">{{$t('reviewComponent.moduleNumber')}}:</span> {{ module.number }} |  
                    <span style="font-weight: bold;">{{$t('reviewComponent.moduleCredits')}}:</span> {{ module.credits }} 
                  </div>
                  
                  <!-- Module approval information -->
                  <div v-if="module.approval !== ''">
                    <span style="font-weight: bold;">{{$t('reviewComponent.moduleApproval')}}: </span> 
                    <span :style="{ color: determineTextColor(module.approval) }">{{ statusTranslation(module.approval) }}</span>
                  </div>
                  <div v-else>
                    <span style="font-weight: bold;">{{$t('reviewComponent.moduleApproval')}}: </span> 
                    <span style="color: blue;" >{{$t('reviewComponent.moduleApprovalOpen')}}</span>
                  </div>
                  
                  <!-- Reason for module approval or disapproval -->
                  <div v-if="module.reason !== ''">
                    <span style="font-weight: bold;">{{$t('reviewComponent.moduleApprovalReason')}}: </span> {{ module.reason }}
                  </div>
                </div>
                <v-divider class="my-divider"></v-divider>

                <!-- Displaying modules to be credited -->
                <div class="my-content" v-if="moduleForm.modules2bCredited && moduleForm.modules2bCredited.length">
                  <div>
                    <span style="font-weight: bold;">{{$t('reviewComponent.modulesToBeCredited')}}:</span>
                  </div>

                  <!-- List of modules to be credited -->
                  <ul class="indented-list">
                    <li v-for="moduleToCredit in moduleForm.modules2bCredited" :key="moduleToCredit.number">
                      {{ moduleToCredit.name }} ({{ moduleToCredit.number }})
                    </li>
                  </ul>
                </div>
              </v-expansion-panel-text>
            </v-expansion-panel>
          </v-expansion-panels>
        </v-col>
      </v-row>
    </v-row>

    <!-- Alert section for status messages -->
    <v-col v-else-if="status">
      <v-alert type="info" :color="statusColor">
        {{ determineStatusTranslations(status) }}
      </v-alert>
    </v-col>
  </v-container>
</template>

<script>
import { mapActions } from 'vuex';
import axios from '@/plugins/axios';


export default {
  /**
   * Data properties of the Vue component.
   * @returns {Object} The initial data for the Vue component.
   */
  data() {
    return {
      form: null,                    // Represents the form data
      isDataVisible: false,          // Determines if the data should be visible
      formId: '',                    // Stores the form ID
      status: null,                  // Stores the current status of the form
      statusMessage: '',             // Message representing the current status
      statusColor: '',               // Color representing the current status
    };
  },
  props: {
    applicationId: String
  },
  

  // Watchers for Vue component
  watch: {
    /**
     * Watcher for changes in the current form in the Vuex store.
     */
    '$store.state.form.currentForm': {
      immediate: true,
      handler(newVal) {
        this.form = newVal; // Update the local form data when the store's form data changes
      }
    },

    applicationId(newVal) {
      this.formId = newVal;
      console.log('Updated Form ID:', this.formId);
      if (this.formId) {
        this.checkStatus();
      }
    }
  },

  // Methods of the Vue component
  methods: {
    ...mapActions(['fetchApplicationSummary', 'fetchPdfSummary']), // Mapping Vuex actions for reuse

    /**
     * Checks the status of an application.
     * Asynchronous method to fetch the application summary and update the component's state.
     */
    async checkStatus() {
      try {
        await this.fetchApplicationSummary(this.formId); // Fetch the application summary based on form ID
        console.log('Form:', this.form); 

        // Check if the form data is available and match the form ID
        if (this.form && this.form.applicationData.applicationID === this.formId) {
          this.isDataVisible = true;
          this.statusMessage = `Status: ${this.form.applicationData.status}`;
          this.statusColor = this.determineStatusColor(this.form.applicationData.status);
        } else {
          this.isDataVisible = false;
          this.status = 'Form not found';
          this.statusColor = 'red';
        }
      } catch (error) {
        console.error('Error in checkStatus:', error);
        this.isDataVisible = false;
        this.status = 'Error fetching form';
        this.statusColor = 'red';
      }
    },

    /**
     * Downloads the form as a PDF.
     * 
     * This method asynchronously fetches PDF data from a specified API endpoint
     * using the form ID from the component's state. It then creates a temporary 
     * link in the DOM to trigger the download of the PDF file. After the download
     * initiates, it cleans up by removing the temporary link and revoking the 
     * created URL object.
     *
     * @async
     * @function downloadForm
     * @throws Will log an error to the console if the PDF download fails.
     */
    async downloadForm() {
        try {
            // Retrieve the form ID from the component's state.
            const applicationId = this.form.applicationData.applicationID;

            // Fetch the PDF as a blob from the API.
            const response = await axios.get("/api/student/getPdfSummary", {
                params: { applicationId },
                responseType: 'blob'
            });

            // Logging the response for debugging purposes.
            console.log('PDF response:', response);

            // Create an object URL for the blob and set up a temporary download link.
            const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
            const filename = `Application-${applicationId}.pdf`;
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', filename); // Consider using a dynamic filename.

            // Append the link to the DOM and trigger the download.
            document.body.appendChild(link);
            link.click();

            // Clean up: revoke the object URL and remove the temporary link.
            window.URL.revokeObjectURL(url);
            link.parentNode.removeChild(link);
        } catch (error) {
            // Log any errors encountered during the download process.
            console.error('Error downloading PDF:', error);
        }
    },



    /**
     * Determines the color associated with a given status.
     * @param {string} status - The current status of the item.
     * @returns {string} The color corresponding to the status.
     */
    determineStatusColor(status) {
      switch (status) {
        case 'open':
          return 'blue';
        case 'editing in progress':
          return 'orange';
        case 'edited':
          return 'orange';
        case 'ready for approval':
          return 'orange';
        case 'approval in progress':
          return 'orange';
        case 'edited approval':
          return 'orange';
        case 'approval finished':
          return 'orange';
        case 'accepted':
          return 'green';
        case 'rejected':
          return 'red';
        case 'formally rejected':
          return 'red';
        case 'Error fetching form':
          return 'red';
        case 'Form not found':
          return 'red';
        default:
          return 'grey';
      }
    },

    /**
     * Retrieves the translation for a given status.
     * @param {string} status - The current status of the item.
     * @returns {string} The translated status text.
     */
    determineStatusTranslations(status) {
      switch (status) {
        case 'open':
          return this.$t('reviewComponent.open');
        case 'editing in progress':
          return this.$t('reviewComponent.editingInProgress');
        case 'edited':
          return this.$t('reviewComponent.edited');
        case 'ready for approval':
          return this.$t('reviewComponent.readyForApproval');
        case 'approval in progress':
          return this.$t('reviewComponent.approvalInProgress');
        case 'edited approval':
          return this.$t('reviewComponent.editedApproval');
        case 'approval finished':
          return this.$t('reviewComponent.approvalFinished');
        case 'accepted':
          return this.$t('reviewComponent.accepted');
        case 'rejected':
          return this.$t('reviewComponent.rejected');
        case 'formally rejected':
          return this.$t('reviewComponent.formallyRejected');
        case 'Error fetching form':
          return this.$t('reviewComponent.errorFetchingForm');
        case 'Form not found':
          return this.$t('reviewComponent.formNotFound');
        default:
          return this.$t('reviewComponent.pending');
      }
    },

    /**
     * Gets the overall approval status of a block based on its modules.
     * @param {Array} modules - Array of modules, each with an approval status.
     * @returns {string} The overall approval status.
     */
    getBlockApprovalStatus(modules) {
      const allApproved = modules.every(module => module.approval === 'accepted');
      const someApproved = modules.some(module => module.approval === 'accepted');
      const someRejected = modules.some(module => module.approval === 'rejected' || module.approval === 'formally rejected');
      const allRejected = modules.every(module => module.approval === 'rejected');
      const allFromallyRejected = modules.every(module => module.approval === 'formally rejected');
      const allOpen = modules.every(module => module.approval === '');

      if (allApproved) {
        return this.$t('reviewComponent.accepted');
      } else if (someApproved) {
        return this.$t('reviewComponent.partiallyAccepted');
      } else if (allOpen) {
        return this.$t('reviewComponent.open');
      } else if (allRejected) {
        return this.$t('reviewComponent.rejected');
      } else if (allFromallyRejected) {
        return this.$t('reviewComponent.formallyRejected');
      } else if (someRejected) {
        return this.$t('reviewComponent.partiallyRejected');
      } else {
        return this.$t('reviewComponent.rejected');
      }
    },

    /**
     * Determines the color representing the overall approval status of a block.
     * @param {Array} modules - Array of modules, each with an approval status.
     * @returns {string} The color representing the overall approval status.
     */
    getBlockApprovalColor(modules) {
      const allApproved = modules.every(module => module.approval === 'accepted');
      const someApproved = modules.some(module => module.approval === 'accepted');
      const someRejected = modules.some(module => module.approval === 'rejected' || module.approval === 'formally rejected');
      const allRejected = modules.every(module => module.approval === 'rejected');
      const allFromallyRejected = modules.every(module => module.approval === 'formally rejected');
      const allOpen = modules.every(module => module.approval === '');

      if (allApproved) {
        return 'green';
      } else if (someApproved) {
        return 'orange';
      } else if (allOpen) {
        return 'blue';
      } else if (allRejected) {
        return 'red';
      } else if (allFromallyRejected) {
        return 'red';
      } else if (someRejected) {
        return 'orange';
      } else {
        return 'red';
      }
    },

    /**
     * Determines the text color based on the approval status.
     * @param {string} approval - The approval status.
     * @returns {string} The color representing the text based on approval status.
     */
    determineTextColor(approval) {
      console.log('Approval:', approval);
      if (approval === 'accepted') {
        return 'green';
      } else if (approval === 'rejected') {
        return 'red';
      } else if (approval === 'formally rejected') {
        return 'red';
      } else {
        return 'black';
      }
    },

    /**
     * Gets the translation of an approval status.
     * @param {string} approval - The approval status.
     * @returns {string} The translated text of the approval status.
     */
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

    copyToClipboard() {
      // Create a temporary textarea element
      const textarea = document.createElement('textarea');
      textarea.value = this.form.applicationData.applicationID;
      document.body.appendChild(textarea);
      
      // Select the text and copy it to the clipboard
      textarea.select();
      document.execCommand('copy');

      // Remove the temporary textarea
      document.body.removeChild(textarea);

      // Optional: Show a message or do something to indicate successful copying
    }
  }
};


</script>

<style scoped>
.mt-2 {
  margin-top: 8px;
}

.mb-4 {
  margin-bottom: 16px;
}

.card-title {
  font-size: 1.25rem;
  font-weight: bold;
}

.card-text {
  font-size: 1rem;
}

.indented-list {
  padding-left: 20px;  
}

.equal-height .v-col {
  display: flex;
  flex-direction: column;
}

.my-content {
  margin-bottom: 10px; 
  margin-top: 10px;
}
</style>

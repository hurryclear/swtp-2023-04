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
                <div><span style="font-weight: bold; font-size: large;">{{ $t('reviewComponent.overview') }}:</span>
                </div>
              </v-col>
            </v-row>
          </v-card-title>

          <v-col cols="12">
            <!-- Card showing the application status -->
            <v-card :color="determineStatusColor(form.applicationData.status)">
              <v-card-text class="text-center" style="color: white;">
                <!-- Status information -->
                <span style="font-weight: bold;">{{ determineStatusTranslations(form.applicationData.status) }}</span>
              </v-card-text>
            </v-card>
          </v-col>

          <v-row>
            <!-- Displaying application details -->
            <v-col cols="12" sm="6">
              <v-card-text>
                <!-- Information fields for the application -->
                <div><span style="font-weight: bold;">{{ $t('reviewComponent.currentUniversity') }}:</span>
                  {{ form.applicationData.university }}
                </div>
                <div><span style="font-weight: bold;">{{ $t('reviewComponent.oldCourseOfStudy') }}:</span>
                  {{ form.applicationData.oldCourseOfStudy }}
                </div>
                <div><span style="font-weight: bold;">{{ $t('reviewComponent.newCourseOfStudy') }}:</span>
                  {{ form.applicationData.newCourseOfStudy }}
                </div>
                <div><span style="font-weight: bold;">{{ $t('reviewComponent.dateOfSubmission') }}:</span>
                  {{ new Date(form.applicationData.dateOfSubmission).toLocaleString() }}
                </div>
                <div><span style="font-weight: bold;">{{ $t('reviewComponent.dateLastEdited') }}:</span>
                  {{ new Date(form.applicationData.dateLastEdited).toLocaleString() }}
                </div>
                <div class="d-flex align-center"><span
                    style="font-weight: bold;">{{ $t('reviewComponent.formID') }}:</span>
                  {{ form.applicationData.applicationID }}
                  <v-btn size="x-small" color="primary" icon="mdi-content-copy" variant="plain"
                         @click="copyToClipboard"></v-btn>
                </div>
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
                      {{ $t('reviewComponent.moduleBlock') }} {{ moduleForm.frontend_key + 1 }}
                    </v-col>

                    <v-col cols="4">
                      <!-- Card displaying approval status of the module block -->
                      <v-card :color="getBlockApprovalColor(moduleForm.modulesStudent)">
                        <v-card-text class="text-center"><span style="font-weight: bold; color: white;"> {{
                            statusTranslation(getBlockApprovalStatus(moduleForm.modulesStudent))
                          }}</span></v-card-text>
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
                    <span style="font-weight: bold;">{{ $t('reviewComponent.moduleName') }}:</span> {{ module.title }} |
                    <span style="font-weight: bold;">{{ $t('reviewComponent.moduleNumber') }}:</span> {{
                      module.number
                    }} |
                    <span style="font-weight: bold;">{{ $t('reviewComponent.moduleCredits') }}:</span> {{
                      module.credits
                    }}
                  </div>

                  <!-- Module approval information -->
                  <div v-if="module.approval !== ''">
                    <span style="font-weight: bold;">{{ $t('reviewComponent.moduleApproval') }}: </span>
                    <span :style="{ color: determineTextColor(module.approval) }">{{
                        statusTranslation(module.approval)
                      }}</span>
                  </div>
                  <div v-else>
                    <span style="font-weight: bold;">{{ $t('reviewComponent.moduleApproval') }}: </span>
                    <span style="color: blue;">{{ $t('reviewComponent.moduleApprovalOpen') }}</span>
                  </div>

                  <!-- Reason for module approval or disapproval -->
                  <div v-if="module.reason !== ''">
                    <span style="font-weight: bold;">{{ $t('reviewComponent.moduleApprovalReason') }}: </span>
                    {{ module.reason }}
                  </div>
                </div>
                <v-divider class="my-divider"></v-divider>

                <!-- Displaying modules to be credited -->
                <div class="my-content" v-if="moduleForm.modules2bCredited && moduleForm.modules2bCredited.length">
                  <div>
                    <span style="font-weight: bold;">{{ $t('reviewComponent.modulesToBeCredited') }}:</span>
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
import {mapActions} from 'vuex';
import axios from '@/plugins/axios';


/**
 * Defines status colors and translations as key-value pairs.
 * @typedef {Object} StatusMappings
 * @property {string} color - The color associated with the status.
 * @property {string} translation - The translation key for the status.
 */
const statusMappings = {
  open: {
    color: 'blue',
    translation: 'open'
  },
  'editing in progress': {
    color: 'orange',
    translation: 'editingInProgress'
  },
  edited: {
    color: 'orange',
    translation: 'edited'
  },
  'ready for approval': {
    color: 'orange',
    translation: 'readyForApproval'
  },
  'approval in progress': {
    color: 'orange',
    translation: 'approvalInProgress'
  },
  'edited approval': {
    color: 'orange',
    translation: 'editedApproval'
  },
  'approval finished': {
    color: 'orange',
    translation: 'approvalFinished'
  },
  accepted: {
    color: 'green',
    translation: 'accepted'
  },
  rejected: {
    color: 'red',
    translation: 'rejected'
  },
  'formally rejected': {
    color: 'red',
    translation: 'formallyRejected'
  },
  'Error fetching form': {
    color: 'red',
    translation: 'errorFetchingForm'
  },
  'Form not found': {
    color: 'red',
    translation: 'formNotFound'
  },
  default: {
    color: 'grey',
    translation: 'pending'
  },
};


/**
 * Defines translations and colors objects for approval status.
 * @typedef {Object} StatusObjects
 * @property {string} accepted - The translated text for the 'accepted' status.
 * @property {string} rejected - The translated text for the 'rejected' status.
 * @property {string} formallyRejected - The translated text for the 'formally rejected' status.
 * @property {string} default - The default translated text.
 */
const approvalStatusMappings = {
  accepted: {
    translation:'accepted',
    color: 'green'
  },
  rejected: {
    translation:'rejected',
    color: 'red'
  },
  formallyRejected: {
    translation:'formallyRejected',
    color: 'red',
  },
  open:{
    translation:'open',
    color: 'blue',
  },
  partiallyAccepted:{
    translation: 'partiallyAccepted',
    color: 'orange'
  },
  partiallyRejected:{
    translation: 'partiallyRejected',
    color: 'orange'
  },
  default: {
    translation:'unknown',
    color: 'grey'
  },
};



export default {

  /**
   * Data properties of the Vue component.
   * @returns {Object} The initial data for the Vue component.
   */
  data() {
    return {
      form: null,
      isDataVisible: false,
      formId: '',
      status: null,
      statusMessage: '',
      statusColor: '',
    };
  },
  methods: {
    ...mapActions(['fetchApplicationSummary', 'fetchPdfSummary']),

    /**
     * Checks the status of an application.
     *
     * Asynchronous method to fetch the application summary and update the component's state.
     * If the application summary is fetched successfully, it sets isDataVisible to true
     * to display the application data. Otherwise, it sets isDataVisible to false.
     */
    async checkStatus() {
      try {
        await this.fetchApplicationSummary(this.formId);
        console.log('Form:', this.form);

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
     * If the PDF download fails, an error will be logged to the console.
     *
     * @async
     * @function downloadForm
     */
    async downloadForm() {
      try {
        const applicationId = this.form.applicationData.applicationID;
        const response = await axios.get("/api/student/getPdfSummary", {
          params: {applicationId},
          responseType: 'blob'
        });
        console.log('PDF response:', response);

        const url = window.URL.createObjectURL(new Blob([response.data], {type: 'application/pdf'}));
        const filename = `Application-${applicationId}.pdf`;
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', filename);

        document.body.appendChild(link);
        link.click();

        window.URL.revokeObjectURL(url);
        link.parentNode.removeChild(link);
      } catch (error) {
        console.error('Error downloading PDF:', error);
      }
    },

    /**
     * Determines the color associated with a given status.
     * @param {string} status - The current status of the item.
     * @returns {string} The color corresponding to the status.
     */
    determineStatusColor(status) {
      return statusMappings[status]?.color || statusMappings.default.color;
    },

    /**
     * Retrieves the translation for a given status.
     * @param {string} status - The current status of the item.
     * @returns {string} The translated status text.
     */
    determineStatusTranslations(status) {
      return this.$t(`reviewComponent.${statusMappings[status]?.translation || statusMappings.default.translation}`);
    },

    /**
     * Gets the translation of an approval status.
     * @param {string} approval - The approval status.
     * @returns {string} The translated text of the approval status.
     */
    statusTranslation(approval) {
      return this.$t(`reviewComponent.${approvalStatusMappings[approval]?.translation || approvalStatusMappings.default.translation}`);
    },

    /**
     * Gets the overall approval status of a block based on its modules.
     * @param {Array} modules - Array of modules, each with an approval status.
     * @returns {string} The overall approval status.
     */
    getBlockApprovalStatus(modules) {
      const approvalCounts = modules.reduce((counts, module) => {
        if (module.approval === 'accepted') counts.accepted++;
        else if (module.approval === 'rejected') counts.rejected++;
        else if (module.approval === 'formally rejected') counts.formallyRejected++;
        else counts.open++;
        return counts;
      }, {accepted: 0, rejected: 0, formallyRejected: 0, open: 0});

      if (approvalCounts.accepted === modules.length) return 'accepted';
      if (approvalCounts.rejected === modules.length) return 'rejected';
      if (approvalCounts.formallyRejected === modules.length) return 'formallyRejected';
      if (approvalCounts.accepted > 0) return 'partiallyAccepted';
      if (approvalCounts.rejected > 0) return 'partiallyRejected';
      if (approvalCounts.open === modules.length) return 'open';

      return 'rejected'; // Default to 'rejected' if none of the conditions match
    },

    /**
     * Determines the color representing the overall approval status of a block.
     * @param {Array} modules - Array of modules, each with an approval status.
     * @returns {string} The color representing the overall approval status.
     */
    getBlockApprovalColor(modules) {
      const approvalStatus = this.getBlockApprovalStatus(modules);
      return approvalStatusMappings[approvalStatus]?.color || approvalStatusMappings.default.color;
    },

    /**
     * Determines the text color based on the approval status.
     * @param {string} approval - The approval status.
     * @returns {string} The color representing the text based on approval status.
     */
    determineTextColor(approval) {
      return approvalStatusMappings[approval]?.color || approvalStatusMappings.default.color;
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
  },
  props: {
    applicationId: String
  },
  watch: {
    '$store.state.form.currentForm': {
      immediate: true,
      handler(newVal) {
        this.form = newVal;
      }
    },
    applicationId(newVal) {
      this.formId = newVal;
      console.log('Updated Form ID:', this.formId);
      if (this.formId) {
        this.checkStatus();
      }
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

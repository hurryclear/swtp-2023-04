<template>
  <v-container>
    <v-col>
      <v-card>
        <v-card-title>{{ $t('reviewComponent.reviewApplication') }}</v-card-title>
        <v-card-text>
          <v-text-field
              :label="$t('reviewComponent.formID')"
              v-model="formId"
              @keyup.enter="checkStatus"
              outlined
              dense
          />
          <v-btn @click="checkStatus" color="primary">{{ $t('reviewComponent.checkStatus') }}</v-btn>
        </v-card-text>
      </v-card>
    </v-col>

    <v-col v-if="form">
      <v-alert :color="statusColor">
        <div>
          {{ statusMessage }}
        </div>
        <v-btn @click="downloadForm" append-icon="mdi-download">{{ $t('reviewComponent.downloadApplication') }}</v-btn>
      </v-alert>
    </v-col>

    <v-col v-else-if="status">
      <v-alert type="info">
        {{ status }}
      </v-alert>
    </v-col>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      formId: '',
      form: null,
      status: null,
      statusMessage: '',
      statusColor: 'info',
    };
  },
  methods: {
    checkStatus() {
      const foundForm = this.$store.getters.form.formsByStatus('all').find(f => f.id === this.formId);
      if (foundForm) {
        this.form = foundForm;
        // TODO: i18n + mapping status to i18n translations.
        this.statusMessage = `Status: ${foundForm.status}`;
        this.statusColor = this.determineStatusColor(foundForm.status);
      } else {
        this.form = null;
        this.status = 'Form not found';
      }
    },
    determineStatusColor(status) {
      switch (status) {
        case 'open':
          return 'blue';
        case 'in progress':
          return 'orange';
        case 'accepted':
          return 'green';
        case 'denied':
          return 'red';
        default:
          return 'grey';
      }
    },

    downloadForm() {
      if (!this.form) return;

      const blob = new Blob([JSON.stringify(this.form, null, 2)], {
        type: "application/json",
      });

      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = `form-${this.form.id}.json`;
      link.click();

      URL.revokeObjectURL(link.href); // Clean up
    }
  }

};
</script>

<style scoped>
.mt-2 {
  margin-top: 8px;
}
</style>

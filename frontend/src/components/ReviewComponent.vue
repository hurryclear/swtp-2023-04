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
              variant="outlined"
              dense
          />
          <v-btn @click="checkStatus" color="primary">{{ $t('reviewComponent.checkStatus') }}</v-btn>
        </v-card-text>
      </v-card>
    </v-col>

    <v-col v-if="form">
      <v-alert :color="statusColor" class="mb-4">
        <div>
          {{ statusMessage }}
        </div>
        <v-btn @click="downloadForm" append-icon="mdi-download">{{ $t('reviewComponent.downloadApplication') }}</v-btn>
      </v-alert>

      <!-- TODO: i18n translations -->

      <v-card class="mb-4">
        <v-card-title><span style="font-weight: bold;">{{ form.applicationData.universityName }}</span></v-card-title>
        <v-card-text>
          <div><span style="font-weight: bold;">Major:</span> {{ form.applicationData.major }}</div>
          <div><span style="font-weight: bold;">Land:</span> {{ form.applicationData.country }}</div>
          <div><span style="font-weight: bold;">Status:</span> {{ form.applicationData.status }}</div>
          <div><span style="font-weight: bold;">Kommentar Student:</span> {{ form.applicationData.commentStudent }}</div>
          <div><span style="font-weight: bold;">Kommentar Mitarbeiter:</span> {{ form.applicationData.commentEmployee }}</div>
          <div><span style="font-weight: bold;">Datum der Einreichung:</span> {{ new Date(form.applicationData.dateOfSubmission).toLocaleString() }}</div>
          <div><span style="font-weight: bold;">Letzte Bearbeitung:</span> {{ new Date(form.applicationData.dateLastEdited).toLocaleString() }}</div>
        </v-card-text>
      </v-card>

      <div v-for="moduleForm in form.moduleFormsData" :key="moduleForm.moduleBlockData.key">
        <v-card class="mb-4">
          <v-card-title><span style="font-weight: bold;">Modul Block</span> {{ moduleForm.moduleBlockData.key }}</v-card-title>
          <v-card-text>
            <div><span style="font-weight: bold;">Genehmigung:</span> {{ moduleForm.moduleBlockData.approval }}</div>
            <div><span style="font-weight: bold;">Kommentar Student:</span> {{ moduleForm.moduleBlockData.commentStudent }}</div>
            <div><span style="font-weight: bold;">Kommentar Mitarbeiter:</span> {{ moduleForm.moduleBlockData.commentEmployee }}</div>

            <div v-for="module in moduleForm.modulesStudent" :key="module.number">
              <div><span style="font-weight: bold;">Modulnummer:</span> {{ module.number }}</div>
              <div><span style="font-weight: bold;">Titel:</span> {{ module.title }}</div>
              <div><span style="font-weight: bold;">Beschreibung:</span> {{ module.description }}</div>
              <div><span style="font-weight: bold;">Credits:</span> {{ module.credits }}</div>
              <div><span style="font-weight: bold;">Universität:</span> {{ module.university }}</div>
              <div><span style="font-weight: bold;">Studiengang:</span> {{ module.major }}</div>
              <div><span style="font-weight: bold;">Kommentar Student:</span> {{ module.commentStudent }}</div>
              <div><span style="font-weight: bold;">Kommentar Mitarbeiter:</span> {{ module.commentEmployee }}</div>
            </div>

            <div v-if="moduleForm.modules2bCredited && moduleForm.modules2bCredited.length">
              <div><span style="font-weight: bold;">Anzurechnende Module:</span></div>
              <ul class="indented-list">
                <li v-for="moduleToCredit in moduleForm.modules2bCredited" :key="moduleToCredit">{{ moduleToCredit }}</li>
              </ul>
            </div>
          </v-card-text>
        </v-card>
      </div>
    </v-col>

    <v-col v-else-if="status">
      <v-alert type="info" :color="statusColor">
        {{ status }}
      </v-alert>
    </v-col>
  </v-container>
</template>

<script>
import { mapActions } from 'vuex';


export default {
  data() {
    return {
      formId: '',
      form: {
        "applicationData":
          {
            "universityName": "University of Regenbogenland",
            "major": "B. Sc. Informatik",
            "country": "Regenbogencountry",
            "status": "",
            "commentStudent": "Ich will alle CP valla",
            "commentEmployee": "Der bekommt nicht alle CP yalla",
            "dateOfSubmission": "2023-12-31T22:30:42.103Z",
            "dateLastEdited": "2024-01-14T14:12:14.675Z"
          },
        "moduleFormsData": [
          {
            "moduleBlockData":
              {
                "key": 0,
                "approval": "",
                "commentStudent": "Das ist mein Block",
                "commentEmployee": "Das ist sein Block"
              },
            "modulesStudent": [
              {
                "number": "420",
                "title": "AlgoDat 1.5",
                "description": "",
                "credits": "5",
                "university": "University of Regenbogenland",
                "major": "B. Sc. Informatik",
                "commentStudent": "War cool",
                "commentEmployee": "Das nicht so cool"
              },
              {
                "number": "69",
                "title": "AlgoDat 0.5",
                "description": "",
                "credits": "4",
                "university": "University of Regenbogenland",
                "major": "B. Sc. Informatik",
                "commentStudent": "War nicht so cool",
                "commentEmployee": "Da hat er mal recht"
              }
            ],
            "modules2bCredited": [
              "Algorithmen und Datenstrukturen 1",
              "Algorithmen und Datenstrukturen 2"
            ]
          },
          {
            "moduleBlockData":
              {
                "key": 1,
                "approval": "",
                "commentStudent": "",
                "commentEmployee": ""
              }
            ,
            "modulesStudent": [
              {
                "number": "81923",
                "title": "Das Alles-Modul",
                "description": "",
                "credits": "20",
                "university": "University of Regenbogenland",
                "major": "B. Sc. Informatik",
                "commentStudent": "Die Beschreibung ist lang, setzen Sie schon mal Kaffee auf",
                "commentEmployee": "Beschreibung war wirklich lang, Kaffee ist alle"
              }
            ],
            "modules2bCredited": [
              "Lineare Algebra für Informatiker",
              "Analysis für Informatiker"
            ]
          }
        ]
      },
      status: null,
      statusMessage: '',
      statusColor: '',
    };
  },
  methods: {
  ...mapActions(['fetchApplicationSummary', 'fetchPdfSummary']),

    async checkStatus() {
      // try {
      //   const form = await this.fetchApplicationSummary(this.formId);
      //   if (form) {
      //     this.form = form;
      //     this.statusMessage = `Status: ${form.status}`;
      //     this.statusColor = this.determineStatusColor(form.status);
      //   } else {
      //     this.form = null;
      //     this.status = 'Form not found';
      //     this.statusColor = this.determineStatusColor(this.status);
      //   }
      // } catch (error) {
      //   console.error('Error in checkStatus:', error);
      //   this.status = 'Error fetching form';
      //   console.log('status geaendert zu' ,this.status);
      //   this.statusColor = this.determineStatusColor(this.status);
      //}
        
        this.statusMessage = `Status: ${this.form.applicationData.status}`;
        this.statusColor = this.determineStatusColor(this.form.applicationData.status);
      },
    

    async downloadForm() {
      if (!this.form) return;

      try {
        const pdfData = await this.fetchPdfSummary(this.form.id);
        const blob = new Blob([pdfData], { type: 'application/pdf' });

        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = `application-${this.form.id}.pdf`;
        link.click();

        URL.revokeObjectURL(link.href); // Clean up
      } catch (error) {
        console.error('Error downloading PDF:', error);
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
        case 'Error fetching form':
          return 'red';
        case 'Form not found':
          return 'red';
        default:
          return 'grey';
      }
    },
  },
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

.divider-custom {
  margin-top: 16px;
  margin-bottom: 16px;
}

.indented-list {
  padding-left: 20px;  /* Oder einen anderen Wert nach Wahl */
}
</style>

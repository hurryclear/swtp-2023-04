<template>
  <v-container>
    <!-- Formular-Überprüfungsbereich -->
    <v-col>
      <v-card>
        <v-card-title :style="{fontWeight: 'bold'}">{{ $t('reviewComponent.reviewApplication') }}</v-card-title>
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

    <!-- Angezeigte Datenbereich -->
    <v-col v-if="editedForm">
      <v-alert class="text-center mb-4" :color="statusColor">
        <div class="text-center" :style="{ fontSize: '24px', fontWeight: 'bold' }">
          {{ statusMessage }}
        </div>
        <v-btn class="text-center" @click="downloadForm" append-icon="mdi-download">
          {{ $t('reviewComponent.downloadApplication') }}
        </v-btn>
      </v-alert>
    
      <div class="text-center mb-4" :style="{ fontSize: '24px', fontWeight: 'bold' }">
        Antrag: {{ editedForm.applicationData.applicationID }}
      </div>
    

      <!-- Tabs für den Wechsel zwischen Original und Bearbeitet -->
    
      <v-row>
        <v-col>
          <v-tabs v-model="tab" background-color="primary" dark>
            <v-tab>Original</v-tab>
            <v-tab>Bearbeitet</v-tab>
          </v-tabs>
          <v-window v-model="tab">
            <v-window-item>
              <!-- Hier kommt der Inhalt des Originalformulars -->

              <v-card class="mb-4">
                <v-card-title><span style="font-weight: bold;">{{ originalForm.applicationData.universityName }}</span></v-card-title>
                <v-card-text>
                  <div><span style="font-weight: bold;">Major:</span> {{ originalForm.applicationData.applicationID }}</div>
                  <div><span style="font-weight: bold;">Land:</span> {{ originalForm.applicationData.country }}</div>
                  <div><span style="font-weight: bold;">Status:</span> {{ originalForm.applicationData.status }}</div>
                  <div><span style="font-weight: bold;">Kommentar Student:</span> {{ originalForm.applicationData.commentStudent }}</div>
                  <div><span style="font-weight: bold;">Kommentar Mitarbeiter:</span> {{ originalForm.applicationData.commentEmployee }}</div>
                  <div><span style="font-weight: bold;">Datum der Einreichung:</span> {{ new Date(originalForm.applicationData.dateOfSubmission).toLocaleString() }}</div>
                  <div><span style="font-weight: bold;">Letzte Bearbeitung:</span> {{ new Date(originalForm.applicationData.dateLastEdited).toLocaleString() }}</div>
                </v-card-text>
              </v-card>

              <!-- Module -->
              <div v-for="moduleForm in originalForm.moduleFormsData" :key="moduleForm.backend_block_id">
                <v-card class="mb-4">
                  <v-card-title><span style="font-weight: bold;">Modul Block</span> {{ moduleForm.backend_block_id }}</v-card-title>
                  <v-card-text>
                    <div v-for="module in moduleForm.modulesStudent" :key="module.backend_module_id">
                      <div><span style="font-weight: bold;">Modulnummer:</span> {{ module.number }}</div>
                      <div><span style="font-weight: bold;">Titel:</span> {{ module.title }}</div>
                      <div><span style="font-weight: bold;">Beschreibung:</span> {{ module.description }}</div>
                      <div><span style="font-weight: bold;">Credits:</span> {{ module.credits }}</div>
                      <div><span style="font-weight: bold;">Universität:</span> {{ module.university }}</div>
                      <div><span style="font-weight: bold;">Studiengang:</span> {{ module.major }}</div>
                      <div><span style="font-weight: bold;">Kommentar Student:</span> {{ module.commentStudent }}</div>
                      <div><span style="font-weight: bold;">Kommentar Mitarbeiter:</span> {{ module.commentEmployee }}</div>
                    </div>

                    <!-- Anzurechnende Module -->
                    <div v-if="moduleForm.modules2bCredited && moduleForm.modules2bCredited.length">
                      <div><span style="font-weight: bold;">Anzurechnende Module:</span></div>
                      <ul class="indented-list">
                        <li v-for="moduleToCredit in moduleForm.modules2bCredited" :key="moduleToCredit">{{ moduleToCredit }}</li>
                      </ul>
                    </div>
                  </v-card-text>
                </v-card>
              </div>
            </v-window-item>

            <v-window-item>
              <!-- Hier kommt der Inhalt des bearbeiteten Formulars -->
              <v-card class="mb-4">
                <v-card-title><span style="font-weight: bold;">{{ editedForm.applicationData.universityName }}</span></v-card-title>
                <v-card-text>
                  <div><span style="font-weight: bold;">Major:</span> {{ editedForm.applicationData.applicationID }}</div>
                  <div><span style="font-weight: bold;">Land:</span> {{ editedForm.applicationData.country }}</div>
                  <div><span style="font-weight: bold;">Status:</span> {{ editedForm.applicationData.status }}</div>
                  <div><span style="font-weight: bold;">Kommentar Student:</span> {{ editedForm.applicationData.commentStudent }}</div>
                  <div><span style="font-weight: bold;">Kommentar Mitarbeiter:</span> {{ editedForm.applicationData.commentEmployee }}</div>
                  <div><span style="font-weight: bold;">Datum der Einreichung:</span> {{ new Date(editedForm.applicationData.dateOfSubmission).toLocaleString() }}</div>
                  <div><span style="font-weight: bold;">Letzte Bearbeitung:</span> {{ new Date(editedForm.applicationData.dateLastEdited).toLocaleString() }}</div>
                </v-card-text>
              </v-card>

              <!-- Module -->
              <div v-for="moduleForm in editedForm.moduleFormsData" :key="moduleForm.backend_block_id">
                <v-card class="mb-4">
                  <v-card-title><span style="font-weight: bold;">Modul Block</span> {{ moduleForm.backend_block_id }}</v-card-title>
                  <v-card-text>
                    <div v-for="module in moduleForm.modulesStudent" :key="module.backend_module_id">
                      <div><span style="font-weight: bold;">Modulnummer:</span> {{ module.number }}</div>
                      <div><span style="font-weight: bold;">Titel:</span> {{ module.title }}</div>
                      <div><span style="font-weight: bold;">Beschreibung:</span> {{ module.description }}</div>
                      <div><span style="font-weight: bold;">Credits:</span> {{ module.credits }}</div>
                      <div><span style="font-weight: bold;">Universität:</span> {{ module.university }}</div>
                      <div><span style="font-weight: bold;">Studiengang:</span> {{ module.major }}</div>
                      <div><span style="font-weight: bold;">Kommentar Student:</span> {{ module.commentStudent }}</div>
                      <div><span style="font-weight: bold;">Kommentar Mitarbeiter:</span> {{ module.commentEmployee }}</div>
                    </div>

                    <!-- Anzurechnende Module -->
                    <div v-if="moduleForm.modules2bCredited && moduleForm.modules2bCredited.length">
                      <div><span style="font-weight: bold;">Anzurechnende Module:</span></div>
                      <ul class="indented-list">
                        <li v-for="moduleToCredit in moduleForm.modules2bCredited" :key="moduleToCredit">{{ moduleToCredit }}</li>
                      </ul>
                    </div>
                  </v-card-text>
                </v-card>
              </div>
            </v-window-item>
          </v-window>
        </v-col>
      </v-row>
    </v-col>

    <!-- Statusmeldungsbereich -->
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

      //hardcoded form, plese remove after backend is implemented
      form: {
          // das senden und empfangen Backend UND Frontend
          
            "original": {
              "applicationData": {
                "applicationID": "23-23-23-23-23-23-23-23-23-23-23 test",
                "status": "",
                //offen, in bearbeitung, formal abgelehnt,     aber nicht abgelehnt, angenommen
                "formalReject": "JO du hast das völlig falsch gemacht mach den antrag nochmal",
                "dateOfSubmission": "2024-03-05T13:56:51.560Z",
                "dateLastEdited": "2024-03-05T13:56:51.560Z",
                "university": "University of Regenbogenland",
                "oldCourseOfStudy": "B.Sc Informatik",       //alter studiengang default
                "newCourseOfStudy": "B.Sc Informatik"   //Studiengang Uni leipzig kann nicht verändert werden!!
              },
              "moduleFormsData": [
                {
                  "frontend_key": 0,
                  "backend_block_id": 1,
                  // backend Id zur identifikation des blocks
                  "modulesStudent": [
                    {
                      "frontend_key": 0,
                      // frontend key eines Moduls in einem block, hier ist es das 1. Modul des 1. Blocks
                      "backend_module_id": 3,
                      // backend id des student moduls zur eindeutigen identifikation
                      "approval": "angenommen",
                      "reason": "ja das kann man so machen passt alles soweit.",
                      "number": "420",
                      "title": "AlgoDat 1.5",
                      "path": "/23-23-23-23-23-23-23-23-23-23-23/S-3",
                      "credits": "5",
                      "university": "University of Regenbogenland",
                      "major": "B.Sc. Informatik",
                      "commentStudent": "War cool",
                      "commentEmployee": "Das nicht so cool"
                    },
                    {
                      "frontend_key": 1,
                      // frontend key eines Moduls in einem block, hier ist es das 1. Modul des 1. Blocks
                      "backend_module_id": 5,
                      // backend id des student moduls zur eindeutigen identifikation
                      "approval": "angenommen",
                      "reason": "ja das kann man so machen passt alles soweit.",
                      "number": "4202",
                      "title": "AlgoDat 0.5",
                      "path": "/23-23-23-23-23-23-23-23-23-23-23/S-5",
                      "credits": "5",
                      "university": "University of Regenbogenland",
                      "major": "B.Sc. Informatik",
                      "commentStudent": "War easy",
                      "commentEmployee": "Das nicht so cool"
                    }
                  ],
                  "modules2bCredited": [
                    1,
                    //id vom Uni Modul Alg 1
                    2
                    //id vom Uni Modul Alg 2
                  ]
                },
                {
                  "frontend_key": 1,
                  "backend_block_id": 3,
                  // backend Id zur identifikation des blocks
                  "modulesStudent": [
                    {
                      "frontend_key": 0,
                      // frontend key eines Moduls in einem block, hier ist es das 1. Modul des 1. Blocks
                      "backend_module_id": 5,
                      // backend id des student moduls zur eindeutigen identifikation
                      "approval": "angenommen",
                      "reason": "ja das kann man so machen passt alles soweit.",
                      "number": "81923",
                      "title": "Das Alles-Modul",
                      "path": "/23-23-23-23-23-23-23-23-23-23-23/S-5",
                      "credits": "20",
                      "university": "University of Regenbogenland",
                      "major": "B. Sc. Informatik",
                      "commentStudent": "Die Beschreibung ist lang, setzen Sie schon mal Kaffee auf",
                      "commentEmployee": "Beschreibung war wirklich lang, Kaffee ist alle"
                    }
                  ],
                  "modules2bCredited": [
                    5,
                    //id vom Uni Modul Alg 1
                    8
                    //id vom Uni Modul Alg 2
                  ]
                }
              ]
            },
            "edited": {
              "applicationData": {
                "applicationID": "23-23-23-23-23-23-23-23-23-23-23",
                "status": "open",
                //offen, in bearbeitung, formal abgelehnt,     aber nicht abgelehnt, angenommen
                "formalReject": "JO du hast das völlig falsch gemacht mach den antrag nochmal",
                "dateOfSubmission": "2024-03-05T13:56:51.560Z",
                "dateLastEdited": "2024-03-05T13:56:51.560Z",
                "university": "University of Regenbogenland",
                "oldCourseOfStudy": "B.Sc Informatik",       //alter studiengang default
                "newCourseOfStudy": "B.Sc Informatik"   //Studiengang Uni leipzig kann nicht verändert werden!!
              },
              "moduleFormsData": [
                {
                  "frontend_key": 0,
                  "backend_block_id": 1,
                  // backend Id zur identifikation des blocks
                  "modulesStudent": [
                    {
                      "frontend_key": 0,
                      // frontend key eines Moduls in einem block, hier ist es das 1. Modul des 1. Blocks
                      "backend_module_id": 3,
                      // backend id des student moduls zur eindeutigen identifikation
                      "approval": "angenommen",
                      "reason": "ja das kann man so machen passt alles soweit.",
                      "number": "420",
                      "title": "AlgoDat 1.5",
                      "path": "/23-23-23-23-23-23-23-23-23-23-23/S-3",
                      "credits": "5",
                      "university": "University of Regenbogenland",
                      "major": "B.Sc. Informatik",
                      "commentStudent": "War cool",
                      "commentEmployee": "Das nicht so cool"
                    },
                    {
                      "frontend_key": 1,
                      // frontend key eines Moduls in einem block, hier ist es das 1. Modul des 1. Blocks
                      "backend_module_id": 5,
                      // backend id des student moduls zur eindeutigen identifikation
                      "approval": "angenommen",
                      "reason": "ja das kann man so machen passt alles soweit.",
                      "number": "4202",
                      "title": "AlgoDat 0.5",
                      "path": "/23-23-23-23-23-23-23-23-23-23-23/S-5",
                      "credits": "5",
                      "university": "University of Regenbogenland",
                      "major": "B.Sc. Informatik",
                      "commentStudent": "War easy",
                      "commentEmployee": "Das nicht so cool"
                    }
                  ],
                  "modules2bCredited": [
                    1,
                    //id vom Uni Modul Alg 1
                    2
                    //id vom Uni Modul Alg 2
                  ]
                },
                {
                  "frontend_key": 1,
                  "backend_block_id": 3,
                  // backend Id zur identifikation des blocks
                  "modulesStudent": [
                    {
                      "frontend_key": 0,
                      // frontend key eines Moduls in einem block, hier ist es das 1. Modul des 1. Blocks
                      "backend_module_id": 5,
                      // backend id des student moduls zur eindeutigen identifikation
                      "approval": "angenommen",
                      "reason": "ja das kann man so machen passt alles soweit.",
                      "number": "81923",
                      "title": "Das Alles-Modul",
                      "path": "/23-23-23-23-23-23-23-23-23-23-23/S-5",
                      "credits": "20",
                      "university": "University of Regenbogenland",
                      "major": "B. Sc. Informatik",
                      "commentStudent": "Die Beschreibung ist lang, setzen Sie schon mal Kaffee auf",
                      "commentEmployee": "Beschreibung war wirklich lang, Kaffee ist alle"
                    }
                  ],
                  "modules2bCredited": [
                    5,
                    //id vom Uni Modul Alg 1
                    8
                    //id vom Uni Modul Alg 2
                  ]
                }
              ]
            }
          },
      editedForm: null,
      originalForm: null,
      status: null,
      statusMessage: '',
      statusColor: '',
      tab: 0,
    };
  },
  methods: {
    ...mapActions(['fetchApplicationSummary', 'fetchPdfSummary']),

    async checkStatus() {
      //uncomment after backend is implemented
      
      // try {
      //   const form = await this.fetchApplicationSummary(this.formId);
      //   if (form && form.edited) {
      //     this.editedForm = form.edited;
      //     this.statusMessage = `Status: ${form.edited.applicationData.status}`;
      //     this.statusColor = this.determineStatusColor(form.edited.applicationData.status);
      //   } else {
      //     this.editedForm = null;
      //     this.status = 'Form not found';
      //     this.statusColor = this.determineStatusColor(this.status);
      //   }
      // } catch (error) {
      //   console.error('Error in checkStatus:', error);
      //   this.status = 'Error fetching form';
      //   this.statusColor = this.determineStatusColor(this.status);
      // }

      //remove after backend is implemented just for testing the hardcoded form
      
      this.editedForm = this.form.edited;
      this.originalForm = this.form.original;
      if(this.editedForm.applicationData.applicationID === this.formId){
        this.statusMessage = `Status: ${this.editedForm.applicationData.status}`;
        this.statusColor = this.determineStatusColor(this.editedForm.applicationData.status);
      } else {
        this.editedForm = null;
        this.status = 'Form not found';
        this.statusColor = this.determineStatusColor(this.status);
      }

    },

    async downloadForm() {
      if (!this.editedForm) return;

      try {
        const pdfData = await this.fetchPdfSummary(this.editedForm.id);
        const blob = new Blob([pdfData], { type: 'application/pdf' });

        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = `application-${this.editedForm.id}.pdf`;
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

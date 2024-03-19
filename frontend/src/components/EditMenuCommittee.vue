<template>
  <v-card>
    <div class="card-header">
      <v-row>
        <v-col cols="6" md="4">
          <v-card-title>
            <u>{{ $t('studentAffairsOfficeView.application') }}</u>
          </v-card-title>
        </v-col>
    
        <v-col cols="6" md="4" v-if="formCopy.edited.applicationData.status === 'open' || formCopy.edited.applicationData.status === 'edited'">
            <v-btn
          variant="elevated"
          class="ma-2"
          prepend-icon="mdi-content-save"
          :loading="loadingSaveButton"
          @click="sendToExaminingCommitteeChair()"
          color="orange"
          >
            {{  $t('studentAffairsOfficeView.makeApprovable') }}
            </v-btn>
        </v-col>

        <v-col cols="6" md="4" v-else>
          <v-btn-toggle class="ma-2" v-model="showEdited" mandatory shaped variant="outlined">
            <v-btn :value="false">
              {{ $t('studentAffairsOfficeView.original') }}
            </v-btn>
            <v-btn :value="true">
              {{ $t('studentAffairsOfficeView.edited') }}
            </v-btn>
          </v-btn-toggle>
        </v-col>

        <v-col cols="3" md="1">
          <v-btn class="ma-2" icon="mdi-call-split" @click="this.$emit('open', { component: 'SplitComponent', form: this.formCopy })" v-show="showEdited" >
            <v-icon>mdi-call-split</v-icon>
            <v-tooltip activator="parent" location="bottom">{{ $t("studentAffairsOfficeView.split") }} </v-tooltip>
          </v-btn>
        </v-col>

        <v-col cols="3" md="1">
          <v-btn class="ma-2" icon="mdi-call-merge" @click="this.$emit('open', { component: 'MergeComponent', form: this.formCopy })" v-show="showEdited">
            <v-icon>mdi-call-merge</v-icon>
            <v-tooltip activator="parent" location="bottom">{{ $t('studentAffairsOfficeView.merge') }} </v-tooltip>
          </v-btn>
        </v-col>

        <v-col cols="3" md="1">
          <v-btn class="ma-2" icon="mdi-file-compare" @click="this.$emit('open',{component:'ComparisonMenu',formCopy:{}});">
            <v-icon>mdi-file-compare</v-icon>
            <v-tooltip activator="parent" location="bottom"> {{ $t('studentAffairsOfficeView.compareWithOtherApplications') }} </v-tooltip>
          </v-btn>
  
        </v-col>
        <v-col cols="3" md="1">
          <v-btn class="ma-2" icon="mdi-close" @click="this.$emit('close')"/>
        </v-col>
      </v-row>
    </div>

    <divider/>

    <v-tabs v-if="formCopy" v-model="selectedTabIndex">
      <v-tab
          v-for="(moduleMapping,i) in (showEdited ? formCopy.edited.moduleFormsData : formCopy.original.moduleFormsData)"
          :key="i"
          :value="i"
      >
        {{ $t('studentAffairsOfficeView.mapping') }} {{ i + 1 }}
      </v-tab>
    </v-tabs>
    <div class="pa-2" v-if="formCopy">
      <v-window v-model="selectedTabIndex">
        <v-window-item
            v-for="(moduleMapping,i) in showEdited ? formCopy.edited.moduleFormsData : formCopy.original.moduleFormsData"
            :key="i"
            :value="i"
        >
          <v-card-text>
            <u>{{ $t('studentAffairsOfficeView.previousUniversity') }}:</u> {{
              formCopy.original.applicationData.university
            }}
            <br/>
            <u>{{ $t('studentAffairsOfficeView.previousCourse') }}:</u> {{
              formCopy.original.applicationData.oldCourseOfStudy
            }}
            <br/>
            <u>{{ $t('studentAffairsOfficeView.currentCourse') }}:</u> {{
              formCopy.original.applicationData.newCourseOfStudy
            }}
          </v-card-text>

          <!--Modules-->
          <v-card-title>
            {{ $t('studentAffairsOfficeView.modules') }}:
          </v-card-title>
          <div v-for="(module, j) in moduleMapping.modulesStudent" :key="module.frontend_key">
            <v-card-subtitle>
              {{ $t('studentAffairsOfficeView.module') }} {{ j + 1 }}
            </v-card-subtitle>
            <v-text-field
                variant="outlined"
                :disabled="!showEdited"
                :label="$t('studentAffairsOfficeView.name')"
                v-model="module.title"
            />
            <v-text-field
                variant="outlined"
                :disabled="!showEdited"
                :label="$t('studentAffairsOfficeView.moduleNumber')"
                v-model="module.number"
            />
            <v-text-field
                variant="outlined"
                :disabled="!showEdited"
                :label="$t('studentAffairsOfficeView.credits')"
                v-model="module.credits"
            />
            <div>
              <u>{{$t('studentAffairsOfficeView.studentComment')}}:</u> {{ module.commentStudent }}
              <br>
              <u>{{$t('studentAffairsOfficeView.officeComment')}}:</u> {{ module.commentEmployee }}
            </div>
              <br>    
            <v-btn class="ma-2" @click="downloadPdf(module.path, module.title)">
              {{ $t('studentAffairsOfficeView.downloadDescription') }}
            </v-btn>

            <div v-if="module.approval === 'formally rejected'">
              <v-card-text>
                <div><span style="font-weight: bold;">{{ $t('reviewComponent.formallyRejected') }}</span></div>
                <br/>
                <div><span style="font-weight: bold;">{{ $t('reviewComponent.moduleApprovalReason') }}:</span>{{ module.reason }}</div>
              </v-card-text>
            </div>

            <div v-else-if="showEdited">
              <v-text-field
                variant="outlined"
                :label="$t('studentAffairsOfficeView.reasonForDesicion')"
                v-model="module.reason"/>
            </div>
            
            <v-row v-if="showEdited && module.approval !== 'formally rejected'">
              <v-btn
                  @click="module.approval = 'rejected'"
                  :disabled="module.reason === ''"
                  class="ma-2"
                  prepend-icon="mdi-hand-back-left"
                  variant="elevated"
                  color="red"
              >{{ $t('examiningCommitteeChairView.decline') }}
              </v-btn>
              <v-btn
                  @click="module.approval = 'edited'"
                  v-if="formCopy && module.approval === 'rejected'"
                  class="ma-2"
                  prepend-icon="mdi-keyboard-backspace"
                  variant="elevated"
                  color="yellow"
              >{{ $t('examiningCommitteeChairView.undoDecline') }}
              </v-btn>
              <v-btn
                  @click="module.approval = 'accepted'"
                  :disabled="module.reason === ''"
                  class="ma-2"
                  prepend-icon="mdi-check-bold"
                  variant="elevated"
                  color="green"
              >{{ $t('examiningCommitteeChairView.accept') }}
              </v-btn>
              <v-btn
                  @click="module.approval = 'edited'"
                  v-if="formCopy && module.approval === 'accepted'"
                  class="ma-2"
                  prepend-icon="mdi-keyboard-backspace"
                  variant="elevated"
                  color="yellow"
              >{{ $t('examiningCommitteeChairView.undoAccept') }}
              </v-btn>
            </v-row>
            <divider/>
          </div>
          <v-card-text>
            <v-select
                multiple
                variant="outlined"
                class="text-field"
                :items="majorModules"
                item-title="name"
                item-value="id"
                :label="$t('studentAffairsOfficeView.creditFor')"
                :disabled="!showEdited"
                v-model="moduleMapping.modules2bCredited"
            />
          </v-card-text>
          <divider/>
        </v-window-item>
      </v-window>
      <divider/>
      <v-card-actions>
        <div v-if="showEdited">
            <v-btn
          variant="elevated"
          class="ma-2"
          prepend-icon="mdi-content-save"
          :loading="loadingSaveButton"
          @click="saveApprovedForm(false)"
          :color="allModulesReviewed ? 'blue' : 'green'"
          >
            {{ allModulesReviewed ? $t('studentAffairsOfficeView.finishApproval') : $t('studentAffairsOfficeView.save') }}
          </v-btn>
        </div>
      </v-card-actions>
    </div>
  </v-card>
</template>

<script>
import StudentAffairsOfficeService from "@/services/StudentAffairsOfficeService";

export default {
  props: {
    form: JSON
  },

  async created() {
    await this.getModules();
    this.formCopy = structuredClone(this.form);
  },

  data() {
    return {
      formalRejectionReason: '',
      showCommentWarning: false,
      loadingSaveButton: false,
      loadingSendButton: false,
      showEdited: false,
      selectedTabIndex: 0,
      formCopy: null
    }
  },
  computed: {
    allModulesReviewed() {
      return this.formCopy.edited.moduleFormsData.every(mapping =>
          mapping.modulesStudent.every(module =>
              ['accepted', 'rejected', 'formally rejected'].includes(module.approval)
          )
      );
    },

    majorModules() {
      return this.$store.state.studentAffairsOffice.majorModules;
    },
  },

  watch:{
    form: {
      handler(newVal){
        this.formCopy = structuredClone(newVal);
      },
      deep: true
    }
  },


  methods: {
    async getModules() {
      try {
        this.$store.state.studentAffairsOffice.majorModules= await StudentAffairsOfficeService.getAllModules(this.form.original.applicationData.newCourseOfStudy);
      } catch (error) {
        console.error(error.message);
      }
    },

    async sendToExaminingCommitteeChair() {
      try {
        await StudentAffairsOfficeService.sendFormToApproval(this.formCopy);
        this.$emit("save");
      } catch (error) {
        console.error(error.message);
      }
    },

    async saveApprovedForm(readyForApproval) {
      try {
        this.formCopy.edited.applicationData.dateLastEdited = new Date().toISOString();
        await StudentAffairsOfficeService.saveApprovedForm(this.formCopy);
        if (!readyForApproval) {
          this.$emit("save");
        }
      } catch (error) {
        console.error(error.message);
      }
    },

    async downloadPdf(filePath, fileName) {
      try {
        const pdfData = await StudentAffairsOfficeService.getModulePDF(filePath);
        const url = window.URL.createObjectURL(new Blob([pdfData]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', fileName + ".pdf");
        document.body.appendChild(link);
        link.click();
      } catch (error) {
        console.error('Error downloading PDF:', error.message);
      }
    },
  }
}
</script>

<style scoped>
.v-text-field {
  margin: 1%;
}

.v-row {
  margin: 1%;
}

.card-header {
  display: flex;
  align-items: center;
}

</style>

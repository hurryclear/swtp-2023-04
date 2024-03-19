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
          <v-btn class="ma-2" icon="mdi-call-split" @click="this.$emit('open', { component: 'SplitComponent', form: this.formCopy })" v-show="showEdited"/>
        </v-col>
        <v-col cols="3" md="1">
          <v-btn class="ma-2" icon="mdi-call-merge" @click="this.$emit('open', { component: 'MergeComponent', form: this.formCopy })" v-show="showEdited"/>
        </v-col>
        <v-col cols="3" md="1">
          <v-btn class="ma-2" icon="mdi-file-compare" @click="this.$emit('open',{component:'ComparisonMenu',formCopy:{}});"/>
        </v-col>
        <v-col cols="3" md="1">
          <v-btn class="ma-2" icon="mdi-close" @click="this.$emit('close')"/>
        </v-col>
      </v-row>
    </div>

    <v-divider/>

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
            <v-div>
              <u>{{$t('studentAffairsOfficeView.studentComment')}}:</u> {{ module.commentStudent }}
              <br>
              <u>{{$t('studentAffairsOfficeView.officeComment')}}:</u> {{ module.commentEmployee }}
            </v-div>
              <br>    
            <v-btn class="ma-2" @click="downloadPdf(module.path, module.title)">
              {{ $t('studentAffairsOfficeView.downloadDescription') }}
            </v-btn>

            <v-div v-if="module.approval === 'formally rejected'">
              <v-card-text>
                <u>{{ $t('reviewComponent.formallyRejected') }}</u>
                <br/>
                <u>{{ $t('reviewComponent.moduleApprovalReason') }}:</u> {{ module.reason }}
              </v-card-text>
            </v-div>

            <v-div v-else-if="showEdited">
              <v-text-field
                variant="outlined"
                :label="$t('studentAffairsOfficeView.reasonForDesicion')"
                v-model="module.reason"/>
            </v-div>
            
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
            <v-divider/>
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
          <v-divider/>
        </v-window-item>
      </v-window>
      <v-divider/>
      <v-card-actions>
        <v-div v-if="showEdited">
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
        </v-div>
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
      majorModules: [],
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
  },


  methods: {

    async openSplitMergeComponent() {
      this.$emit('open', { component: 'SplitMergeComponent', formCopy: this.formCopy });
    },
    
    async getModules() {
      try {
        this.majorModules = await StudentAffairsOfficeService.getAllModules(this.form.original.applicationData.newCourseOfStudy);
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

    /**
     * Merges two module mappings by combining their module data and credited module IDs.
     * @param {number} moduleMappingIndex1 - The index of the first module mapping.
     * @param {number} moduleMappingIndex2 - The index of the second module mapping.
     */
    mergeModuleMappings(moduleMappingIndex1, moduleMappingIndex2) {
      const mapping1 = this.formCopy.edited.moduleFormsData[moduleMappingIndex1];
      const mapping2 = this.formCopy.edited.moduleFormsData[moduleMappingIndex2];

      // Create a set to track unique module numbers
      const uniqueNumbers = new Set();

      // Filter out duplicates from mapping2
      const uniqueModules2 = mapping2.modulesStudent.filter(module => {
        if (uniqueNumbers.has(module.number)) {
          return false; // duplicate, exclude
        }
        uniqueNumbers.add(module.number);
        return true; // not a duplicate, include
      });

      // Append unique modules from mapping2 to mapping1
      mapping1.modulesStudent.push(...uniqueModules2);

      // Merge modules2bCredited arrays
      mapping1.modules2bCredited.push(...mapping2.modules2bCredited);

      // Remove mapping2
      this.formCopy.edited.moduleFormsData.splice(moduleMappingIndex2, 1);
    },

    /**
     * Splits a module mapping by moving selected modules and credited IDs to a new mapping.
     * @param {number} moduleMappingIndex - The index of the module mapping to split.
     * @param {number[]} moduleIndices - Indices of modules to move to the new mapping.
     * @param {number[]} modules2BCIds - IDs of credited modules to move to the new mapping.
     */
    splitModuleMapping(moduleMappingIndex, moduleIndices, modules2BCIds) {
      const originalModuleMapping = this.formCopy.edited.moduleFormsData[moduleMappingIndex];
      const newModuleMapping = {
        frontend_key: originalModuleMapping.frontend_key + 1,
        backend_block_id: originalModuleMapping.backend_block_id,
        modulesStudent: [],
        modules2bCredited: []
      };

      // Iterate over moduleIndices and add modules to the new ModuleMapping
      moduleIndices.forEach(moduleIndex => {
        const moduleToAdd = originalModuleMapping.modulesStudent[moduleIndex];
        // Check if moduleToAdd is not a duplicate in newModuleMapping
        if (!newModuleMapping.modulesStudent.some(module => module.number === moduleToAdd.number)) {
          newModuleMapping.modulesStudent.push(moduleToAdd);
        }
      });

      // Iterate over modules2BCIds and add IDs to the new ModuleMapping
      modules2BCIds.forEach(id => {
        // Check if id is not a duplicate in newModuleMapping
        if (!newModuleMapping.modules2bCredited.includes(id)) {
          newModuleMapping.modules2bCredited.push(id);
        }
      });

      // Remove the modules and IDs from the original ModuleMapping
      moduleIndices.sort((a, b) => b - a); // Sort moduleIndices in descending order
      moduleIndices.forEach(moduleIndex => {
        originalModuleMapping.modulesStudent.splice(moduleIndex, 1);
      });

      modules2BCIds.forEach(id => {
        const index = originalModuleMapping.modules2bCredited.indexOf(id);
        if (index !== -1) {
          originalModuleMapping.modules2bCredited.splice(index, 1);
        }
      });

      // Add the new ModuleMapping to the array
      this.formCopy.edited.moduleFormsData.splice(moduleMappingIndex + 1, 0, newModuleMapping);
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

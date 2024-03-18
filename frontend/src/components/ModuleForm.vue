<!-- ModuleForm.vue -->
<template>
  <v-expansion-panel>
    <v-expansion-panel-title>
      <template v-slot:default="{ expanded }">
        <v-row no-gutters>
          <v-col>
            {{ $t("applicationFormView.moduleFormList.moduleMapping.module") }}
          </v-col>
          <v-col class="text-grey">
            <v-fade-transition leave-absolute>
              <span v-if="expanded" key="0">
                {{ $t("applicationFormView.moduleFormList.moduleMapping.description") }}
              </span>
              <span v-else key="1">
                {{ moduleMapping.previousModules.map((module) => module.name).join(", ") }}
              </span>
            </v-fade-transition>
          </v-col>
        </v-row>
      </template>
    </v-expansion-panel-title>
    <v-expansion-panel-text>
      <v-tabs v-model="selectedTab">
        <v-tab v-for="(module, index) in moduleMapping.previousModules"
               :key="moduleMapping.previousModules[index].meta.key"
               :value="index">
          {{ module.name || $t("applicationFormView.moduleFormList.moduleMapping.module") + " " + (index + 1) }}
          <v-btn @click.stop="removeModule(index)" dense v-if="moduleMapping.previousModules.length>1">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-tab>
        <v-tab :key="moduleMapping.previousModules.length" @click="addModule" class="add-module-tab">
          {{ $t("applicationFormView.moduleFormList.moduleMapping.addModule") }}
        </v-tab>
      </v-tabs>
      <v-window v-model="selectedTab">
        <v-window-item
            v-for="(module,index) in moduleMapping.previousModules"
            :key="index"
            :value="index"
        >
          <v-text-field
              class="userInput"
              v-model="module.name"
              hide-details
              :label="$t('applicationFormView.moduleFormList.moduleMapping.moduleNameLabel')"
              variant="outlined"
          />
          <v-file-input
              v-model="module.description.file"
              class="userInput"
              accept=".pdf"
              show-size
              :label="$t('applicationFormView.moduleFormList.moduleMapping.moduleDescriptionLabel')"
              variant="outlined"
              hide-details
              prepend-icon=""
          />
          <v-combobox
              v-model="module.selectedUniversity"
              :items="universities"
              item-title="name"
              hide-details
              :label="$t('applicationFormView.universityForm.nameLabel')"
              variant="outlined"
              class="userInput"
          />
          <v-text-field
              class="userInput"
              v-model="module.courseOfStudy"
              hide-details
              :label="$t('applicationFormView.courseOfStudy.courseOfStudy')"
              variant="outlined"
          />
          <v-text-field
              class="userInput"
              v-model="module.id"
              hide-details
              :label="$t('applicationFormView.moduleFormList.moduleMapping.moduleId')"
              variant="outlined"
          />
          <v-text-field
              v-model="module.credits"
              class="userInput"
              hide-details
              :label="$t('applicationFormView.moduleFormList.moduleMapping.creditLabel')"
              variant="outlined"
              type="number"
              min="0"
              max="30"
          />
          <v-text-field
              v-model="module.meta.comments.student"
              class="userInput"
              hide-details
              :label="$t('applicationFormView.moduleFormList.moduleMapping.commentLabel')"
              variant="outlined"
          />
        </v-window-item>
      </v-window>
      <v-select
          v-model="moduleMapping.modulesToBeCredited"
          class="userInput"
          item-title="name"
          item-value="id"
          :label="$t('applicationFormView.moduleFormList.moduleMapping.modulesToBeCreditedLabel')"
          variant="outlined"
          :items="modules"
          hide-details
          multiple
      />
      <v-btn
          @click="removeModuleMapping"
          color="red"
          :disabled="disableModuleMappingRemoval"
      >
        {{ $t("applicationFormView.moduleFormList.removeMapping") }}
      </v-btn>
    </v-expansion-panel-text>
  </v-expansion-panel>
</template>

<script>

export default {
  data() {
    return {
      selectedTab: 0,
      selectedFile: null,
      moduleMapping: {
        meta: {
          key: this.moduleKey
        },
        previousModules: [
          {
            meta: {
              key: 0,
              comments: {
                student: "",
              },
            },
            name: "",
            credits: 0,
            university: {
              name: null,
              country: "",
              website: "",
            },
            selectedUniversity: null,
            id: "",
            courseOfStudy: "",
            description: {
              file: null,
            },
          }
        ],
        modulesToBeCredited: [],
      },
    };
  },
  props: {
    moduleMappingIndex: Number,
    moduleKey: Number,
  },
  computed: {
    disableModuleMappingRemoval() {
      return this.$store.getters.disableModuleMappingRemoval;
    },
    universities() {
      return this.$store.state.university.universities;
    },
    modules() {
      return this.$store.state.module.modules;
    }
  },
  methods: {
    addModule() {
      const key = this.moduleMapping.previousModules[this.moduleMapping.previousModules.length - 1].meta.key + 1
      const moduleMappingIndex = this.moduleMappingIndex
      this.$store.commit('addModule', {moduleMappingIndex, key});
      this.moduleMapping = this.$store.getters.getModuleMappingByIndex(moduleMappingIndex)
      this.selectedTab = this.moduleMapping.previousModules.length - 1;
    },
    removeModule(moduleIndex) {
      this.$store.commit('removeModule', {
        moduleMappingIndex: this.moduleMappingIndex,
        moduleIndex: moduleIndex,
      })
      if (this.selectedTab === this.moduleMapping.previousModules.length) {
        this.selectedTab--
      }
    },
    removeModuleMapping() {
      this.$store.commit('removeModuleMappingForm', this.moduleMappingIndex)
    },
  },
  watch: {
    'moduleMapping.previousModules': {
      deep: true,
      handler(newValue, oldValue) {
        // This watcher will be triggered whenever any property inside previousModules changes
        // We need to check if the selectedUniversity has changed
        newValue.forEach((module, index) => {
          if (module.selectedUniversity !== oldValue[index].selectedUniversity) {
            // If selectedUniversity has changed, update the university properties
            const newValue = module.selectedUniversity;
            if (typeof newValue === 'string') {
              this.moduleMapping.previousModules[index].university.name = newValue;
            } else {
              if (!newValue) { // Check if newValue is null or undefined
                this.moduleMapping.previousModules[index].university.name = '';
                return;
              }
              this.moduleMapping.previousModules[index].university = newValue
            }
          }
        });
      }
    },
    moduleMapping: {
      handler(newVal) {
        this.$store.commit("updateModuleMappingData", {
          moduleMappingIndex: this.moduleMappingIndex,
          newData: newVal,
        })
      },
      deep: true,
    },
    selectedTab: {
      handler(newVal) {
        if (!newVal) {
          this.selectedTab = 0
        }
      }
    }
  }
}
</script>
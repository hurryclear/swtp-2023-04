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
                {{ moduleForm.previousModules.map((module) => module.name).join(", ") }}
              </span>
            </v-fade-transition>
          </v-col>
        </v-row>
      </template>
    </v-expansion-panel-title>
    <v-expansion-panel-text>
      <v-tabs v-model="selectedTab">
        <v-tab v-for="(module, index) in moduleForm.previousModules" :key="moduleForm.previousModules[index].key"
               :value="index">
          {{ module.name || $t("applicationFormView.moduleFormList.moduleMapping.module") + " " + (index + 1) }}
          <v-btn @click.stop="removeTab(index)" dense v-if="moduleForm.previousModules.length>1">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-tab>
        <v-tab :key="moduleForm.previousModules.length" @click="addModule" class="add-module-tab">
          {{ $t("applicationFormView.moduleFormList.moduleMapping.addModule") }}
        </v-tab>
      </v-tabs>
      <v-window v-model="selectedTab"> <!--TODO: correct submission and add module behaviour-->
        <v-window-item
            v-for="(module, index) in moduleForm.previousModules"
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
              v-model="module.university"
              :items="universities"
              item-title="name"
              hide-details
              :label="$t('applicationFormView.universityForm.university.nameLabel')"
              variant="outlined"
              class="userInput"
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
          v-model="moduleForm.modulesToBeCredited"
          class="userInput"
          item-title="name"
          :label="$t('applicationFormView.moduleFormList.moduleMapping.modulesToBeCreditedLabel')"
          variant="outlined"
          hide-details
          multiple
          :items="modules"
      />
      <v-text-field
          v-model="moduleForm.meta.comments.student"
          class="userInput"
          hide-details
          :label="$t('applicationFormView.moduleFormList.moduleMapping.commentLabel')"
          variant="outlined"
      />
      <v-btn
          @click="removeModule"
          color="red"
          :disabled="removeDisabled"
      >
        {{ $t("applicationFormView.moduleFormList.removeMapping") }}
      </v-btn>
    </v-expansion-panel-text>
  </v-expansion-panel>
</template>

<script>

export default {
  props: {
    moduleMapping: Object, // Add a prop to receive module data
    removeDisabled: Boolean,
  },
  data() {
    return {
      isFilled: false,
      selectedTab: 0,
      moduleForm: {...this.moduleMapping},
      selectedFile: null,
    };
  },
  computed: {
    universities() {
      return this.$store.state.university.universities;
    },
    modules() {
      return this.$store.state.module.modules;
    }
  },
  methods: {
    addModule() {
      this.moduleForm.previousModules.push(
          {
            meta: {
              comments: {
                student: "",
                office: ""
              }
            },
            university: "",
            key: (this.moduleForm.previousModules[this.moduleForm.previousModules.length - 1].key + 1),
            number: "",
            name: "",
            description: {file: null},
            credits: 0,
          },
      );
      this.selectedTab = this.moduleForm.previousModules.length - 1;
    },
    checkFormFilled() {
      this.moduleForm.isFilled =
          this.moduleForm.previousModules.every(
              (module) => module.name.trim() !== '' &&
                  module.description.file !== null
          ) &&
          //TODO Description
          this.moduleForm.modulesToBeCredited !== null;
    },
    removeTab(index) {
      this.moduleForm.previousModules.splice(index, 1);
      if (this.selectedTab === this.moduleForm.previousModules.length) {
        this.selectedTab--
      }
      this.watchModuleData();
    },
    removeModule() {
      this.$emit('removeModule');
    },
    // Watch changes in module data and emit an event to the parent
    watchModuleData() {
      this.checkFormFilled();
      this.$emit('updateModuleData', this.moduleForm, this.selectedFile);
    }
  },
  watch: {
    'moduleForm': {
      handler: 'watchModuleData',
      deep: true,
    },
    'selectedTab': function (val) {
      if (!val) {
        this.selectedTab = 0
      }
    }
  }
}
</script>
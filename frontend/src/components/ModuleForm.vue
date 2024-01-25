<template>
  <v-expansion-panel>
    <v-expansion-panel-title>
      <template v-slot:default="{ expanded }">
        <v-row no-gutters>
          <v-col>
            {{ $t("applicationForm.module") }}
          </v-col>
          <v-col class="text-grey">
            <v-fade-transition leave-absolute>
              <span v-if="expanded" key="0">
                {{ $t("applicationForm.moduleDescription") }}
              </span>
              <span v-else key="1"> <!--TODO:Multiple Modules here-->
                {{ moduleForm.previousModules[0].name }}
              </span>
            </v-fade-transition>
          </v-col>
        </v-row>
      </template>
    </v-expansion-panel-title>
    <v-expansion-panel-text>
      <!--TODO:Multiple Modules here-->
      <v-text-field
          class="userInput"
          v-model="moduleForm.previousModules[0].name"
          hide-details
          :label="$t('applicationForm.moduleNameLabel')"
          variant="outlined"
      />
      <!--TODO: change v-model...-->
      <v-file-input
          v-model="moduleForm.description"
          class="userInput"
          accept=".pdf"
          show-size
          :label="$t('applicationForm.moduleDescriptionLabel')"
          variant="outlined"
          hide-details
          prepend-icon=""
          @change="handleFileChange"
      />
      <v-select
          v-model="moduleForm.modulesToBeCredited"
          class="userInput"
          item-title="name"
          :label="$t('applicationForm.moduleCreditedLabel')"
          variant="outlined"
          hide-details
          multiple
          :items="moduleNamesList"
      />
      <v-text-field
          v-model="moduleForm.meta.comments.student"
          class="userInput"
          hide-details
          :label="$t('applicationForm.commentLabel')"
          variant="outlined"
      />
      <v-btn
          @click="removeModule"
          color="red"
          :disabled="removeDisabled"
      >
        {{ $t("applicationForm.removeModule") }}
      </v-btn>
    </v-expansion-panel-text>
  </v-expansion-panel>
</template>

<script>
import moduleJSON from '../assets/module_liste.json'; //TODO: API Request for this

export default {
  props: {
    moduleMapping: Object, // Add a prop to receive module data
    removeDisabled: Boolean,
  },
  data() {
    return {
      isFilled: false,
      moduleNamesList: [],
      moduleForm: {...this.moduleMapping},
      selectedFile: null,
    };
  },
  mounted() {
    this.extractModuleNames();
  },
  methods: {
    extractModuleNames() {
      this.moduleNamesList = moduleJSON.courses[0].modules;
    },
    checkFormFilled() {
      this.moduleForm.isFilled =
          this.moduleForm.previousModules.every((module) => module.name.trim() !== '') &&
          //TODO Description
          this.moduleForm.description !== null &&
          this.moduleForm.modulesToBeCredited !== null;
    },
    removeModule() {
      this.$emit('removeModule');
    },
    handleFileChange(event) {
      this.selectedFile = event.target.files[0];
    },
    // Watch changes in module data and emit an event to the parent
    watchModuleData() {
      this.checkFormFilled();
      this.$emit('updateModuleData', this.moduleForm, this.selectedFile);
    },
  },
  watch: {
    'moduleForm': {
      handler: 'watchModuleData',
      deep: true,
    },
  }

}
</script>
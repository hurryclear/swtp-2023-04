<template>
  <v-card class="pa-2" v-if="formCopy">
    <v-select
        v-model="selectedModuleMappingIndex"
        :items="moduleOptions"
        item-title="title"
        item-value="value"
        :label="$t('mergeComponents.selectMappingSplit')"
        outlined
    />
    <v-select
        v-if="selectedModuleMappingIndex!==null"
        v-model="selectedModules"
        :items="formCopy.edited.moduleFormsData[selectedModuleMappingIndex].modulesStudent"
        item-title="title"
        :item-value="item => item"
        :label="$t('mergeComponents.selectModuleSplit')"
        multiple
        :disabled="formCopy.edited.moduleFormsData[selectedModuleMappingIndex].modulesStudent.length<2"
        outlined
    />
    <v-select
        v-if="selectedModuleMappingIndex!==null"
        v-model="selectedCreditedIds"
        :items="formCopy.edited.moduleFormsData[selectedModuleMappingIndex].modules2bCredited.map(id => majorModules.find(module => module.id === id))"
        item-title="name"
        item-value="id"
        :label="$t('mergeComponents.selectModuleTBCSplit')"
        :disabled="formCopy.edited.moduleFormsData[selectedModuleMappingIndex].modulesStudent.length<2"
        multiple
        outlined
    />
    <v-btn @click="splitModule" :disabled="formNotFilled" color="primary">Split Module</v-btn>
  </v-card>
</template>

<script>

export default {
  props: {
    form: Object
  },
  data() {
    return {
      formCopy: null,
      selectedModuleMappingIndex: null,
      selectedModules: [],
      selectedCreditedIds: [],
    };
  },
  computed: {
    majorModules() {
      return this.$store.state.studentAffairsOffice.majorModules;
    },
    moduleOptions() {
      if (!this.formCopy || !this.formCopy.edited.moduleFormsData) return [];
      return this.formCopy.edited.moduleFormsData.map((module, index) => ({
        title: `Module Mapping ${index + 1}`,
        value: index,
      }));
    },
    formNotFilled(){
      return (this.selectedModuleMappingIndex === null ||
          this.selectedModules.length === 0 ||
          this.selectedCreditedIds.length === 0);
    }
  },
  created() {
    this.formCopy = structuredClone(this.form);
  },
  methods: {
    splitModule() {
      if (this.formNotFilled) return;

      // Check if both original and new mappings have at least one module selected
      const originalMapping = this.formCopy.edited.moduleFormsData[this.selectedModuleMappingIndex];
      const newMappingModules = this.selectedModules;
      if (originalMapping.modulesStudent.length === 0 || newMappingModules.length === 0) return;

      this.splitModuleMapping(
          this.selectedModuleMappingIndex,
          this.selectedModules,
          this.selectedCreditedIds
      );
    },
    splitModuleMapping(moduleMappingIndex, modules, modules2BCIds) {
      const originalModuleMapping = this.formCopy.edited.moduleFormsData[moduleMappingIndex];
      const newModuleMapping = {
        backend_block_id: null,
        frontend_key: -1,
        modulesStudent: [], // Initialize with empty array
        modules2bCredited: [], // Initialize with empty array
      };

      // Copy selected modules from originalModuleMapping to newModuleMapping
      modules.forEach((module) => {
        newModuleMapping.modulesStudent.push(module); // Simply push the module to the array
      });
      // Copy credited module IDs to newModuleMapping
      modules2BCIds.forEach((id) => {
        newModuleMapping.modules2bCredited.push(id);
      });

      modules.forEach((module)=>{
        const index = originalModuleMapping.modulesStudent.indexOf(module);
        if (index !== -1) {
          originalModuleMapping.modulesStudent.splice(index, 1);
        }
      })

      // Remove credited module IDs from originalModuleMapping
      modules2BCIds.forEach((id) => {
        const index = originalModuleMapping.modules2bCredited.indexOf(id);
        if (index !== -1) {
          originalModuleMapping.modules2bCredited.splice(index, 1);
        }
      });

      // Insert newModuleMapping after the current moduleMappingIndex
      this.formCopy.edited.moduleFormsData.splice(moduleMappingIndex + 1, 0, newModuleMapping);
      console.log(JSON.stringify(this.formCopy))
      this.$emit('open', {component: 'EditMenu', form: this.formCopy})
    },
  },
};
</script>

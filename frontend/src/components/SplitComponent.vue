<template>
  <v-card class="pa-2" v-if="formCopy">
    <v-select
        v-model="selectedModuleIndex"
        :items="moduleOptions"
        item-title="title"
        item-value="value"
        label="Select Module Mapping to be Split"
        outlined
    />
    <v-select
        v-if="selectedModuleIndex!==null"
        v-model="selectedModuleIndices"
        :items="formCopy.edited.moduleFormsData[selectedModuleIndex].modulesStudent"
        item-title="title"
        :item-value="item => item"
        label="Select Modules to be split off"
        multiple
        outlined
    />
    <v-select
        v-if="selectedModuleIndex!==null"
        v-model="selectedCreditedIds"
        :items="formCopy.edited.moduleFormsData[selectedModuleIndex].modules2bCredited"
        :title="filteredModules"
        item-title="title"
        item-value="id"
        label="Select Modules To Be Credited to be split off"
        multiple
        outlined
    />
    <v-btn @click="splitModule" color="primary">Split Module</v-btn>
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
      selectedModuleIndex: null,
      selectedModuleIndices: [],
      selectedCreditedIds: [],
    };
  },
  computed: {
    majorModules() {
      return this.$store.state.studentAffairsOffice.majorModules;
    },
    filteredModules() {
      if (this.selectedModuleIndex && this.formCopy.edited.moduleFormsData[this.selectedModuleIndex]) {
        const modules2bCredited = this.formCopy.edited.moduleFormsData[this.selectedModuleIndex].modules2bCredited;
        return modules2bCredited.filter(item => this.majorModules.some(majorModule => majorModule.id === item.id));
      }
      return [];
    },
    moduleOptions() {
      if (!this.formCopy || !this.formCopy.edited.moduleFormsData) return [];
      return this.formCopy.edited.moduleFormsData.map((module, index) => ({
        title: `Module Mapping ${index + 1}`,
        value: index,
      }));
    },
  },
  created() {
    this.formCopy = structuredClone(this.form);
  },
  methods: {
    splitModule() {
      if (
          this.selectedModuleIndex === null ||
          this.selectedModuleIndices.length === 0 ||
          this.selectedCreditedIds.length === 0
      ) {
        return;
      }
      this.splitModuleMapping(
          this.selectedModuleIndex,
          this.selectedModuleIndices,
          this.selectedCreditedIds
      );
    },
    splitModuleMapping(moduleMappingIndex, modules, modules2BCIds) {
      const originalModuleMapping = this.formCopy.edited.moduleFormsData[moduleMappingIndex];
      const newModuleMapping = {
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

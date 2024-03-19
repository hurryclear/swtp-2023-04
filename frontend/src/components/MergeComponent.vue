<template>
  <v-card class="pa-2" v-if="formCopy">
    <v-select
        v-model="selectedModuleIndices"
        :items="moduleOptions"
        item-title="title"
        item-value="value"
        :label="$t('mergeComponents.selectModuleMerge')"
        outlined
        multiple
    />
    <v-btn @click="mergeModules" color="primary">Merge Modules</v-btn>
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
      selectedModuleIndices: [],
    };
  },
  created() {
    this.formCopy = structuredClone(this.form);
  },computed: {
    moduleOptions() {
      if (!this.formCopy || !this.formCopy.edited.moduleFormsData) return [];
      return this.formCopy.edited.moduleFormsData.map((module, index) => ({
        title: `Module Mapping ${index + 1}`,
        value: index,
      }));
    },
  },
  methods: {
    mergeModules() {
      if (this.selectedModuleIndices.length < 2) {
        // Display error or prompt user to select at least two modules
        return;
      }
      this.mergeModuleMappings(this.selectedModuleIndices);
    },
    mergeModuleMappings(moduleIndices) {
      const mergedModules = [];
      const merged2bCredited = [];

      moduleIndices.forEach(index => {
        const mapping = this.formCopy.edited.moduleFormsData[index];
        mergedModules.push(...mapping.modulesStudent);
        merged2bCredited.push(...mapping.modules2bCredited);
      });

      const targetIndex = moduleIndices[0];
      const targetMapping = this.formCopy.edited.moduleFormsData[targetIndex];

      // Add unique modules to the target mapping
      const uniqueNumbers = new Set(targetMapping.modulesStudent.map(module => module.number));
      mergedModules.forEach(module => {
        if (!uniqueNumbers.has(module.number)) {
          targetMapping.modulesStudent.push(module);
          uniqueNumbers.add(module.number);
        }
      });

      // Add unique 2b credited modules to the target mapping
      merged2bCredited.forEach(module => {
        if (!targetMapping.modules2bCredited.includes(module)) {
          targetMapping.modules2bCredited.push(module);
        }
      });

      // Remove merged mappings except the first one
      moduleIndices.shift(); // Remove the first index, as it's already targeted
      moduleIndices.reverse().forEach(index => {
        this.formCopy.edited.moduleFormsData.splice(index, 1);
      });
      const userRole = this.$store.state.authentication.userRole
      if (userRole==='ROLE_COMMITTEE'){
        this.$emit('open',{component: 'EditMenuCommittee', form: this.formCopy})
      } else if (userRole==='ROLE_OFFICE'){
        this.$emit('open', {component: 'EditMenu', form: this.formCopy})
      }
    },
  },
};
</script>
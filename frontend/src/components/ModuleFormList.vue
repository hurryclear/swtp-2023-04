<template>
  <div>
    <v-expansion-panels>
      <ModuleForm
          v-for="(form, index) in moduleForms"
          :key="moduleForms[index].key"
          :module="form"
          :removeDisabled =" removeDisabled"
          @removeModule="removeModuleForm(index)"
          @updateModuleData="(form,file)=>updateModuleData(index,form,file)"
      />
      <v-btn
          class="userInput"
          @click="addModuleForm"
          :disabled="!this.formsFilled"
      >
        {{ $t("applicationForm.addModule") }}
      </v-btn>
    </v-expansion-panels>
  </div>
</template>

<script>
import ModuleForm from "@/components/ModuleForm.vue";
import {defineComponent} from "vue";

export default defineComponent({
  components: {ModuleForm},
  data() {
    return {
      moduleForms: [
        {
          key: 0,
          name: '',
          comment: '',
          description: null,
          file: null,
          module2bCredited: null
        }
      ],
      formsFilled: false,
    };
  },
  computed: {
    removeDisabled: this.moduleForms.length === 1
  },
  methods: {
    addModuleForm() {
      this.moduleForms.push({
        key: (this.moduleForms[this.moduleForms.length - 1].key + 1), name: '', comment: '', description: null,
        file: null, module2bCredited: null, isFilled: false
      });
      this.formsFilled = false;
    },
    checkIfFilled() {
      this.formsFilled = this.moduleForms.every(form => form.isFilled);
      return this.formsFilled;
    },
    removeModuleForm(index) {
      if (this.moduleForms.length > 1) {
        this.moduleForms.splice(index, 1);
        this.checkIfFilled()
      }
    },
    // Listen to the emitted event from ModuleForm and propagate it to the parent (FormPage)
    updateModuleData(index, data, file) {
      this.moduleForms[index] = data;
      this.moduleForms[index].file = file
      if (this.checkIfFilled()) {
        this.$emit('updateModuleData', this.moduleForms)
      }
    },
    emitFillChange() {
      this.$emit('fillChange', this.formsFilled)
    },
  },
  watch: {
    "formsFilled": "emitFillChange"
  }
});
</script>

<style scoped>
.v-btn {
  width: 100%;
}
</style>

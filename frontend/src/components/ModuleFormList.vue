<template>
  <div>
    <v-expansion-panels>
      <ModuleForm
          v-for="(form, index) in moduleForms"
          :key="index"
          @formFilled="handleFormFilled"
          @removeModule="removeModuleForm(index)"
      />
      <v-btn
          class="userInput"
          @click="addModuleForm"
          :disabled="!previousFormFilled"
      >
        Modul hinzufügen
      </v-btn>
    </v-expansion-panels>
  </div>
</template>

<script>
import ModuleForm from "@/components/ModuleForm.vue";
import { defineComponent } from "vue";

export default defineComponent({
  components: { ModuleForm },
  data() {
    return {
      moduleForms: [ModuleForm], // Array to store instances of ModuleForm
      previousFormFilled: false,
    };
  },
  methods: {
    addModuleForm() {
      const newModuleForm = ModuleForm; // Create a new instance of ModuleForm
      this.moduleForms.push(newModuleForm);
      this.previousFormFilled = false;
    },
    handleFormFilled() {
      this.previousFormFilled = true; // Enable the button when the current form is filled
    },
    removeModuleForm(index) {
      console.log(index)
      this.moduleForms.splice(index, 1);
    },
  },
});
</script>

<style scoped>
  .v-btn{
    width:100%
  }
</style>

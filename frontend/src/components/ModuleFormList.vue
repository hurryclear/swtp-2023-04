<template>
  <div>
    <v-expansion-panels>
      <template v-for="(form, index) in moduleForms" :key="index">
        <ModuleForm
            :module="form"
            @formFilled="handleFormFilled"
            @removeModule="removeModuleForm(index)"
        />
      </template>
      <v-btn
          class="userInput"
          @click="addModuleForm"
          :disabled="!previousFormFilled"
          variant="outlined"
      >
        {{ $t("moduleForm.addModule") }}
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
      moduleForms: [ModuleForm],
      previousFormFilled: false,
    };
  },
  methods: {
    addModuleForm() {
      this.moduleForms.push(ModuleForm);
      this.previousFormFilled = false;
    },
    handleFormFilled() {
      this.previousFormFilled = true;
    },
    removeModuleForm(index) {
      if (this.moduleForms.length > 1) {
        console.log("Deleting module at Index", index);
        this.moduleForms.splice(index, 1);
      } else {
        console.log("Not deleting the last module.");
      }
    },
  },
});
</script>

<style scoped>
.v-btn {
  width: 100%;
}
</style>

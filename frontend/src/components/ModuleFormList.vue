<template>
  <div>
    <v-expansion-panels>
      <ModuleForm
          v-for="(form, index) in moduleMappings"
          :key="this.moduleMappings[index].meta.key"
          :moduleMapping="form"
          :removeDisabled=" removeDisabled"
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
      moduleMappings: [
        {
          meta: {
            key: 0,
            approval: "",
            comments: {
              student: "",
              office: "",
            },
          },
          previousModules: [
            {
              number: "",
              name: "",
              description: {
                id: "",
                filename: "",
              },
              credits: 0,
            },
          ],
          modulesToBeCredited: [],
        },
      ],
      formsFilled: false,
    };
  },
  computed: {
    removeDisabled() {
      return this.moduleMappings.length === 1
    }
  },
  methods: {
    addModuleForm() {
      this.moduleMappings.push({
        meta: {
          key: (this.moduleMappings[this.moduleMappings.length - 1].key + 1),
          approval: "",
          comments: {
            student: "",
            office: "",
          },
        },
        previousModules: [
          {
            number: "",
            name: "",
            description: {
              id: "",
              filename: "",
            },
            credits: 0,
          },
        ],
        modulesToBeCredited: [
          {
            number: "",
            name: null,
          }
        ],
      });
      this.formsFilled = false;
    },
    checkIfFilled() {
      this.formsFilled = this.moduleMappings.every(form => form.isFilled);
      return this.formsFilled;
    },
    removeModuleForm(index) {
      if (this.moduleMappings.length > 1) {
        this.moduleMappings.splice(index, 1);
        this.checkIfFilled()
      }
    },
    // Listen to the emitted event from ModuleForm and propagate it to the parent (FormPage)
    updateModuleData(index, data/*, file*/) {//TODO
      this.moduleMappings[index] = data;
      //this.moduleForms[index].file = file
      if (this.checkIfFilled()) {
        this.$emit('updateModuleData', this.moduleMappings)
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

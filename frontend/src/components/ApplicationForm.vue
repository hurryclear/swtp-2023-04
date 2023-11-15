<script>
import UniversityForm from "@/components/UniversityForm.vue";
import {defineComponent} from "vue";
import ModuleFormList from "@/components/ModuleFormList.vue";

export default defineComponent({
  components: {ModuleFormList, UniversityForm}
})

</script>

<template>
  <UniversityForm/>
  <br/>
  <ModuleFormList/>
</template>

<style>
.userInput{
  margin-bottom: 0.5rem;
  margin-top: 0.5rem;
}
</style>
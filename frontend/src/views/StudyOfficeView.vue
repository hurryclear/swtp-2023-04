<template>
    <v-container fluid class="wrapper">
      <h1>Offene Anträge</h1>
      <FormDisplay @open-edit-menu="displayEditMenu"/>
      <EditMenu v-if="isDisplayed" :form="formContent"/>
    </v-container>
</template>

<script>
  import EditMenu from "@/components/EditMenu.vue";
  import FormDisplay from "@/components/FormDisplay.vue"
  export default {
    components: {EditMenu, FormDisplay},
    data() {
      return {
        isDisplayed: false,
        formContent: {}
      }
    },
    methods: {
      displayEditMenu(form) {
        this.formContent = form;
        this.isDisplayed = true;
      }
    }
  }



</script>

<style scoped>
  .wrapper {
    margin-left: 5%;
  }
</style>
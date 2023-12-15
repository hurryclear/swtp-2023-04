<template>
  <v-container fluid>
    <h1>Studienbüro: Offene Anträge</h1>
    <v-row>
      <FormDisplay class="form-display" :forms="openForms" @open-edit-menu="openEditMenu"/>
      <EditMenu class="edit-menu" v-if="isDisplayed" :form="formContent" @close-edit-menu="closeEditMenu"/>
    </v-row>
  </v-container>
</template>

<script>
import FormDisplay from "@/components/FormDisplay.vue"; // Adjust path as necessary
import EditMenu from "@/components/EditMenu.vue"; // Adjust path as necessary

export default {
  components: {
    FormDisplay,
    EditMenu
  },
  data() {
    return {
      isDisplayed: false,
      formContent: {}
    };
  },
  methods: {

    openEditMenu(form) {
      this.formContent = form;
      this.isDisplayed = true;
    },
    closeEditMenu() {
      this.isDisplayed = false;
      this.formContent = {};
    }
  }
}
</script>

<style scoped>

  .form-display {
    border: 2px solid gray;
    border-radius: 3px;
  }

  .edit-menu {
    border: 2px solid gray;
    border-radius: 3px;
    width: 40%;
  }
</style>
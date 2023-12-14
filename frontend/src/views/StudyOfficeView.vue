<template>
    <v-container fluid class="wrapper">
      <h1 style="margin-bottom: 2%">Offene Anträge</h1>
      <v-row>
        <v-col>
          <FormDisplay class="form-display" @open-edit-menu="openEditMenu"/>
        </v-col>
        <v-col>
          <EditMenu class="edit-menu" v-if="isDisplayed" :form="formContent" @close-edit-menu="closeEditMenu"/>
        </v-col>
      </v-row>
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
  .wrapper {
    margin-left: 5%;
  }

  .form-display {
    border: 2px solid gray;
    border-radius: 3px;
    display: flow;
    width: 80%;
  }

  .edit-menu {
    border: 2px solid gray;
    border-radius: 3px;
    width: 80%;
  }
</style>
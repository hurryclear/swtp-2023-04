<template>
    <v-container fluid class="wrapper">
      <v-row>
        <h1 style="margin-bottom: 2%">Offene Anträge</h1>
        <v-spacer/>
        <v-spacer/>
        <LogoutButton style="margin-left: 40%"/>
      </v-row>
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
  import LogoutButton from "@/components/LogoutButton.vue";
  export default {
    components: {EditMenu, FormDisplay, LogoutButton},
    data() {
      return {
        isDisplayed: false,
        formContent: {}
      }
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
    border-radius: 10px;
    width: 80%;
  }

  .edit-menu {
    border: 2px solid gray;
    border-radius: 10px;
    width: 80%;
  }
</style>
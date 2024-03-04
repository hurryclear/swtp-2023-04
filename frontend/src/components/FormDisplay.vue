<template>
  <h1>{{$t("studentAffairsOfficeView.openApplications")}}</h1>

  <v-card>
    <template v-slot:text>
      <v-text-field
          v-model="search"
          label="Search"
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          hide-details
          single-line
      ></v-text-field>
    </template>
  <v-data-table :headers="headers" :items="forms" :search="search" item-key="id" class="table">
    <template v-slot:[`item.actions`]="{ item }">
      <v-btn @click="openEditMenu(item)" icon="mdi-pencil"></v-btn>
    </template>
  </v-data-table>
  </v-card>
</template>

<script>
// <!-----formArray and imports are strictly for testing purposes-----!>
/*import form1 from "@/assets/form-2017-10-21T22_11_56.973Z.json"
import form2 from "@/assets/form-2023-12-13T6_45_30.512.json"
import form3 from "@/assets/form-2023-12-15T10_34_51.207Z.json"
import form4 from "@/assets/form4.json"*/
export default {
    data() {
      return {
        search: "",
        //formArray: [form1,form2,form3,form4],
        //TODO i18n
        headers: [
          { title: "ID", key: "id" },
          { title: "University" , key: "universityData.universityName" },
          { title: "Number of Modules", key: "moduleFormsData.length" },
          { title: "Previous Study Program", key: "universityData.studyProgram" },
          { title: "Edit", value: "actions", sortable: false}
        ]
      }
    },
    computed: {
      // Use Vuex getter to get forms with a specific status
      forms() {
        return this.$store.getters.formsByStatus('Offen');
      }
    },
    methods: {
      openEditMenu(form) {
        this.$emit('open-edit-menu', form);
      }
    }
  }
</script>

<style scoped>

  .table {
    width: 70rem;
  }

  td {
    padding: 1rem;
    width: 20%;
  }

</style>
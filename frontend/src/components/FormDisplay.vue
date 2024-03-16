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
      />
    </template>
  <v-data-table :headers="headers" :items="forms" :search="search" item-key="id">
    <template v-slot:[`item.actions`]="{ item }">
      <v-btn
          @click="openEditMenu(item)"
          icon="mdi-pencil"
      />
    </template>
  </v-data-table>
  </v-card>
</template>

<script>
import StudentAffairsOfficeService from "@/services/StudentAffairsOfficeService";
export default {
    data() {
      return {
        search: "",
        forms: [],
        //TODO i18n
        headers: [
          { title: "ID", key: "applicationID" },
          { title: "Universität" , key: "university" },
          { title: "Antragsdatum", key: "dateOfSubmission" },
          { title: "Letzte Bearbeitung", key: "dateLastEdited"},
          { title: "Status", key: "status" },
          { title: "Editieren", value: "actions", sortable: false }
        ]
      }
    },

  async created() {
    await this.getForms();
    this.forms = this.forms.map(form => ({
      ...form,
      dateOfSubmission: this.formatDate(form.dateOfSubmission),
      dateLastEdited: this.formatDate(form.dateLastEdited)
    }));
  },

  methods: {
    async openEditMenu(item) {
      try {
        const form = await StudentAffairsOfficeService.getApplication(item.applicationID);
        this.$emit('open', { component: 'EditMenu', form });
      } catch (error) {
        console.error("Error retrieving form: ", error);
      }
    },

    async getForms() {
      try {
        this.forms = await StudentAffairsOfficeService.getOfficeOverview();
      } catch (error) {
        console.error("Error retrieving open forms: ", error);
      }
    },

    formatDate(inputDate) {
      const date = new Date(inputDate);
      const options = {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      };
      return date.toLocaleDateString(this.$i18n.locale, options);
    }
  }
}
</script>

<style scoped>

  td {
    padding: 1rem;
    width: 20%;
  }

</style>
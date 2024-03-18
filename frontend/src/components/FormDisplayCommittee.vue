<template>
  <h1>{{$t("studentAffairsOfficeView.openApplications")}}</h1>

  <v-card>
    <template v-slot:text>
      <v-text-field
          v-model="search"
          :label="$t('studentAffairsOfficeView.search')"
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          hide-details
          single-line
      />
    </template>
    <v-data-table :headers="translatedHeaders" :items="formattedForms" :search="search" item-key="id">
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
      forms: []
    }
  },

  async created() {
    await this.getForms();
  },

  methods: {
    async openEditMenu(item) {
      try {
        const form = await StudentAffairsOfficeService.getApplication(item.applicationID);
        this.$emit('open', {component: 'EditMenuCommittee', form});
      } catch (error) {
        console.error("Error retrieving form: ", error);
      }
    },

    async getForms() {
      try {
        this.forms = await StudentAffairsOfficeService.getCommitteeOverview();
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
    },
  },

    computed: {
    translatedHeaders() {
      return [
        {title: this.$t("studentAffairsOfficeView.ID"), key: "applicationID"},
        {title: this.$t("studentAffairsOfficeView.university"), key: "university"},
        {title: this.$t("studentAffairsOfficeView.dateOfSubmission"), key: "dateOfSubmission"},
        {title: this.$t("studentAffairsOfficeView.dateLastEdited"), key: "dateLastEdited"},
        {title: this.$t("studentAffairsOfficeView.status"), key: "status"},
        {title: this.$t("studentAffairsOfficeView.edit"), value: "actions", sortable: false}
      ];
    },
    formattedForms() {
      return this.forms.map(form => ({
        ...form,
        dateOfSubmission: this.formatDate(form.dateOfSubmission),
        dateLastEdited: this.formatDate(form.dateLastEdited)
      }));
    }
  },
}
</script>

<style scoped>
td {
  padding: 1rem;
  width: 20%;
}
</style>

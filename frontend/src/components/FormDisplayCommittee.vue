<template>
  <h1>{{$t("studentAffairsOfficeView.openApplications")}}</h1>

  <v-card>
    <v-data-table
        v-model:items-per-page="itemsPerPage"
        v-model:sort-by="sortBy"
        :headers="translatedHeaders"
        :items="formattedForms"
        :items-length="totalItems"
        :loading="loading"
        :page="page"
        item-key="id"
        @update:options="getForms"
    >
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
      itemsPerPage: 5,
      page: 1,
      totalItems: 0,
      sortBy: [],
      loading: false,
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
        this.loading = true;
        const queryString = this.buildQueryString()
        this.forms = await StudentAffairsOfficeService.getCommitteeOverview(queryString);
        this.loading = false;
      } catch (error) {
        console.error("Error retrieving open forms: ", error);
      }
    },

    buildQueryString() {
      const queryParams = {
        perPage: this.itemsPerPage,
        page: this.page - 1,
      }

      if (this.sortBy.length) {
        queryParams.sortBy = this.sortBy[0].key;
        queryParams.sortDirection = this.sortBy[0].order.toUpperCase();
      }

      return Object.entries(queryParams)
          .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
          .join("&");
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
        {title: this.$t("studentAffairsOfficeView.university"), key: "universityName"},
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

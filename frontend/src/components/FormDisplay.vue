<template>
  <v-card>
    <v-card-title>
      <u>{{ $t('studentAffairsOfficeView.openApplications') }}</u>
    </v-card-title>
    <v-data-table-server
        v-model:items-per-page="itemsPerPage"
        :items-per-page-options="itemsPerPageOptions"
        v-model:sort-by="sortBy"
        :headers="translatedHeaders"
        :items="formattedForms"
        :items-length="totalItems"
        :loading="loading"
        v-model:page="page"
        item-key="id"
        @update:options="getForms"
    >
      <template v-slot:[`item.actions`]="{ item }">
        <v-btn
            @click="openEditMenu(item)"
            icon="mdi-pencil"
        />
      </template>
    </v-data-table-server>
  </v-card>
</template>

<script>
import StudentAffairsOfficeService from "@/services/StudentAffairsOfficeService";

export default {
  data() {
    return {
      itemsPerPage: 5,
      itemsPerPageOptions: [
        {value: 5, title: "5"},
        {value: 10, title: "10"},
        {value: 25, title: "25"},
        {value: -1, title: "$vuetify.dataFooter.itemsPerPageAll"}
      ],
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
    async updateForm(){
      await this.getForms()
    },
    async openEditMenu(item) {
      try {
        const form = await StudentAffairsOfficeService.getApplication(item.applicationID);
        const role = this.$store.state.authentication.userRole;
        const component = role === 'ROLE_COMMITTEE' ? 'EditMenuCommittee' : role === 'ROLE_OFFICE' ? 'EditMenu' : null;
        if (component) {
          this.$emit('open', { component, form });
        }
      } catch (error) {
        console.error("Error retrieving form: ", error);
      }
    },

    async getForms() {
      try {
        this.loading = true;
        const queryString = this.buildQueryString()
        const response = await StudentAffairsOfficeService.getOverview(queryString);
        this.forms = response.content;
        this.totalItems = response.totalItems;
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

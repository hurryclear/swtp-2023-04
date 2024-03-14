<!-- UniversityForm.vue -->
<template>
  <v-expansion-panels>
    <v-expansion-panel>
      <v-expansion-panel-title>
        <template v-slot:default="{ expanded }">
          <v-row no-gutters>
            <v-col>
              {{ $t("applicationFormView.universityForm.previous") }}
            </v-col>
            <v-col class="text-grey">
              <v-fade-transition leave-absolute>
                <span v-if="expanded" key="0">
                  {{ $t("applicationFormView.universityForm.description") }}
                </span>
                <span v-else key="1">
                  {{ university.name }}
                </span>
              </v-fade-transition>
            </v-col>
          </v-row>
        </template>
      </v-expansion-panel-title>
      <v-expansion-panel-text>
        <v-combobox
            v-model="selectedUniversity"
            :items="universities"
            item-title="name"
            hide-details
            :label="$t('applicationFormView.universityForm.nameLabel')"
            variant="outlined"
            class="userInput"
        />
        <v-text-field
            v-model="university.country"
            hide-details
            :label="$t('applicationFormView.universityForm.countryLabel')"
            variant="outlined"
            class="userInput"
        />
        <!--TODO: i18n  -->
        <v-text-field
            v-model="university.website"
            hide-details
            :label="$t('applicationFormView.universityForm.websiteLabel')"
            variant="outlined"
            class="userInput"
        />
      </v-expansion-panel-text>
    </v-expansion-panel>
    <v-expansion-panel>
      <v-expansion-panel-title>
        <template v-slot:default="{ expanded }">
          <v-row no-gutters>
            <v-col>
              {{ $t("applicationFormView.courseOfStudy.courseOfStudy") }}
            </v-col>
            <v-col class="text-grey">
              <v-fade-transition leave-absolute>
                <span v-if="expanded" key="0">
                  {{ $t("applicationFormView.courseOfStudy.description") }}
                </span>
                <span v-else key="1">
                  {{ courseOfStudy.new }}
                </span>
              </v-fade-transition>
            </v-col>
          </v-row>
        </template>
      </v-expansion-panel-title>
      <v-expansion-panel-text>
        <v-text-field
            v-model="courseOfStudy.old"
            hide-details
            :label="$t('applicationFormView.courseOfStudy.previous')"
            variant="outlined"
            class="userInput"
        />
        <v-select
            v-model="courseOfStudy.new"
            hide-details
            :items="studyPlans"
            item-title="name"
            :label="$t('applicationFormView.courseOfStudy.new')"
            variant="outlined"
            class="userInput"
        />
      </v-expansion-panel-text>
    </v-expansion-panel>
  </v-expansion-panels>
</template>
<script>

export default {
  data() {
    return {
      university: {
        name: null,
        country: "",
        website: "",
      },
      courseOfStudy: {
        old: "",
        new: null,
      },
      selectedUniversity: null,
    }
  },
  computed: {
    universities() {
      return this.$store.state.university.universities;
    },
    studyPlans() {
      return this.$store.state.module.studyPlans
    }
  },
  watch: {
    university: {
      handler(newVal) {
        this.$store.commit('updateUniversityData', newVal)
      },
      deep: true,
    },
    courseOfStudy: {
      handler(newVal) {
        this.$store.commit('updateCourseOfStudyData', newVal)
      },
      deep: true,
    },
    selectedUniversity: function (newValue) {
      if (typeof newValue === 'string') {
        this.university.name = newValue
      } else {
        if (newValue == null) {
          this.university.name = '';
          return;
        }
        this.university.name = newValue.name
        this.university.country = newValue.country
        this.university.website = newValue.web_pages.toString()
      }
    },
    'courseOfStudy.new': function (newValue) {
      this.$store.dispatch('fetchModules', newValue);
    }
  },
  beforeCreate() {
    if (!this.$store.state.module.universities || !this.$store.state.module.universities.length) {
      // If universities data is not in the store, fetch it
      this.$store.dispatch('fetchUniversities');
    }
    if (!this.$store.state.module.studyPlans || !this.$store.state.module.studyPlans.length) {
      // If modules data is not in the store, fetch it
      this.$store.dispatch('fetchStudyPlans');
    }
  },
}
</script>

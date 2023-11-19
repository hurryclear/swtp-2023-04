<template>
  <v-expansion-panels>
    <v-expansion-panel>
      <v-expansion-panel-title>
        <template v-slot:default="{ expanded }">
          <v-row no-gutters>
            <v-col>
              {{ $t("universityForm.previousUniversity") }}
            </v-col>
            <v-col class="text-grey">
              <v-fade-transition leave-absolute>
                <span v-if="expanded" key="0">
                  {{ $t("universityForm.universityDescription") }}
                </span>
                <span v-else key="1">
                  {{ university.universityName }}
                </span>
              </v-fade-transition>
            </v-col>
          </v-row>
        </template>
      </v-expansion-panel-title>
      <v-expansion-panel-text>
        <v-text-field
            v-model="university.universityName"
            hide-details
            :label="$t('universityForm.universityNameLabel')"
            variant="outlined"
            class="userInput"
        />
        <v-text-field
            v-model="university.country"
            hide-details
            :label="$t('universityForm.countryLabel')"
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
              {{ $t("universityForm.previousStudyProgram") }}
            </v-col>
            <v-col class="text-grey">
              <v-fade-transition leave-absolute>
                <span v-if="expanded" key="0">
                  {{ $t("universityForm.studyProgramDescription") }}
                </span>
                <span v-else key="1">
                  {{ university.studyProgram }}
                </span>
              </v-fade-transition>
            </v-col>
          </v-row>
        </template>
      </v-expansion-panel-title>
      <v-expansion-panel-text>
        <v-text-field
            v-model="university.studyProgram"
            hide-details
            :label="$t('universityForm.studyProgramLabel')"
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
        universityName: '',
        studyProgram: '',
        country: ''
      }
    }
  }
}
</script>

<style scoped>
/* Add scoped styles if needed */
</style>

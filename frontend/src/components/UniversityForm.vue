<template>
  <v-expansion-panels>
    <v-expansion-panel>
      <v-expansion-panel-title>
        <template v-slot:default="{ expanded }">
          <v-row no-gutters>
            <v-col>
              Bisherige Universität
            </v-col>
            <v-col
                class="text-grey"
            >
              <v-fade-transition leave-absolute>
                <span
                    v-if="expanded"
                    key="0"
                >
                  Geben Sie den Namen Ihrer bisherigen Universität an!
                </span>
                <span
                    v-else
                    key="1"
                >
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
            label="Name der Universität"
            variant="outlined"
            class="userInput"
        />
        <v-text-field
            v-model="university.country"
            hide-details
            label="Land"
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
              Bisheriger Studiengang
            </v-col>
            <v-col
                class="text-grey"
            >
              <v-fade-transition leave-absolute>
                <span
                    v-if="expanded"
                    key="0"
                >
                  Geben Sie den Namen Ihres bisherigen Studiengangs an!
                </span>
                <span
                    v-else
                    key="1"
                >
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
            label="Name des Studiengangs"
            variant="outlined"
            class="userInput"
        />
      </v-expansion-panel-text>
    </v-expansion-panel>
  </v-expansion-panels>
</template>
<script>
export default {
  data: () => ({
    university: {
      universityName: '',
      studyProgram: '',
      country: ''
    }
  }),
}
</script>

<style scoped>
</style>

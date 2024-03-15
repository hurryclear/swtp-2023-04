<template>
  <v-container>
    <v-app-bar app class="custom-navbar">
      <!-- Logo -->
      <v-img :src="logoSrc" @click="navigateTo('/')"/>
      <v-spacer/>

      <!-- Hamburger menu for mobile view -->
      <v-btn class="d-md-none" variant="elevated" color="#262A31" icon="mdi-menu" @click="drawer = !drawer">
        <v-icon>mdi-menu</v-icon>
      </v-btn>

      <!-- Regular buttons for desktop view -->
      <div class="d-none d-md-flex">
        <v-btn class="button-spacing t-button"
               variant="elevated" color="#262A31"
               prepend-icon="mdi-home"
               @click="navigateTo('/')"
        >
          {{ $t("navBar.main") }}
        </v-btn>
        <v-btn
            v-if="this.userRole === 'ROLE_OFFICE' || this.userRole === 'ROLE_COMMITTEE'"
            class="button-spacing t-button"
            variant="elevated"
            color="#262A31"
            @click="navigateTo('/update-module-data')"
        >
          {{ $t("navBar.updateModuleData") }}
        </v-btn>
        <v-spacer/>
        <v-btn
            v-if="userRole==='ROLE_STUDENT'"
            class="button-spacing t-button"
            variant="elevated"
            color="#262A31"
            @click="navigateTo('/application-form')"
        >
          {{ $t("navBar.creditModules") }}
        </v-btn>
        <v-btn v-if="userRole==='ROLE_STUDENT'"
               class="button-spacing t-button"
               variant="elevated"
               color="#262A31"
               @click="navigateTo('/review-application')"
        >
          {{ $t("navBar.review") }}
        </v-btn>
        <v-btn
            v-if="userRole==='ROLE_OFFICE'"
            class="button-spacing t-button"
            variant="elevated"
            color="#262A31"
            @click="navigateTo('/student-affairs-office')"
        >
          {{ $t("navBar.studentAffairsOffice") }}
        </v-btn>
        <v-btn
            v-if="userRole==='ROLE_COMMITTEE'"
            class="button-spacing t-button"
            variant="elevated"
            color="#262A31"
            @click="navigateTo('/examining-committee-chair')"
        >
          {{ $t("navBar.examiningCommitteeChair") }}
        </v-btn>
        <LanguageSwitcher/>
        <DarkThemeToggle/>
        <v-btn class="button-spacing"
               variant="elevated"
               color="#262A31"
               icon="mdi-login-variant"
               @click="navigateTo('/login')"
        />
      </div>
    </v-app-bar>
    <v-navigation-drawer fluid temporary v-model="drawer" app>
      <v-list>
        <v-btn
            class="button-spacing"
            variant="text"
            icon="mdi-close"
            @click="drawer = !drawer"
        />
        <v-spacer/>
        <v-btn
            class="button-spacing t-button"
            variant="elevated"
            color="#262A31"
            prepend-icon="mdi-home"
            @click="navigateTo('/')"
        >
          {{ $t("navBar.main") }}
        </v-btn>
        <v-spacer/>
        <v-btn
            v-if="userRole==='ROLE_STUDENT'"
            class="button-spacing t-button"
            variant="elevated"
            color="#262A31"
            @click="navigateTo('/application-form')"
        >
          {{ $t("navBar.creditModules") }}
        </v-btn>
        <v-btn
            v-if="userRole==='ROLE_STUDENT'"
            class="button-spacing t-button"
            variant="elevated"
            color="#262A31"
            @click="navigateTo('/review-application')"
        >
          {{ $t("navBar.review") }}
        </v-btn>
        <v-spacer/>
        <v-btn
            v-if="userRole==='ROLE_OFFICE'"
            class="button-spacing t-button"
            variant="elevated"
            color="#262A31"
            @click="navigateTo('/student-affairs-office')"
        >
          {{ $t("navBar.studentAffairsOffice") }}
        </v-btn>
        <v-spacer/>
        <v-btn
            v-if="userRole==='ROLE_COMMITTEE'"
            class="button-spacing t-button"
            variant="elevated"
            color="#262A31"
            @click="navigateTo('/examining-committee-chair')"
        >
          {{ $t("navBar.examiningCommitteeChair") }}
        </v-btn>
        <v-spacer/>
        <v-container fluid>
          <v-row>
            <v-col cols="4">
              <DarkThemeToggle/>
            </v-col>
            <v-col cols="4">
              <LanguageSwitcher/>
            </v-col>
            <v-col cols="4">
              <v-btn
                  class="button-spacing"
                  variant="elevated"
                  color="#262A31"
                  icon="mdi-login-variant"
                  @click="navigateTo('/login')"
              />
            </v-col>
          </v-row>
        </v-container>
      </v-list>
    </v-navigation-drawer>
  </v-container>
</template>

<script>
import DarkThemeToggle from "@/components/DarkThemeToggle.vue";
import LanguageSwitcher from "@/components/LanguageSwitcher.vue";
import {computed} from 'vue';
import {useTheme} from 'vuetify';

export default {
  setup() {
    const theme = useTheme();
    const logoSrc = computed(() => {
      return theme.global.current.value.dark
          ? require('@/assets/logo-darkmode.png')
          : require('@/assets/logo-lightmode.png');
    });

    return {
      logoSrc,
    };
  },

  data() {
    return {
      drawer: false,
    };
  },

  components: {
    DarkThemeToggle,
    LanguageSwitcher,
  },
  computed: {
    userRole() {
      return this.$store.state.authentication.userRole; //TODO: fix error
    },
  },
  methods: {
    navigateTo(page) {
      this.$router.push(page);
    },
  },
};
</script>

<style scoped>
.custom-navbar::after {
  content: '';
  position: absolute;
  right: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
  background-color: #B02F2C;
  clip-path: polygon(47% 0, 100% 0, 100% 100%, 50% 100%);
  z-index: -1;
}

.button-spacing {
  margin: 8px;
}
.t-button {
  height:auto;
}

.d-md-none {
  display: inline-block;
  margin-left: 8px;
  margin-right: 8px;
}

.d-none.d-md-flex {
  display: none;
}
</style>

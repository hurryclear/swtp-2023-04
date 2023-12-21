<template>
  <v-container>
    <v-app-bar app class="custom-navbar">
      <!-- Logo -->
      <v-img :src="logoSrc" @click="navigateTo('main')"/>
      <v-spacer/>

      <!-- Hamburger menu for mobile view -->
      <v-btn class="d-md-none" variant="elevated" color="#262A31" icon="mdi-menu" @click="drawer = !drawer">
        <v-icon>mdi-menu</v-icon>
      </v-btn>

        <!-- Regular buttons for desktop view -->
        <div class="d-none d-md-flex">
          <v-btn class="button-spacing"
                 variant="elevated" color="#262A31"
                 prepend-icon="mdi-home"
                 @click="navigateTo('main')"
                 style="height: auto;"
          >
            {{ $t("navBar.main") }}
          </v-btn>
          <v-btn class="button-spacing"
                 variant="elevated"
                 color="#262A31"
                 @click="navigateTo('applicationForm')"
                 style="height: auto;"
          >
            {{ $t("navBar.creditModules") }}
          </v-btn>
          <v-btn class="button-spacing"
                 variant="elevated"
                 color="#262A31"
                 @click="navigateTo('reviewApplication')"
                 style="height: auto;"
          >
            {{ $t("navBar.review") }}
          </v-btn>
          <LanguageSwitcher/>
          <DarkThemeToggle/>
          <v-btn class="button-spacing"
                 variant="elevated"
                 color="#262A31"
                 icon="mdi-login-variant"
                 @click="navigateTo('login')"
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
        <!-- TODO: width:calc(100% - 1rem) ist erstmal ne Übergangslösung -->
        <v-btn
            class="button-spacing"
            variant="elevated"
            color="#262A31"
            prepend-icon="mdi-home"
            @click="navigateTo('main')"
            style="width:calc(100% - 1rem)"
        >
          {{ $t("navBar.main") }}
        </v-btn>
        <v-spacer/>
        <v-btn
            class="button-spacing"
            variant="elevated"
            color="#262A31"
            @click="navigateTo('applicationForm')"
            style="width:calc(100% - 1rem)"
        >
          {{ $t("navBar.creditModules") }}
        </v-btn>
        <v-spacer/>
        <v-btn
            class="button-spacing"
            variant="elevated"
            color="#262A31"
            @click="navigateTo('reviewApplication')"
            style="width:calc(100% - 1rem)"
        >
          {{ $t("navBar.review") }}
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
                  @click="navigateTo('login')"
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

  methods: {
    navigateTo(page) {
      switch (page) {
        case 'main':
          this.$router.push('/');
          break;
        case 'applicationForm':
          this.$router.push('/application-form');
          break;
        case 'reviewApplication':
          this.$router.push('/review-application');
          break;
        case 'login':
          this.$router.push('/login');
          break;
      }
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

.d-md-none {
  display: inline-block;
  margin-left: 8px;
  margin-right: 8px;
}

.d-none.d-md-flex {
  display: none;
}
</style>

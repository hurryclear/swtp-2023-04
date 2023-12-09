<template>
  <v-container>
    <v-app-bar app class="custom-navbar">
      <div class="navbar-content">
        <v-img :src="logoSrc" height="100%" contain class="navbar-logo"></v-img>
        <v-spacer></v-spacer>

        <!-- Hamburger menu for mobile view -->
        <v-btn class="d-sm-none" variant="elevated" color="#262A31" icon="mdi-menu" @click="drawer = !drawer">
          <v-icon>mdi-menu</v-icon>
        </v-btn>

        <!-- Regular buttons for desktop view -->
        <div class="d-none d-sm-flex">
          <v-btn class="button-spacing" variant="elevated" color="#262A31" prepend-icon="mdi-home" @click="navigateTo('main')">{{ $t("navBar.main") }}</v-btn>
          <v-btn class="button-spacing" variant="elevated" color="#262A31" @click="navigateTo('applicationForm')">{{$t("navBar.creditModules")}}</v-btn>
          <v-btn class="button-spacing" variant="elevated" color="#262A31" @click="navigateTo('reviewApplication')">{{$t("navBar.review")}}</v-btn>
          <LanguageSwitcher />
          <DarkThemeToggle />
          <v-btn class="button-spacing" variant="elevated" color="#262A31" icon="mdi-login-variant" @click="navigateTo('login')"></v-btn>
        </div>
      </div>
    </v-app-bar>

    <v-navigation-drawer fluid v-model="drawer" app>
      <v-list>
        <v-spacer> <v-btn class="button-spacing" variant="text" icon="mdi-close" @click="drawer = !drawer"></v-btn></v-spacer>
        <v-spacer> <v-btn class="button-spacing" variant="elevated" color="#262A31" prepend-icon="mdi-home" @click="navigateTo('main')">{{ $t("navBar.main") }}</v-btn></v-spacer>
        <v-spacer><v-btn class="button-spacing" variant="elevated" color="#262A31" @click="navigateTo('applicationForm')">{{$t("navBar.creditModules")}}</v-btn></v-spacer>
        <v-spacer> <v-btn class="button-spacing" variant="elevated" color="#262A31" @click="navigateTo('reviewApplication')">{{$t("navBar.review")}}</v-btn></v-spacer>
        <DarkThemeToggle />
        <LanguageSwitcher />
        <v-btn class="button-spacing" variant="elevated" color="#262A31" icon="mdi-login-variant" @click="navigateTo('login')"></v-btn>
      </v-list> 
    </v-navigation-drawer>
  </v-container>
</template>

<script>
import DarkThemeToggle from "@/components/DarkThemeToggle.vue";
import LanguageSwitcher from "@/components/LanguageSwitcher.vue";
import { computed } from 'vue';
import { useTheme } from 'vuetify';

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
.custom-navbar {
  background-color: var(--v-theme-dark);
}

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

.navbar-logo {
  max-height: 120px;
  max-width: 240px;
}

.button-spacing {
  margin-left: 8px; 
  margin-right: 8px; 
  margin-top: 8px;
  margin-bottom: 8px;
}


.navbar-content {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
}


  .d-sm-none {
    display: inline-block;
    margin-left: 8px; 
    margin-right: 8px; 
  }

  .d-none.d-sm-flex {
    display: none;
  }

</style>

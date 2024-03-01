# Vue.js, Vuetify, i18n, and Vue Router Tutorial

---

## Table of Contents

1. Introduction to Vue.js
2. Getting Started with Vuetify
3. Internationalization (i18n) in Vue.js
4. Implementing Vue Router 
5. Making HTTP Requests with Axios
6. Project Structure Overview

---

## 1. Introduction to Vue.js

### What is Vue.js?

Vue.js is a progressive JavaScript framework that is designed for building user interfaces. Developed by Evan You, Vue.js focuses on the view layer of your application, making it easy to integrate with other libraries and existing projects. One of Vue.js' key strengths is its simplicity, allowing developers to adopt it incrementally. Vue.js provides declarative rendering using a straightforward template syntax, making it accessible to both beginners and experienced developers.

Key Features of Vue.js:
- Reactive Data Binding: Vue.js uses a reactive data-binding mechanism, which means that changes to the data automatically update the view, and vice versa.

- Components: Vue.js allows you to create modular and reusable components, promoting a component-based architecture.

- Directives: Vue.js provides built-in directives that offer special behavior to HTML elements. Examples include v-if, v-for, and v-model.

- Vue CLI: The Vue Command Line Interface (CLI) provides a robust development environment with features like project scaffolding, development server, and build tools.

- Community and Ecosystem: Vue.js has a growing and active community, and it is supported by a rich ecosystem of libraries and tools.

---

### Vue Component anatomy

Vue Components are the building blocks of Vue Applications. They are structured as following:

```vue 
<template>
  <!-- HTML elements and other Vue directives --> 
</template>

<script>
  <!-- Imports, Exports, Methods, Lifesycle Hooks and Configs -->
</script>

<style>
  /* CSS or other styling*/
</style>
```

---

#### Templates

The template section is where you define the structure of the component's HTML using Vue's template syntax. It specifies how the component will render in the DOM

```vue
<template>
  <h1>
    This is a header.
  </h1>
  <MyComponent1>
    This is a Vue Component
  </MyComponent1>
  <MyComponent2
      label="This is also a component"
  />
</template>
```
---
#### Scripts
The Script section is where you write the JavaScript code for the component. It includes the component's data, methods, lifecycle hooks, and other configurations.
```javascript
import MyComponent1 from '@/components/MyComponent1.vue';
import MyComponent2 from '@/components/MyComponent2.vue';

export default {
  components: {
    MyComponent1, MyComponent2
    // Other components used in this view
  },
  // Other configurations and lifecycle hooks for the view
};
```
<u>Lifecycle hooks:</u>

1. beforeCreate:
   - Called synchronously after the instance has just been created but before data observation and event/watcher setup.
2. created:
   - Called synchronously after the instance is created. At this stage, the component has finished processing options and has set up data observation, computed properties, methods, and watchers.
3. beforeMount:
   - Called right before the mounting begins. The render function is about to be called for the first time.
4. mounted:
   - Called after the component has been mounted to the DOM. This is often where you perform initial data fetching and set up event listeners.
5. beforeUpdate:
   - Called when the data changes, but before the DOM is patched.
6. updated:
   - Called after a data change causes the virtual DOM to be re-rendered and patched.
7. beforeDestroy:
   - Called right before a component is destroyed. This is where you can clean up event listeners or other resources.
8. destroyed:
   - Called after a component has been destroyed. At this point, all directives and child components have been unbound.


<u>Configurations:</u>
1. data:
   - A function that returns the initial data for the component. It should return an object.
```javascript
data() {
  return {
    message: 'Hello, Vue!',
  };
}
```
2. props:
   - An array or object specifying the props that a component can receive from its parent.
```javascript
props: ['title', 'isVisible'],
```   
3. methods:
   - An object containing methods that can be called within the component.
```javascript
methods: {
   greet() {
      console.log('Greetings!');
   },
}
```
4. computed:
   - An object containing computed properties. Computed properties are cached based on their dependencies and only re-evaluate when those dependencies change.
```javascript
computed: {
   fullName() {
      return `${this.firstName} ${this.lastName}`;
   },
}
```
5. watch:
   - An object where keys are the expressions to watch, and values are the corresponding callback functions to execute when the watched property changes.
```javascript
watch: {
   'user.name': function(newName, oldName) {
      console.log(`User's name changed from ${oldName} to ${newName}`);
   },
}
```
---
#### Styles
The style section allows you to define the component's styles using CSS. You can use regular CSS or preprocessors like Sass or Less.
```css
<style>
  div {
    background-color: #f0f0f0;
    padding: 20px;
    border-radius: 5px;
  }

  h1 {
    color: #42b983;
  }
  /* Other styles specific to the component */
</style>
```
You can the scoped attribute to the style section to scope its influence to this Component.

---
## 2. Getting Started with Vuetify

### What is Vuetify?

Vuetify is a Material Design component framework for Vue.js. It brings the aesthetic and functional principles of Google's Material Design to Vue applications, offering a set of pre-built components that follow the Material Design guidelines. Vuetify simplifies the process of building attractive and responsive user interfaces with Vue.js.

---
### Key Features of Vuetify
- Material Design Components: Vuetify provides a wide range of Material Design components, including buttons, cards, navigation drawers, and more.

- Theming: Vuetify allows easy customization and theming to match the look and feel of your application. You can customize the color scheme, typography, and other design aspects.

- Responsive Layout: Vuetify components are designed to be responsive, ensuring a consistent user experience across various devices and screen sizes.

- Grid System: The grid system in Vuetify enables you to create complex layouts with a responsive grid, making it easy to design flexible and dynamic interfaces.

---

### Installing Vuetify

```bash
### Install Vuetify
npm install vuetify
```

To use Vuetify in your Vue.js project, you need to import and configure it. Check the [official Vuetify documentation](https://vuetifyjs.com/) for detailed setup instructions.

---
## 3. Internationalization (i18n) in Vue.js

### What is i18n?
Internationalization (i18n) is the process of designing and preparing your application to be adaptable to different languages and regions. In the context of Vue.js, the vue-i18n library simplifies the implementation of i18n features in your application.

---

### Key Features of i18n in Vue.js
- Translation Management: vue-i18n allows you to manage translations easily, enabling your application to support multiple languages.

- Pluralization: It supports pluralization rules for different languages, ensuring correct handling of singular and plural forms in your application.

- Formatting: vue-i18n supports formatting for dates, numbers, and other data types according to the user's locale.

- Component Integration: Seamless integration with Vue components makes it straightforward to implement translations in your templates and scripts.

---

### Installing i18n

```bash
# Install vue-i18n
npm install vue-i18n
```

To use vue-i18n in your Vue.js project, you need to set up and configure the i18n instance. Check the [official Vue I18n documentation](https://kazupon.github.io/vue-i18n/) for detailed instructions.

---

## 4. Implementing Vue Router

### What is Vue Router?
Vue Router is the official routing library for Vue.js. It enables navigation between different views (components) in your application, allowing you to build single-page applications with dynamic content.

---

### Key Features of Vue Router
- Declarative Routing: Vue Router adopts a declarative approach to routing, where you define your routes in a configuration and link them directly in your templates.

- Nested Routes: Vue Router supports nested routes, allowing you to create complex page layouts with multiple nested components.

- Dynamic Route Matching: You can define dynamic segments in your routes, enabling dynamic content and parameterized views.

- Navigation Guards: Vue Router provides navigation guards that allow you to control the navigation flow, securing routes and executing functions before entering or leaving a route.

---

### Installing Vue Router

```bash
# Install vue-router
npm install vue-router
```

To use Vue Router in your Vue.js project, you need to configure your routes and integrate them into your application. Check the [official Vue Router documentation](https://router.vuejs.org/) for detailed setup instructions.

---

## 5. Making HTTP Requests with Axios

### What is Axios?

Axios is a popular JavaScript library used for making HTTP requests. It works both in the browser and Node.js environments and is commonly used with Vue.js to handle data fetching from APIs.

---

### Key Features of Axios
- Promise-Based: Axios uses promises, making it easy to work with asynchronous code and handle responses.
- Interceptors: Axios provides interceptors that allow you to run your code or modify the request or response before the request is sent or after the response is received.
- Cancel Requests: Axios allows you to cancel requests, which is useful for managing multiple asynchronous operations. 

---

### Installing Axios

```bash
# Install axios
npm install axios
```

To use Axios in your Vue.js project, you need to import it and make use of it in your components or services.

---

## 6. Project Structure Overview
### Introduction
This project follows a standard directory structure commonly used in Vue.js applications. This structure helps organize the codebase, making it modular, maintainable, and scalable. Here's a brief overview of the key folders and files:

---

### Root

- [App.vue](./src/App.vue): The root component of the Vue.js application, serving as the entry point for the entire project.

- [main.js](./src/main.js): The entry file where the Vue instance is initialized, and other configurations, such as global components and plugins, are set up.

---

### Assets
- [main.css](./src/assets/main.css): The main stylesheet for the application.

- [module_liste.json](./src/assets/module_liste.json): A JSON file containing data, possibly related to modules or any other structured information.

---

### Components
This directory houses various Vue.js components, each serving a specific purpose in the application. Examples include:

- [DarkThemeToggle.vue](./src/components)
- [LanguageSwitcher.vue](./src/components/LanguageSwitcher.vue)
- [ModuleForm.vue](./src/components/ModuleForm.vue)
- [ModuleFormList.vue](./src/components/ModuleFormList.vue)
- [NavigationMenu.vue](./src/components/NavigationMenu.vue)
- [UniversityForm.vue](./src/components/UniversityForm.vue)

---

### Locale
Contains localization files for different languages, facilitating internationalization (i18n). Examples include:

- [de.json](./src/locale/de.json)
- [en.json](./src/locale/en.json)
- [es.json](./src/locale/es.json)
- [fr.json](./src/locale/fr.json)
- [it.json](./src/locale/it.json)
- [ja.json](./src/locale/ja.json)
- [ko.json](./src/locale/ko.json)
- [pt.json](./src/locale/pt.json)
- [ru.json](./src/locale/ru.json)
- [zh.json](./src/locale/zh.json)

---

### Plugins
This directory includes configuration files for various plugins used in the project. Examples include:

- [axios.js](./src/plugins/axios.js): Configuration for the Axios HTTP client.
- [i18n.js](./src/plugins/i18n.js): Configuration for Vue-i18n, supporting internationalization.
- [vuetify.js](./src/plugins/vuetify.js): Configuration for Vuetify, a Vue.js framework for Material Design.

---

### Router
- [index.js](./src/router/index.js): Vue Router configuration defining the routes for different views in the application.

---

### Scripts
Includes script files, possibly for utility functions or testing. Examples include:

- [sum.js](./src/scripts/sum.js): A script for adding two numbers.
- [sum.test.js](./src/scripts/sum.test.js): Test file for the sum.js script, using a testing framework like Jest.

---

### Views
This directory contains Vue.js view components representing different pages or views in the application. Examples include:

- [ApplicationForm.vue](./src/views/ApplicationFormView.vue)
- [HomeView.vue](./src/views/HomeView.vue)
- [LoginView.vue](./src/views/LoginView.vue)
- [ReviewView.vue](./src/views/ReviewView.vue)

---
## Conclusion 

Congratulations! You've learned the basics of Vue.js, Vuetify, i18n, and Vue Router. Now, explore further and build amazing Vue.js applications!

---

## Resources

- [Vue.js Documentation](https://vuejs.org/)
- [Vuetify Documentation](https://vuetifyjs.com/)
- [Vue I18n Documentation](https://kazupon.github.io/vue-i18n/)
- [Vue Router Documentation](https://router.vuejs.org/)
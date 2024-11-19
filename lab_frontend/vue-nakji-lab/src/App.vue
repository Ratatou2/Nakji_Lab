<template>
  <div>
    <!-- <img class="Vue-logo" src="./assets/logo.png"> -->
  </div>
  <div>
    <h1>Nakji World</h1>
  </div>
  <div>
    <img class="mangom-logo" src="./assets/img/mangom_ggozil.png" style="width: 15%" />
  </div>
  <div class="main">
    <!-- <img class="mangom-cute" src="./assets/img/mangom_typing.png" style="width: 30%" /> -->
    <YoutubeDownlaod></YoutubeDownlaod>
  </div>
  <div>
    <!-- <HelloWorld msg="Welcome to Your Vue.js App" /> -->
  </div>
</template>

<script>
// import HelloWorld from "./components/HelloWorld.vue";
import YoutubeDownlaod from "./components/YoutubeDownlaod.vue";

export default {
  name: "App",
  components: {
    // HelloWorld,
    YoutubeDownlaod,
  },
};
</script>

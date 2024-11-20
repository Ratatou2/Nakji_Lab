<<<<<<< HEAD
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
import Swal from "sweetalert2";

const Toast = Swal.mixin({
  toast: true,
  position: "top-end",
  showConfirmButton: false,
  timer: 3000,
  timerProgressBar: true,
  didOpen: (toast) => {
    toast.addEventListener("mouseenter", Swal.stopTimer);
    toast.addEventListener("mouseleave", Swal.resumeTimer);
  },
});

Toast.fire({
  icon: "error",
  title: "The current password is incorrect",
});

export default {
  name: "App",
  components: {
    // HelloWorld,
    YoutubeDownlaod,
  },
};
</script>

<style>
/* Main css */
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

.mangom-logo {
  width: 24%;
  margin: -5px 12px 12px 12px;
}

.mangom-cute {
  width: 40%;
  height: 40%;
  margin: 0 30px 0 0;
}

.Vue-logo {
  width: 2%;
  height: 2%;
}

.main {
  display: flex; /* Flexbox layout을 사용 */
  flex-direction: row; /* 요소들을 가로 방향으로 정렬 */
  align-items: center; /* 세로 방향 가운데 정렬 */
  justify-content: center; /* 가로 방향 가운데 정렬 */
=======
<script setup>
import HelloWorld from "./components/HelloWorld.vue";
import TheWelcome from "./components/TheWelcome.vue";
import YoutubeDownlaod from "./components/YoutubeDownlaod.vue";
import Toast from "@/util/sweetalert2";

// 함수 정의
const showAlert = () => {
  Toast.fire({
    icon: "info",
    title: `TestTestTest`,
  });
};
</script>

<template>
  <header>
    <img alt="Vue logo" class="logo" src="./assets/logo.svg" width="125" height="125" />

    <div class="wrapper">
      <HelloWorld msg="You did it!" />
    </div>
  </header>

  <main>
    <TheWelcome />
  </main>
  <button @click="showAlert">눌러</button>
  <YoutubeDownlaod></YoutubeDownlaod>
</template>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
>>>>>>> origin/frontend
}
</style>

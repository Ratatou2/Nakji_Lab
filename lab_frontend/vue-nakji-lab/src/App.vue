<script setup>
import HelloWorld from "./components/HelloWorld.vue";
import TheWelcome from "./components/TheWelcome.vue";
import Toast from "@/util/sweetalert2";

// 알림 함수
const successAlert = () => {
  Toast.fire({
    icon: "success",
    title: `success`,
  });
};

const errorAlert = () => {
  Toast.fire({
    icon: "error",
    title: `error`,
  });
};

const warningAlert = () => {
  Toast.fire({
    icon: "warning",
    title: `warning`,
  });
};

const infoAlert = () => {
  Toast.fire({
    icon: "info",
    title: `info`,
  });
};

const questionAlert = () => {
  Toast.fire({
    icon: "question",
    title: `question`,
  });
};
</script>

<template>
  <header>
    <img alt="Vue logo" class="logo" src="./assets/logo.svg" width="125" height="125" />

    <div class="wrapper">
      <HelloWorld msg="Nakji World!!" />
    </div>
  </header>

  <main>
    <TheWelcome />
    <button @click="successAlert">success</button>
    <button @click="errorAlert">error</button>
    <button @click="warningAlert">warning</button>
    <button @click="infoAlert">info</button>
    <button @click="questionAlert">question</button>
  </main>
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
}
</style>

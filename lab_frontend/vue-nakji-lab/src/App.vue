<template>
  <div>
    <!-- <img class="Vue-logo" src="./assets/logo.png"> -->
  </div>
  <div>
    <h1>Nakji World</h1>
  </div>
  <div>
    <img class="mangom-logo" src="./assets/img/mangom_ggozil.png">
  </div>
  <div class="main">
    <img class="mangom-cute" src="./assets/img/mangom_typing.png">
    <HelloWorld msg="Welcome to Your Vue.js App"/>
  </div>
  
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'

export default {
  name: 'App',
  components: {
    HelloWorld
  }
}
</script>

<style>
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
  margin: 0 30px 0 0 ;
}

.Vue-logo {
  width: 2%;
  height: 2%;
}

.main {
  display: flex;          /* Flexbox layout을 사용 */
  flex-direction: row;    /* 요소들을 가로 방향으로 정렬 */
  align-items: center;    /* 세로 방향 가운데 정렬 */
  justify-content: center; /* 가로 방향 가운데 정렬 */
}
</style>

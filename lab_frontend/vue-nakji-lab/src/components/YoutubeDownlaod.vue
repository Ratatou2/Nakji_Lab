<template>
  <div>
    <button @click="showAlert">눌러</button>
  </div>
  <div>
    <div>
      <div>유튜브 링크: <input type="text" v-model="url" /></div>
      <div>가수: <input type="text" v-model="artist" /></div>
      <div>제목: <input type="text" v-model="songTitle" /></div>
    </div>
    <button @click="downloadYoutube">유튜브 다운로드</button>
  </div>
</template>

<script>
import Toast from "../util/sweetalert2";

export default {
  data() {
    return {
      url: "https://youtu.be/8Ebqe2Dbzls",
      artist: "ROSE & Bruno Mars",
      songTitle: "APT",
    };
  },
  methods: {
    showAlert() {
      Toast.fire({
        icon: "info",
        title: `TestTestTest`,
      });
    },

    async downloadYoutube() {
      try {
        const response = await fetch(
          `${
            process.env.VUE_APP_BACKEND_URL || "http://localhost:8080"
          }/api/youtube/download`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              artist: this.artist,
              songTitle: this.songTitle,
              url: this.url,
            }),
          }
        );

        if (!response.ok) {
          throw new Error("Network response was not ok");
        }

        const data = await response.json();

        if (data.success) {
          Toast.fire({
            icon: "success",
            title: `[Success] \n ${data.artist} - ${data.songTitle}`,
          });
        } else {
          Toast.fire({
            icon: "error",
            title: `[Fail] \n ${data.artist} - ${data.songTitle}`,
          });
        }
      } catch (error) {
        console.error(
          "There has been a problem with your fetch operation:",
          error
        );
      }
    },
  },
};
</script>

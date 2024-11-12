<template>
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
export default {
  data() {
    return {
      url: "https://youtu.be/8Ebqe2Dbzls",
      artist: "ROSE & Bruno Mars",
      songTitle: "APT",
    };
  },
  methods: {
    async downloadYoutube() {
      try {
        // 여기서 백엔드 URL을 프로시로 설정된 URL로 호출
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
        console.log(data); // 백엔드에서 보낸 응답 처리
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

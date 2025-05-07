<script setup>
import { ref } from "vue";
import Toast from "@/util/sweetalert2";

// 반응형 변수 선언
const url = ref("https://youtu.be/8Ebqe2Dbzls");
const artist = ref("ROSE & Bruno Mars");
const songTitle = ref("APT");

// 유튜브 다운로드 함수
const downloadYoutube = async () => {
  try {
    const response = await fetch(`${import.meta.env.VITE_BACKEND_URL || "http://localhost:8080"}/api/youtube/download`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        artist: artist.value,
        songTitle: songTitle.value,
        url: url.value,
      }),
    });

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
    Toast.fire({
      icon: "question",
      title: `[Fail] \n ${data.artist} - ${data.songTitle}`,
    });
    console.error("There has been a problem with your fetch operation:", error);
  }
};
</script>

<template>
  <div class="form-container">
    <div class="form-group">
      <label for="url">유튜브 링크</label>
      <input id="url" type="text" v-model="url" class="input-field" />
    </div>
    <div class="form-group">
      <label for="artist">가수</label>
      <input id="artist" type="text" v-model="artist" class="input-field" />
    </div>
    <div class="form-group">
      <label for="songTitle">제목</label>
      <input id="songTitle" type="text" v-model="songTitle" class="input-field" />
    </div>
    <button @click="downloadYoutube" class="download-button">유튜브 다운로드</button>
  </div>
</template>

<style>
.form-container {
  max-width: 500px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 15px; /* 항목 간 간격 */
}

.form-group {
  display: flex;
  align-items: center; /* 세로 정렬 */
  justify-content: flex-start; /* 가로 정렬 */
}

input {
  background-color: #eee;
  border: none;
  padding: 12px 15px;
  margin: 8px 0;
  width: 100%;
  height: 100%;
  border-radius: 4px; /* 라운드 처리 */
}

label {
  display: inline-block;
  text-align: center;
  font-weight: bold;
  width: 150px; /* 라벨의 고정 너비 */
}

.download-button {
  padding: 10px 20px; /* 텍스트 주변의 여백 설정 */
  background-color: #0077ff;
  color: white;
  border: none;
  cursor: pointer;
  text-align: center;
  border-radius: 4px; /* 라운드 처리 */
  display: inline-block; /* 텍스트에 맞춘 크기 */
  max-width: 200px; /* 최대 너비 설정 */
  margin: 0 auto; /* 가운데 정렬 */
}

.download-button:hover {
  background-color: #5baaff;
}
</style>

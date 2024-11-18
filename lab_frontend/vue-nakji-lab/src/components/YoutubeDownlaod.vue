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
import Swal from "sweetalert2";

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

        const Toast = Swal.mixin({
          toast: true,
          position: "top-end",
          iconColor: "white",
          customClass: {
            popup: "colored-toast",
          },
          showConfirmButton: false,
          timer: 4500,
          timerProgressBar: true,
          didOpen: (toast) => {
            toast.addEventListener("mouseenter", Swal.stopTimer);
            toast.addEventListener("mouseleave", Swal.resumeTimer);
          },
        });

        if (data.success) {
          Toast.fire({
            icon: "success",
            title: `다운로드 성공: ${data.artist} - ${data.songTitle}`,
          });
        } else {
          Toast.fire({
            icon: "error",
            title: `다운로드 실패: ${data.artist} - ${data.songTitle}`,
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

<style scoped>
/* SweetAlert2 Toast Styling */
.colored-toast.swal2-icon-success {
  background-color: #a5dc86 !important;
}

.colored-toast.swal2-icon-error {
  background-color: #f27474 !important;
}

.colored-toast.swal2-icon-warning {
  background-color: #f8bb86 !important;
}

.colored-toast.swal2-icon-info {
  background-color: #3fc3ee !important;
}

.colored-toast.swal2-icon-question {
  background-color: #87adbd !important;
}

.colored-toast .swal2-title {
  color: white;
}

.colored-toast .swal2-close {
  color: white;
}

.colored-toast .swal2-html-container {
  color: white;
}

/* Form and Button Styling */
.container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 15px;
}

.label {
  font-weight: bold;
}

.input-field {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.download-button {
  width: 100%;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.download-button:hover {
  background-color: #0056b3;
}
</style>

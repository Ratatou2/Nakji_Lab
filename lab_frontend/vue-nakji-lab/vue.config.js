<<<<<<< HEAD
const { defineConfig } = require("@vue/cli-service");

module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: "../../lab_backend/src/main/resources/static/", // 빌드된 파일을 static에 저장
  devServer: {
    port: 8081, // Vue 개발 서버 포트
    proxy: {
      "/api": {
        target: "http://babywombat.zapto.org:18260/", // 백엔드 서버의 실제 주소
        changeOrigin: true,
        pathRewrite: {
          "^/api": "", // 프록시 경로 재작성
        },
      },
    },
  },
});
=======
const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})
>>>>>>> f3cf5f98e8279c01e5fe4da26ffc4e2e9e2b6376

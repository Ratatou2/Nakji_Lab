const { defineConfig } = require("@vue/cli-service");

module.exports = defineConfig({
  publicPath: "",
  transpileDependencies: true,
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

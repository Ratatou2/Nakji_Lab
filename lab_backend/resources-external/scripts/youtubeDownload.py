import os
import yt_dlp
# import logging
import argparse

# # resources/logs 디렉토리 체크
# log_dir = os.path.join('./logs')
# if not os.path.exists(log_dir):
#     os.makedirs(log_dir)
#
# # resources/mp3file 디렉토리 체크
# output_dir = os.path.join('./resources-external/mp3file')
# if not os.path.exists(output_dir):
#     os.makedirs(output_dir)

# # 로그 설정 (UTF-8 없으면 한글 깨짐)
# logging.basicConfig(
#     filename=os.path.join(log_dir, 'youtube_download.log'),
#     level=logging.INFO,
#     encoding='utf-8'  # UTF-8 인코딩 설정
# )

def download_audio(url, ffmpeg_path, singer, songName, mp3_dir):
    fileName = f"{singer}-{songName}"

    ydl_opts = {
        'format': 'bestaudio/best',
        'outtmpl': os.path.join(mp3_dir, fileName + '.%(ext)s'),  # mp3file 디렉토리에 저장
        'postprocessors': [{
            'key': 'FFmpegExtractAudio',
            'preferredcodec': 'mp3',
            'preferredquality': '320',
        }],
        'ffmpeg_location': ffmpeg_path  # FFmpeg 경로를 직접 사용
    }

#     logging.info(f'[SYSTEM] download for: [info][{singer}-{songName}][{url}]')
    try:
        with yt_dlp.YoutubeDL(ydl_opts) as ydl:
            ydl.download([url])
#             logging.info(f'[Complete][{singer}-{songName}]')
    except Exception as e:
        print(f"[Error][yt_dlp Fail]: {e}")
#         logging.error(f"[Error][yt_dlp Fail][{e}]")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='YouTube에서 오디오 다운로드.')
    parser.add_argument('url', help='YouTube 비디오의 URL')
    parser.add_argument('--ffmpeg-location', required=True, help='FFmpeg 실행 파일의 경로')
    parser.add_argument('singer', help='가수 이름')
    parser.add_argument('songName', help='노래 제목')
    parser.add_argument('mp3_dir', help='mp3 저장 경로')

    args = parser.parse_args()
    download_audio(args.url, args.ffmpeg_location, args.singer, args.songName, args.mp3_dir)

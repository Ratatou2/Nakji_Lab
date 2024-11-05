import os
import yt_dlp
import logging
import argparse

# resources/logs 디렉토리 체크
log_dir = os.path.join('src\\main\\resources\\logs')
if not os.path.exists(log_dir):
    os.makedirs(log_dir)

# resources/mp3file 디렉토리 체크
output_dir = os.path.join('src\\main\\resources\\mp3file')
if not os.path.exists(output_dir):
    os.makedirs(output_dir)

# 로그 설정
logging.basicConfig(filename=os.path.join(log_dir, 'youtubeDownload.log'), level=logging.INFO)

def download_audio(url, ffmpeg_path, singer, songName):
    fileName = f"{singer}-{songName}"

    ydl_opts = {
        'format': 'bestaudio/best',
        'outtmpl': os.path.join(output_dir, fileName + '.%(ext)s'),  # mp3file 디렉토리에 저장
        'postprocessors': [{
            'key': 'FFmpegExtractAudio',
            'preferredcodec': 'mp3',
            'preferredquality': '320',
        }],
        'ffmpeg_location': ffmpeg_path  # FFmpeg 경로를 직접 사용
    }

    logging.info(f'Starting download for: {url}')
    try:
        with yt_dlp.YoutubeDL(ydl_opts) as ydl:
            ydl.download([url])
            logging.info(f'Completed download for: {url}')
    except Exception as e:
        logging.error(f"An error occurred: {e}")
        print(f"An error occurred: {e}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='YouTube에서 오디오 다운로드.')
    parser.add_argument('url', help='YouTube 비디오의 URL')
    parser.add_argument('--ffmpeg-location', required=True, help='FFmpeg 실행 파일의 경로')
    parser.add_argument('singer', help='가수 이름')
    parser.add_argument('songName', help='노래 제목')

    args = parser.parse_args()
    download_audio(args.url, args.ffmpeg_location, args.singer, args.songName)

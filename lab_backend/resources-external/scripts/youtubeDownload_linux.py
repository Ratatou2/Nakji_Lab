import os
import yt_dlp
# import logging
import argparse


def download_audio(url, singer, songName, mp3_dir):
    fileName = f"{singer}-{songName}"
    ydl_opts = {
        'format': 'bestaudio/best',
        'outtmpl': os.path.join(mp3_dir, fileName + '.%(ext)s'),  # mp3file 디렉토리에 저장
        'postprocessors': [{
            'key': 'FFmpegExtractAudio',
            'preferredcodec': 'mp3',
            'preferredquality': '320',
        }],
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
    parser.add_argument('singer', help='가수 이름')
    parser.add_argument('songName', help='노래 제목')
    parser.add_argument('mp3_dir', help='mp3 저장 경로')

    args = parser.parse_args()
    download_audio(args.url, args.singer, args.songName, args.mp3_dir)

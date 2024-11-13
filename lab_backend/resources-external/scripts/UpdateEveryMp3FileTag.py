import requests
from bs4 import BeautifulSoup
from io import BytesIO
from mutagen.id3 import ID3, APIC, TIT2, TPE1, TALB, USLT
from PIL import Image
import os
import glob
import sys
import argparse
# import logging

# # 로그 설정 (UTF-8 없으면 한글 깨짐)
# log_dir = os.path.join('../logs')
# if not os.path.exists(log_dir):
#     os.makedirs(log_dir)
#
# logging.basicConfig(
#     filename=os.path.join(log_dir, 'Update_mp3_file.log'),
#     level=logging.INFO,
#     encoding='utf-8'  # UTF-8 인코딩 설정
# )

global_final_artist_name = ""
global_final_song_title = ""

# 콘솔 출력 인코딩 설정 (UTF-8)
if sys.stdout.encoding != 'UTF-8':
    import io
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

def edit_mp3_file(query, file_link):
#     logging.info(f'[SYSTEM] Starting Edit mp3 file')
    # 벅스 검색 URL
    url = f"https://music.bugs.co.kr/search/integrated?q={query}"

    # 검색 결과에서 첫 번째 노래 페이지로 이동
    response = requests.get(url)
    soup = BeautifulSoup(response.content, "html.parser")

    song_url = soup.find("a", {"class": "trackInfo"}).get('href')
    song_response = requests.get(song_url)

    # 앨범 제목, 아티스트, 노래 제목 가져오기
    soup = BeautifulSoup(song_response.content, "html.parser")

    artist = soup.select('#container > section.sectionPadding.summaryInfo.summaryTrack > div > div.basicInfo > table > tbody > tr:nth-child(1) > td > a')
    song = soup.select('#container > header > div > h1')
    album = soup.select('#container > section.sectionPadding.summaryInfo.summaryTrack > div > div.basicInfo > table > tbody > tr:nth-child(3) > td > a')

    # 앨범 아트 이미지 가져오기
    soup = BeautifulSoup(song_response.content, "html.parser")
    img_url = soup.select_one('img').get('src').replace('200', '1000')  # 고해상도 앨범아트로 변경

    img_response = requests.get(img_url)

    # 검색된 이미지를 PIL Image 객체로 변환
    try:
        image = Image.open(BytesIO(img_response.content))
        mime_type = 'image/' + image.format.lower()
        image_data = img_response.content
#         logging.info(f"[PIL Complete]")
        print("[PIL Complete] 앨범 아트 이미지 변환 성공")
    except Exception as e:
#         logging.error(f"[Error Occurred][앨범 아트 이미지 변환 실패][{e}]")
        print("[Error Occurred] 앨범 아트 이미지 변환 실패: ", e)

    # 가사 가져오기
    lyrics_url = soup.find("div", {"class": "lyricsContainer"}).text
    delete_part = lyrics_url.find('님이 등록해 주신 가사입니다.')
    lyrics = lyrics_url[:delete_part]

    # mp3 파일 경로
    mp3_file = file_link

    # mp3 파일 태그 수정
    try:
        audio = ID3(mp3_file)
        print("[Tag Edit Success] 앨범 아트 이미지 변환 성공")
    except Exception as e:
        audio = ID3()
        print('[Tag Edit Fail] mp3 파일 태그 수정 실패: ', e)

    try:
        print('[Tags] ' + song[0].text.strip() + ' ' + artist[0].text.strip() + ' ' + album[0].text.strip())

        audio["TIT2"] = TIT2(encoding=3, text=song[0].text.strip())  # 제목
        audio["TPE1"] = TPE1(encoding=3, text=artist[0].text.strip())  # 가수
        audio["TALB"] = TALB(encoding=3, text=album[0].text.strip())  # 앨범
        audio["APIC"] = APIC(encoding=3, mime=mime_type, type=3, desc=u'Cover', data=image_data)  # 앨범아트
        audio["USLT"] = USLT(encoding=3, lang=u'kor', desc=u'', text=lyrics)  # 가사

        audio.save(mp3_file)

        global global_final_artist_name, global_final_song_title
        global_final_artist_name = artist[0].text.strip()
        global_final_song_title = song[0].text.strip()

#         logging.info(f'[Complete] {mp3_file} Complete!')
        print('[Success][mp3 파일 저장 성공][' + mp3_file + ']')
    except Exception as e:
#         logging.error(f"[Error][mp3 파일 태그 수정 Fail][{e}]")
        print("[Error][mp3 파일 저장 실패][" + mp3_file + '] : ', e)


def rename_mp3_file(file_path, artist, songTitle):
    # 파일명 수정: {artist} - {songTitle} 형식으로 변경
    new_file_name = f"{artist.strip()} - {songTitle.strip()}.mp3"
    new_file_path = os.path.join(os.path.dirname(file_path), new_file_name)

    # 파일이 이미 존재하는지 확인
    if os.path.exists(new_file_path):
        print(f"[Renamed Fail] {os.path.basename(new_file_path)}가 이미 있습니다")  # 파일이 이미 존재하는 경우 출력
    else:
        os.rename(file_path, new_file_path)
        print(f"[Renamed Success] {os.path.basename(file_path)} -> {os.path.basename(new_file_path)}")
    return new_file_path


if __name__ == "__main__":
    # 커맨드라인 인자 받기
    parser = argparse.ArgumentParser(description='Update MP3 file tags.')
    parser.add_argument('--mp3path', required=True, help='Path to MP3 files')
    args = parser.parse_args()

    path = args.mp3path


    file_list = glob.glob(os.path.join(path, "*.mp3"))

    if not file_list:
        print(f"[Error] 지정된 경로에 mp3 파일이 없습니다: {path}")
    else:
        for file_path in file_list:
            print(f"[Found] 파일 경로: {file_path}")

            file_name = os.path.basename(file_path)
            start = file_name.rfind('-')
            end = file_name.rfind('.mp3')

            if start == -1 or end == -1:
                continue  # 올바른 형식이 아니면 건너뜀

            query = file_name[:end].replace('-', ' ')

            print(f'[Processing] {query}')
            edit_mp3_file(query, file_path)

            artist = file_name[:start]  # 아티스트 이름
            song = file_name[start + 1:end]  # 노래 제목

            # 파일명을 {artist} - {songTitle} 형식으로 변경
            new_file_path = rename_mp3_file(file_path, global_final_artist_name, global_final_song_title)



import requests
from bs4 import BeautifulSoup
from io import BytesIO
from mutagen.id3 import ID3, APIC, TIT2, TPE1, TALB, USLT
from PIL import Image
import os
import glob
import logging
import sys

# 로그 설정 (UTF-8 없으면 한글 깨짐)
log_dir = os.path.join('src\\main\\resources\\logs')
if not os.path.exists(log_dir):
    os.makedirs(log_dir)

logging.basicConfig(
    filename=os.path.join(log_dir, 'Update_mp3_file.log'),
    level=logging.INFO,
    encoding='utf-8'  # UTF-8 인코딩 설정
)

# 콘솔 출력 인코딩 설정 (UTF-8)
if sys.stdout.encoding != 'UTF-8':
    import io
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

def edit_mp3_file(query, file_link):
    logging.info(f'[SYSTEM] Starting Edit mp3 file')
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
        logging.info(f"[PIL Complete]")
        print("[PIL Complete] 앨범 아트 이미지 변환 성공")
    except Exception as e:
        logging.error(f"[Error Occurred][앨범 아트 이미지 변환 실패][{e}]")
        print("앨범 아트 이미지 변환 실패: ", e)

    # 가사 가져오기
    lyrics_url = soup.find("div", {"class": "lyricsContainer"}).text
    delete_part = lyrics_url.find('님이 등록해 주신 가사입니다.')
    lyrics = lyrics_url[:delete_part]

    # mp3 파일 경로
    mp3_file = file_link

    # mp3 파일 태그 수정
    try:
        audio = ID3(mp3_file)
    except Exception as e:
        print('mp3 파일 태그 수정 실패: ', e)
        audio = ID3()

    try:
        print('[Tags] ' + song[0].text.strip() + ' ' + artist[0].text.strip() + ' ' + album[0].text.strip())

        audio["TIT2"] = TIT2(encoding=3, text=song[0].text.strip())  # 제목
        audio["TPE1"] = TPE1(encoding=3, text=artist[0].text.strip())  # 가수
        audio["TALB"] = TALB(encoding=3, text=album[0].text.strip())  # 앨범
        audio["APIC"] = APIC(encoding=3, mime=mime_type, type=3, desc=u'Cover', data=image_data)  # 앨범아트
        audio["USLT"] = USLT(encoding=3, lang=u'kor', desc=u'', text=lyrics)  # 가사

        audio.save(mp3_file)
        logging.info(f'[Complete] {mp3_file} Complete!')
        print('[Success] ' + mp3_file + ' Complete!')
    except Exception as e:
        logging.error(f"[Error][mp3 파일 태그 수정 Fail][{e}]")
        print("[Error] mp3 파일 저장 실패 [" + mp3_file + '] : ', e)


def rename_mp3_file(file_path, artist, song):
    # 파일명 수정: {SingerName} - {SongName} 형식으로 변경
    new_file_name = f"{artist.strip()} - {song.strip()}.mp3"
    new_file_path = os.path.join(os.path.dirname(file_path), new_file_name)
    os.rename(file_path, new_file_path)
    print(f"[Renamed] {file_path} -> {new_file_path}")
    return new_file_path


if __name__ == "__main__":

    # 파일이 있는 경로 (경로 구분자는 '/' 사용)
    path = os.path.join("src", "main", "resources", "mp3file")

    # 스크립트 파일이 있는 디렉토리를 기준으로 경로 생성
    script_dir = os.path.dirname(os.path.abspath(__file__))
    path = os.path.join(script_dir, '..', 'mp3file')  # scripts 기준으로 상위 경로 지정

    # 경로가 존재하는지 확인
    if not os.path.exists(path):
        print(f"[Error] 지정된 경로가 존재하지 않습니다: {path}")
    else:
        # 해당 경로 내 모든 .mp3 파일 리스트 반환
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

                logging.info(f'[Processing] {query}')
                print(f'[Processing] {query}')
                edit_mp3_file(query, file_path)

                artist = file_name[:start]  # 아티스트 이름
                song = file_name[start + 1:end]  # 노래 제목

                # 파일명을 {SingerName} - {SongName} 형식으로 변경
                new_file_path = rename_mp3_file(file_path, artist, song)




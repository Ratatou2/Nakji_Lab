import requests
from bs4 import BeautifulSoup
from io import BytesIO
from mutagen.id3 import ID3, APIC, TIT2, TPE1, TALB, USLT
from PIL import Image
import os
import glob
import sys
import argparse
import time

# 콘솔 출력 인코딩 설정 (UTF-8)
if sys.stdout.encoding != 'UTF-8':
    import io
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

global_final_artist_name = ""
global_final_song_title = ""

# 안전한 GET 요청
def safe_get(url, headers=None, retries=1, timeout=20):
    try:
        response = requests.get(url, headers=headers, timeout=timeout)
        if response.status_code == 200:
            return response
        else:
            print(f"[HTTP {response.status_code}] 실패: {url}")
    except requests.RequestException as e:
        print(f"[RequestError] {e} ({url})")
    return None

def edit_mp3_file(query, file_link):
    print(f"[Start] {query}")
    base_url = f"https://music.bugs.co.kr/search/integrated?q={query}"
    response = safe_get(base_url)
    if response is None:
        print("[Skip] 검색 페이지 요청 실패")
        return False

    soup = BeautifulSoup(response.content, "html.parser")
    track_link = soup.find("a", {"class": "trackInfo"})
    if not track_link:
        print("[Skip] 곡 링크를 찾지 못함")
        return False

    song_url = track_link.get('href')
    song_response = safe_get(song_url)
    if song_response is None:
        print("[Skip] 곡 상세 페이지 요청 실패")
        return False

    soup = BeautifulSoup(song_response.content, "html.parser")

    try:
        artist = soup.select('#container section.summaryTrack .basicInfo table tbody tr:nth-child(1) td a')[0].text.strip()
        song = soup.select('#container > header > div > h1')[0].text.strip()
        album = soup.select('#container section.summaryTrack .basicInfo table tbody tr:nth-child(3) td a')[0].text.strip()
    except Exception as e:
        print("[Skip] 메타 정보 파싱 실패:", e)
        return False

    img_tag = soup.select_one('img')
    if not img_tag or not img_tag.get('src'):
        print("[Skip] 앨범 아트 없음")
        return False

    img_url = img_tag.get('src').replace('200', '1000')
    img_response = safe_get(img_url)
    if img_response is None:
        print("[Skip] 앨범 아트 다운로드 실패")
        return False

    try:
        image = Image.open(BytesIO(img_response.content))
        mime_type = 'image/' + image.format.lower()
        image_data = img_response.content
    except Exception as e:
        print("[Skip] 앨범 아트 변환 실패:", e)
        return False

    try:
        audio = ID3(file_link)
    except Exception:
        audio = ID3()

    try:
        audio["TIT2"] = TIT2(encoding=3, text=song)
        audio["TPE1"] = TPE1(encoding=3, text=artist)
        audio["TALB"] = TALB(encoding=3, text=album)
        audio["APIC"] = APIC(encoding=3, mime=mime_type, type=3, desc=u'Cover', data=image_data)
        lyrics_block = soup.find("div", {"class": "lyricsContainer"})
        if lyrics_block:
            raw = lyrics_block.text
            cut = raw.find('님이 등록해 주신 가사입니다.')
            lyrics = raw[:cut].strip() if cut > 0 else raw.strip()
            audio["USLT"] = USLT(encoding=3, lang=u'kor', desc=u'', text=lyrics)

        audio.save(file_link)
        global global_final_artist_name, global_final_song_title
        global_final_artist_name = artist
        global_final_song_title = song

        print(f"[Success] 저장 완료: {file_link}")
        return True
    except Exception as e:
        print("[Error] mp3 저장 실패:", e)
        return False

def rename_mp3_file(file_path, artist, songTitle):
    new_file_name = f"{artist.strip()} - {songTitle.strip()}.mp3"
    new_file_path = os.path.join(os.path.dirname(file_path), new_file_name)

    if os.path.exists(new_file_path):
        print(f"[Rename Fail] {os.path.basename(new_file_path)} 이미 존재")
    else:
        os.rename(file_path, new_file_path)
        print(f"[Renamed] {os.path.basename(file_path)} -> {os.path.basename(new_file_path)}")
    return new_file_path

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Update MP3 file tags.')
    parser.add_argument('--mp3path', required=True, help='Path to MP3 files')
    args = parser.parse_args()
    path = args.mp3path

    file_list = glob.glob(os.path.join(path, "*.mp3"))
    if not file_list:
        print(f"[Error] mp3 파일 없음: {path}")
    else:
        for file_path in file_list:
            print(f"[파일 감지] {file_path}")
            file_name = os.path.basename(file_path)
            start = file_name.rfind('-')
            end = file_name.rfind('.mp3')

            if start == -1 or end == -1:
                print(f"[Skip] 잘못된 파일명 형식: {file_name}")
                continue

            query = file_name[:end].replace('-', ' ')
            print(f"[처리 중] {query}")

            success = edit_mp3_file(query, file_path)
            if success:
                rename_mp3_file(file_path, global_final_artist_name, global_final_song_title)
            else:
                print("[Skip] 메타데이터 업데이트 실패. 다음 파일로 넘어감.")
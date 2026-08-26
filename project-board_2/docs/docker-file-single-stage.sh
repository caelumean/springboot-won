# 모든 기존 컨테이너 강제 중지 및 전체 삭제
docker rm -f $(docker ps -aq) 2>/dev/null || true

# 모든 기존 이미지 강제 전체 삭제
docker rmi -f $(docker images -q) 2>/dev/null || true

# 모든 기존 Named Volumes 및 네트워크 전체 삭제
docker volume rm -f $(docker volume ls -q) 2>/dev/null || true
docker network rm -f $(docker network ls -q) 2>/dev/null || true

# Spring Board 애플리케이션 전용 사용자 정의 네트워크 생성
docker network create board-net

# DB 데이터 영속성 보존용 Named Volume 생성
docker volume create board-db-data

# 게시판 첨부파일(uploads) 및 시스템 로그(logs) 바인드 마운트 전용 호스트 디렉터리 사전 생성
mkdir -p uploads logs

# MySQL DB 컨테이너 백그라운드 구동, 볼륨 마운트 및 환경 변수 설정
docker run -d --name board-db \
  --network board-net \
  -p 3306:3306 \
  -v board-db-data:/var/lib/mysql \
  -e MYSQL_DATABASE=board_db \
  -e MYSQL_USER=board-app \
  -e MYSQL_PASSWORD=Board123! \
  -e MYSQL_ROOT_PASSWORD=rootpass \
  mysql:9.7
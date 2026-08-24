# 컨테이너가 구동중일 경우 삭제 후 다시 구동
docker stop mydb
docker stop board-web
docker rm mydb
docker rm board-web

# MySql DB 컨테이너 구동
docker run -d --name mydb --network my-net -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1111 mysql:9.7

# temurin 25 jre 컨테이너 구동
# tail 명령 - 컨테이너를 구동하고 그 안에서 그 명령어를 실행하라
docker stop board-web
docker rm board-web
docker run -d --name board-web -p 80:8080 --network my-net eclipse-temurin:25-jre-alpine tail -f /dev/null

# 로컬의 빌드된 최종 코드를 컨테이너에 복사(로그인한 계정의 홈디렉토리로 복사)
# 로컬에서 실행
docker cp ./build/libs/spring-board-0.0.1-SNAPSHOT.jar board-web:/root/board.jar

# 프로젝트를 도커 이미지로 빌드
./gradlew bootBuildImage --imageName=caelumean/spring-board:1.0

# 컨테이너 내부의 대화형 쉘 접속
docker exec -it board-web //bin/sh
cd ~
java -jar board.jar

# spring-board 배포
# 1. 기존 컨테이너들 중지 및 삭제
docker stop spring-board db-server
docker rm spring-board db-server

# 2. 네트워크 삭제
docker network rm myapp-net

# 3. 네트워크 새로 생성
docker network create myapp-net

# Named Volume 생성
docker volume create spring-board-db-data

# 4. MySQL 컨테이너 먼저 실행
#docker run --name db-server --network myapp-net -p 3306:3306 -e MYSQL_DATABASE=board_db -e MYSQL_ROOT_PASSWORD=1111 mysql:9.7
# USER = board-app
docker run --name db-server --network myapp-net -p 3306:3306 \
  -v spring-board_db-data:/var/lib/mysql \
  -e MYSQL_DATABASE=board_db \
  -e MYSQL_ROOT_PASSWORD=1111 \
  -e MYSQL_USER=board-app \
  -e MYSQL_PASSWORD=Board123! \
   mysql:9.7

# (ready for connections 문구가 뜨면 Ctrl+C로 빠져나오기, 안되면 엔터 ~ . 순서로 입력하면  SSH 연결을 끊을 수 있음)

# 5. 스프링 부트 컨테이너 실행
MSYS_NO_PATHCONV=1 docker run -d --name spring-board --network myapp-net -p 80:8080 \
 -v "${PWD}/uploads:/app/uploads" \
 -v "${PWD}/logs:/app/logs" \
 -e SPRING_DATASOURCE_USERNAME=board-app \
 -e SPRING_DATASOURCE_PASSWORD=Board123! \
 -e SPRING_SQL_INIT_MODE=never \
 caelumean/spring-board:1.0

# 테스트 완료된 spring-boaard 이미지를 운영 서버에 배포하기 위해서 docker hub에 빌드
# 로그인
docker login

# docker hub에 이미지 업로드
docker push caelumean/spring-board:1.0


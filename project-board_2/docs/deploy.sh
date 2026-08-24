
####### 로컬에서 실행 #######
# 로컬의 빌드된 최종 코드를 EC2에 업로드
scp -i "first-key.pem" build/libs/spring-board-0.0.1-SNAPSHOT.jar ec2-user@15.164.70.248:/home/ec2-user/
#scp -i "first-key.pem" build/libs/spring-board-0.0.1-SNAPSHOT.jar ec2-user@3.39.226.73:/home/ec2-user/

# EC2에 SSH로 원격 접속
# 탄력적 IP
ssh -i "first-key.pem" ec2-user@15.164.70.248

# 인스턴스 ip
ssh -i "first-key.pem" ec2-user@3.39.226.73

# 서버 지문 변경으로 인해 접속이 되지 않을 경우 이전 서버의 지문을 삭제
ssh-keygen -R 15.164.70.248

######### EC2에서 실행 #######
# 자바 서버 실행
java -jar spring-board-0.0.1-SNAPSHOT.jar

# mysql 클라이언트로 RDS 서비스에 접속
mysql -h board-db.cxw64me2icmw.ap-northeast-2.rds.amazonaws.com -u admin -p

CREATE DATABASE board_db;

CREATE USER 'board_app'@'%' IDENTIFIED BY 'Board123!';

GRANT ALL PRIVILEGES ON board_db.* TO 'board_app'@'%';

FLUSH PRIVILEGES;

EXIT;
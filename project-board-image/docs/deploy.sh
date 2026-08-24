
####### 로컬에서 실행 #######
# 로컬의 빌드된 최종 코드를 EC2에 업로드
scp -i "first-key.pem" build/libs/spring-board-0.0.1-SNAPSHOT.jar ec2-user@107.21.76.82:/home/ec2-user/

# EC2에 SSH로 원격 접속
ssh -i "first-key.pem" ec2-user@35.78.215.31

####### EC2에서 실행 #######
# 라이브러리 매니저 업데이트
sudo yum update -y

# jdk 라이브러리 정보 등록
sudo tee /etc/yum.repos.d/adoptium.repo << 'EOF'
[Adoptium]
name=Adoptium
baseurl=https://packages.adoptium.net/artifactory/rpm/amazonlinux/2/x86_64
enabled=1
gpgcheck=1
gpgkey=https://packages.adoptium.net/artifactory/api/gpg/key/public
EOF

# jdk 설치
sudo yum install temurin-25-jdk -y

# JAVA_HOME 환경 변수 등록
echo 'export JAVA_HOME=/usr/lib/jvm/java-25-temurin-jdk' | sudo tee -a /etc/profile
source /etc/profile

# 자바 서버 실행
java -jar spring-board-0.0.1-SNAPSHOT.jar


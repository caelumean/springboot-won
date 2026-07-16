-- 게시글 테이블 생성
DROP TABLE IF EXISTS post2;
CREATE TABLE IF NOT EXISTS post2 (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     author VARCHAR(10) NOT NULL,
                                     title VARCHAR(200) NOT NULL,
                                     content TEXT NOT NULL,
                                     secret BOOLEAN NOT NULL DEFAULT FALSE,
                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
DROP TABLE IF EXISTS member2;
CREATE TABLE IF NOT EXISTS member2 (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       user_id VARCHAR(30) NOT NULL UNIQUE,
                                       password VARCHAR(100) NOT NULL,
                                       name VARCHAR(30) NOT NULL,
                                       email VARCHAR(100) NOT NULL,
                                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP
)


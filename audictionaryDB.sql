# DB 생성
DROP DATABASE IF EXISTS audictionary;
CREATE DATABASE audictionary;

USE audictionary;

# 지원자 테이블 생성
CREATE TABLE ap (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
    delDate DATETIME,
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
	loginId CHAR(20) NOT NULL,
	loginPw VARCHAR(200) NOT NULL,
	`name` CHAR(10) NOT NULL, # 이름
    engName CHAR(20) NOT NULL, # 영문이름
	nickName CHAR(10), # 활동명
	gender CHAR(2) NOT NULL, # 성별
	regNumber CHAR(14) NOT NULL, # 주민등록번호
    address CHAR(30) NOT NULL, # 주소
	cellPhoneNo VARCHAR(15) NOT NULL, # 전화번호
    feet INT(10) UNSIGNED, # 키
    weight INT(10) UNSIGNED, # 몸무게
    skinTone CHAR(10), # 피부색
    eyelid TINYINT(1) UNSIGNED, # 쌍꺼풀 유무
    feature CHAR(100), # 키워드(특징)
    filmgraphy TEXT, # 커리어
    jobArea CHAR(10), # 희망분야
	corp CHAR(20), # 소속 회사
    authLevel INT(10) UNSIGNED NOT NULL DEFAULT 2, # 회원관리등급
    authKey CHAR(80) NOT NULL, # 로그인 인증키
	UNIQUE INDEX loginId (loginId),
	UNIQUE INDEX authKey (authKey),
	INDEX `name` (`name` , jobArea, feature, gender, feet, weight)
);

# 제작자 테이블 생성pd
CREATE TABLE pd (
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
    delDate DATETIME,
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
	loginId CHAR(20) NOT NULL,
	loginPw VARCHAR(200) NOT NULL,
	`name` CHAR(10) NOT NULL,
	gender CHAR(2) NOT NULL, # 성별
	regNumber CHAR(100) NOT NULL, # 주민등록번호
	address CHAR(30) NOT NULL,	# 주소
	email CHAR(20) NOT NULL,  # 이메일
	cellPhoneNo VARCHAR(15) NOT NULL,  # 전화번호
    jobPosition CHAR(10) NOT NULL, # 직급
	corpName CHAR(10), # 회사이름
    corpType CHAR(10), # 회사타입
    filmgraphy TEXT, # 커리어
    authLevel INT(10) UNSIGNED NOT NULL DEFAULT 2, # 회원관리등급 ( 1 = 관리자 , 2 = 제작자 )
    authKey CHAR(80) NOT NULL, # 로그인 인증키
	UNIQUE INDEX loginId (loginId),
    UNIQUE INDEX authKey (authKey),
	INDEX `name` (`name`)
);

CREATE TABLE attr(
id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
regDate DATETIME NOT NULL,
updateDate DATETIME NOT NULL,
expireDate DATETIME,
relTypeCode CHAR(50),
relId INT(10) UNSIGNED,
typeCode CHAR(50),
type2Code CHAR(50),
`value` CHAR(50)
);

#보드 테이블 만들기
CREATE TABLE board(
id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
regDate DATETIME NOT NULL,
updateDate DATETIME NOT NULL,
delDate DATETIME,
delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
`name` char(20) not null,
`code` char(20) not null
);

# 기본적인 게시판 만들기
INSERT INTO board SET
regDate = Now(), updateDate = now(),
`name` = '공지사항',
`code` = 'notice';

INSERT INTO board SET
regDate = Now(), updateDate = now(),
`name` = '문의사항',
`code` = 'qna';

INSERT INTO board SET
regDate = Now(), updateDate = now(),
`name` = '공고',
`code` = 'recruit';


# 모집공고 테이블 생성
CREATE TABLE recruitment (
id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
regDate DATETIME NOT NULL,
updateDate DATETIME NOT NULL,
delDate DATETIME,
delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
memberId INT(10) UNSIGNED,
boardId INT(10) UNSIGNED,
roleType char(100) not null, # 배역타입
title CHAR(100) NOT NULL, # 공고제목
`body` LONGTEXT NOT NULL, # 공고내용
location CHAR(10), # 촬영장소
`period` CHAR(10), # 촬영기간
deadline DATETIME, # 모집기한날짜
script text not null, # 연기대사
videoTime int(10) unsigned not null, # 영상 시간( 1 = 1분이내 / 2 = 1~2분 / 3 = 2~3분 / 4 = 5분이내 )
etc text not null, # 기타우대사항
INDEX title (title)
);

# 작품 테이블 만들기
CREATE TABLE artwork (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    relTypeCode char(50) not null, # 타입코드
    relId INT(10) UNSIGNED, # 공고 번호
    `name` CHAR(50) NOT NULL, # 작품이름
	media char(50) not null, # 매체
    genre CHAR(20), # 장르
	corp CHAR(50) NOT NULL, # 제작사
    director CHAR(50) NOT NULL, # 감독
    producer char(50) not null, # 프로듀서
    castingManager char(20) not null, # 캐스팅매니저
    story text not null, # 줄거리
	imageUrl char(100), # 이미지url
    etc TEXT
);

# 배역 테이블 만들기
CREATE TABLE actingRole (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    recruitmentId INT(10) UNSIGNED NOT NULL,
    realName CHAR(50) NOT NULL, # 배역본명
    `name` CHAR(50) NOT NULL, # 배역이름
    pay CHAR(50) NOT NULL, # 출연료
    age CHAR(50) NOT NULL, # 나이
    job CHAR(100) NOT NULL, # 직업
    gender CHAR(5) NOT NULL, # 성별
    scriptStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0, # 대사유무
    scenesCount TINYINT(2) UNSIGNED NOT NULL DEFAULT 0, # 장면수
    shootingsCount TINYINT(2) UNSIGNED NOT NULL DEFAULT 0, # 촬영횟수
    `character` TEXT, # 배역 설명
    etc TEXT
);

# 공고 지원 테이블 생성
CREATE TABLE application (
id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
regDate DATETIME NOT NULL,
updateDate DATETIME NOT NULL,
memberId INT(10) UNSIGNED NOT NULL,
recruitId INT(10) UNSIGNED NOT NULL, # 공고번호
`exp` TINYINT(1) UNSIGNED NOT NULL #경력 여부
);

# 파일 테이블 생성
CREATE TABLE genFile (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT, # 번호
  regDate DATETIME DEFAULT NULL, # 작성날짜
  updateDate DATETIME DEFAULT NULL, # 갱신날짜
  delDate DATETIME DEFAULT NULL, # 삭제날짜
  delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0, # 삭제상태(0:미삭제,1:삭제)
  relTypeCode CHAR(50) NOT NULL, # 관련 데이터 타입(article, member)
  relId INT(10) UNSIGNED NOT NULL, # 관련 데이터 번호
  originFileName VARCHAR(100) NOT NULL, # 업로드 당시의 파일이름
  fileExt CHAR(10) NOT NULL, # 확장자
  typeCode CHAR(20) NOT NULL, # 종류코드 (common)
  type2Code CHAR(20) NOT NULL, # 종류2코드 (attatchment)
  fileSize INT(10) UNSIGNED NOT NULL, # 파일의 사이즈
  fileExtTypeCode CHAR(10) NOT NULL, # 파일규격코드(img, video)
  fileExtType2Code CHAR(10) NOT NULL, # 파일규격2코드(jpg, mp4)
  fileNo SMALLINT(2) UNSIGNED NOT NULL, # 파일번호 (1)
  fileDir CHAR(20) NOT NULL, # 파일이 저장되는 폴더명
  PRIMARY KEY (id),
  KEY relId (relId,relTypeCode,typeCode,type2Code,fileNo)
); 

select * from pd;


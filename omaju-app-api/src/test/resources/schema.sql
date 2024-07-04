-- 테이블 coredb.member 구조 내보내기
CREATE TABLE IF NOT EXISTS "member"
(
    "id"         INTEGER      NOT NULL AUTO_INCREMENT,
    "profile_img" varchar(200),
    "created_at" BIGINT DEFAULT NULL,
    "updated_at" BIGINT DEFAULT NULL,
    "email"      varchar(50)  NOT NULL,
    "fcm_token"  varchar(200) NULL,
    "is_leave"   BOOLEAN      NOT NULL,
    "nickname"   varchar(50)  NOT NULL,
    "provider"   varchar(50)  NOT NULL,
    "role"       varchar(50)  NOT NULL
);

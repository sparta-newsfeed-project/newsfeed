CREATE TABLE users (
    id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    email   VARCHAR(255) NOT NULL UNIQUE,
    name    VARCHAR(100) NOT NULL,
    intro_text VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL,
    deleted_at DATETIME DEFAULT NULL
);

CREATE TABLE posts (
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    title    VARCHAR(100) NOT NULL,
    content   TEXT NOT NULL,
    comment_count INT UNSIGNED NOT NULL DEFAULT 0,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL,
    deleted_at DATETIME DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE comments (
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    post_id     BIGINT NOT NULL,
    content   VARCHAR(255) NOT NULL,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL,
    deleted_at DATETIME DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (post_id) REFERENCES posts(id)
);

CREATE TABLE follows (
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    follower_id     BIGINT NOT NULL,
    followed_id     BIGINT NOT NULL,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL,
    deleted_at DATETIME DEFAULT NULL,
    FOREIGN KEY (follower_id) REFERENCES users(id),
    FOREIGN KEY (followed_id) REFERENCES users(id)
);



CREATE TABLE IF NOT EXISTS MPA
(
    MPA_ID   INT PRIMARY KEY AUTO_INCREMENT,
    MPA_NAME VARCHAR(55) NOT NULL
);

CREATE TABLE IF NOT EXISTS GENRES
(
    GENRE_ID   INT PRIMARY KEY AUTO_INCREMENT,
    GENRE_NAME VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID   INT PRIMARY KEY AUTO_INCREMENT,
    EMAIL     VARCHAR(55) NOT NULL,
    LOGIN     VARCHAR(55) NOT NULL,
    USER_NAME VARCHAR(55) NOT NULL,
    BIRTHDAY  DATE

);

CREATE TABLE IF NOT EXISTS FILMS
(
    FILM_ID      INT PRIMARY KEY AUTO_INCREMENT,
    FILM_NAME    VARCHAR(155) NOT NULL,
    DESCRIPTION  VARCHAR(255) NOT NULL,
    RELEASE_DATE DATE,
    DURATION     INT,
    GENRE_ID     INT REFERENCES GENRES (GENRE_ID),
    MPA_ID       INT REFERENCES MPA (MPA_ID)
);

CREATE TABLE IF NOT EXISTS FILMS_GENRES
(
    FILM_ID  INT REFERENCES FILMS (FILM_ID),
    GENRE_ID INT REFERENCES GENRES (GENRE_ID),
    PRIMARY KEY (FILM_ID, GENRE_ID)
);

CREATE TABLE IF NOT EXISTS LIKES
(
    FILM_ID  INT REFERENCES FILMS (FILM_ID),
    USER_ID INT REFERENCES USERS (USER_ID),
    PRIMARY KEY (FILM_ID, USER_ID)
);

CREATE TABLE IF NOT EXISTS FRIENDS
(
    USER_ID INT REFERENCES USERS (USER_ID),
    FRIEND_ID INT REFERENCES USERS (USER_ID),
    PRIMARY KEY (USER_ID, FRIEND_ID)
);


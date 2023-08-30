DROP ALL OBJECTS;

CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID  LONG AUTO_INCREMENT,
    EMAIL    CHARACTER VARYING(50) NOT NULL,
    LOGIN    CHARACTER VARYING(50) NOT NULL,
    NAME     CHARACTER VARYING(50),
    BIRTHDAY DATE                  NOT NULL,
    CONSTRAINT USERS_PK
        PRIMARY KEY (USER_ID)
);

CREATE TABLE IF NOT EXISTS FRIENDS
(
    USER_ID   LONG NOT NULL,
    FRIEND_ID LONG NOT NULL,
    CONSTRAINT FRIENDS_USERS_USER_ID_FK
        FOREIGN KEY (USER_ID) REFERENCES USERS
            ON DELETE CASCADE,
    CONSTRAINT FRIENDS_USERS_USER_ID_FK_2
        FOREIGN KEY (FRIEND_ID) REFERENCES USERS
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS DIRECTORS
(
    DIRECTOR_ID LONG AUTO_INCREMENT,
    NAME        CHARACTER VARYING(50) NOT NULL,
    CONSTRAINT DIRECTORS_PK PRIMARY KEY (DIRECTOR_ID)
);

CREATE TABLE IF NOT EXISTS MPA
(
    MPA_ID INTEGER AUTO_INCREMENT,
    NAME   CHARACTER VARYING(50) NOT NULL,
    CONSTRAINT "MPA_PK"
        PRIMARY KEY (MPA_ID)
);

CREATE TABLE IF NOT EXISTS FILMS
(
    FILM_ID      LONG AUTO_INCREMENT,
    NAME         CHARACTER VARYING(200) NOT NULL,
    DESCRIPTION  CHARACTER VARYING(200) NOT NULL,
    RELEASE_DATE DATE                   NOT NULL,
    DURATION     INTEGER                NOT NULL,
    MPA_ID       INTEGER                NOT NULL,
    CONSTRAINT FILMS_PK
        PRIMARY KEY (FILM_ID),
    CONSTRAINT FILMS_MPA_MPA_ID_FK
        FOREIGN KEY (MPA_ID) REFERENCES MPA
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS FILM_LIKES
(
    FILM_ID LONG NOT NULL,
    USER_ID LONG NOT NULL,
    CONSTRAINT FILMLIKES_FILMS_FILM_ID_FK
        FOREIGN KEY (FILM_ID) REFERENCES FILMS
            ON DELETE CASCADE,
    CONSTRAINT FILMLIKES_USERS_USER_ID_FK
        FOREIGN KEY (USER_ID) REFERENCES USERS
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS FILM_DIRECTOR
(
    FILM_ID     LONG NOT NULL,
    DIRECTOR_ID LONG NOT NULL,
    CONSTRAINT PK_FILM_DIRECTOR
        PRIMARY KEY (FILM_ID, DIRECTOR_ID),
    CONSTRAINT FK_FILM_DIRECTOR_FILM_ID
        FOREIGN KEY (FILM_ID) REFERENCES FILMS
            ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_FILM_DIRECTOR_DIRECTOR_ID
        FOREIGN KEY (DIRECTOR_ID) REFERENCES DIRECTORS
            ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS GENRE
(
    GENRE_ID INTEGER AUTO_INCREMENT,
    NAME     CHARACTER VARYING(50) NOT NULL,
    CONSTRAINT "GENRE_PK"
        PRIMARY KEY (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS FILM_GENRE
(
    FILM_ID  LONG    NOT NULL,
    GENRE_ID INTEGER NOT NULL,
    CONSTRAINT FILM_GENRE_FILMS_FILM_ID_FK
        FOREIGN KEY (FILM_ID) REFERENCES FILMS
            ON DELETE CASCADE,
    CONSTRAINT FILM_GENRE_GENRE_GENRE_ID_FK
        FOREIGN KEY (GENRE_ID) REFERENCES GENRE
);

CREATE TABLE IF NOT EXISTS FEED
(
    EVENT_ID   LONG AUTO_INCREMENT,
    ENTITY_ID  LONG                  NOT NULL,
    USER_ID    LONG                  NOT NULL,
    TIME_STAMP LONG                  NOT NULL,
    EVENT_TYPE CHARACTER VARYING(10) NOT NULL,
    OPERATION  CHARACTER VARYING(10) NOT NULL,
    CONSTRAINT "FEED_PK"
        PRIMARY KEY (EVENT_ID),
    CONSTRAINT FEED_USERS_USER_ID_FK
        FOREIGN KEY (USER_ID) REFERENCES USERS
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS REVIEWS
(
    REVIEW_ID   LONG AUTO_INCREMENT,
    CONTENT     CHARACTER VARYING NOT NULL,
    IS_POSITIVE BOOLEAN           NOT NULL,
    USER_ID     LONG              NOT NULL,
    FILM_ID     LONG              NOT NULL,
    CONSTRAINT "REVIEWS_PK"
        PRIMARY KEY (REVIEW_ID),
    CONSTRAINT "REVIEWS_FILMS_NULL_FK"
        FOREIGN KEY (FILM_ID) REFERENCES FILMS
            ON DELETE CASCADE,
    CONSTRAINT REVIEWS_USERS_USER_ID_FK
        FOREIGN KEY (USER_ID) REFERENCES USERS
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS REVIEW_USER
(
    REVIEW_ID LONG NOT NULL,
    USER_ID   LONG NOT NULL,
    IS_USEFUL LONG NOT NULL,
    CONSTRAINT REVIEW_USER_REVIEWS_REVIEW_ID_FK
        FOREIGN KEY (REVIEW_ID) REFERENCES REVIEWS
            ON DELETE CASCADE,
    CONSTRAINT REVIEW_USER_USERS_USER_ID_FK
        FOREIGN KEY (USER_ID) REFERENCES USERS
            ON DELETE CASCADE
);
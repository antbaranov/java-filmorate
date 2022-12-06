CREATE TABLE IF NOT EXISTS FILMS
(
    FILM_ID      INT          NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    FILM_NAME    VARCHAR(155) NOT NULL,
    DESCRIPTION  VARCHAR(255) NOT NULL,
    RELEASE_DATE DATE         NOT NULL,
    DURATION     INT          NOT NULL,
    RATE         INT,
    RATING_ID    INT          NOT NULL,
    CONSTRAINT pk_Film PRIMARY KEY (FILM_ID)
);

CREATE TABLE IF NOT EXISTS RATING_MPA
(
    RATING_ID   INT          NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    MPA_NAME    VARCHAR(10)  NOT NULL,
    DESCRIPTION VARCHAR(200) NOT NULL,
    CONSTRAINT pk_RatingMPA PRIMARY KEY (RATING_ID),
    CONSTRAINT uc_RatingMPA_Name UNIQUE (MPA_NAME)
);

CREATE TABLE IF NOT EXISTS GENRE_LINE
(
    GENRE_LINE_ID INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    FILM_ID       INT NOT NULL,
    GENRE_ID      INT NOT NULL,
    CONSTRAINT pk_GenreLine PRIMARY KEY (GENRE_LINE_ID)
);

CREATE TABLE IF NOT EXISTS GENRES
(
    GENRE_ID   INT         NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    GENRE_NAME VARCHAR(50) NOT NULL,
    CONSTRAINT pk_Genre PRIMARY KEY (GENRE_ID),
    CONSTRAINT uc_Genre_Name UNIQUE (GENRE_NAME)
);

CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID   INT         NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    EMAIL     VARCHAR(55) NOT NULL,
    LOGIN     VARCHAR(55) NOT NULL,
    USER_NAME VARCHAR(55) NOT NULL,
    BIRTHDAY  DATE        NOT NULL,
    CONSTRAINT pk_User PRIMARY KEY (USER_ID),
    CONSTRAINT uc_User_Email UNIQUE (EMAIL)
);


CREATE TABLE IF NOT EXISTS LIKES
(
    LIKE_ID INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    USER_ID INT NOT NULL,
    FILM_ID INT NOT NULL,
    CONSTRAINT pk_Like PRIMARY KEY (LIKE_ID)
);

CREATE TABLE IF NOT EXISTS FRIENDSHIP
(
    FRIENDSHIP_ID INT  NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    USER_ID       INT  NOT NULL,
    FRIEND_ID     INT  NOT NULL,
    STATUS        BOOL NOT NULL,
    CONSTRAINT pk_Friendship PRIMARY KEY (FRIENDSHIP_ID)
);

ALTER TABLE FILMS
    ADD CONSTRAINT IF NOT EXISTS fk_Film_RatingID FOREIGN KEY (RATING_ID)
        REFERENCES RATING_MPA (RATING_ID) ON DELETE RESTRICT;

ALTER TABLE LIKES
    ADD CONSTRAINT IF NOT EXISTS fk_Like_FilmID FOREIGN KEY (FILM_ID)
        REFERENCES FILMS (FILM_ID) ON DELETE CASCADE;

ALTER TABLE LIKES
    ADD CONSTRAINT IF NOT EXISTS fk_Like_UserID FOREIGN KEY (USER_ID)
        REFERENCES USERS (USER_ID);

ALTER TABLE FRIENDSHIP
    ADD CONSTRAINT IF NOT EXISTS fk_Friendship_UserID FOREIGN KEY (USER_ID)
        REFERENCES USERS (USER_ID);

ALTER TABLE FRIENDSHIP
    ADD CONSTRAINT IF NOT EXISTS fk_Friendship_FriendID FOREIGN KEY (FRIEND_ID)
        REFERENCES USERS (USER_ID);

ALTER TABLE GENRE_LINE
    ADD CONSTRAINT IF NOT EXISTS fk_GenreLine_FilmID FOREIGN KEY (FILM_ID)
        REFERENCES FILMS (FILM_ID) ON DELETE CASCADE;

ALTER TABLE GENRE_LINE
    ADD CONSTRAINT IF NOT EXISTS fk_GenreLine_GenreID FOREIGN KEY (GENRE_ID)
        REFERENCES GENRES (GENRE_ID) ON DELETE RESTRICT;

ALTER TABLE GENRE_LINE
    ADD CONSTRAINT IF NOT EXISTS UC_GenreLine_GenreID_FilmID UNIQUE (GENRE_ID, FILM_ID)

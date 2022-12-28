DROP ALL OBJECTS;
create table IF NOT EXISTS USERS
(
    USER_ID  INTEGER auto_increment,
    EMAIL    CHARACTER VARYING(50) not null,
    LOGIN    CHARACTER VARYING(50) not null,
    NAME     CHARACTER VARYING(50),
    BIRTHDAY DATE                  not null,
    constraint USERS_PK
        primary key (USER_ID)
);

create table IF NOT EXISTS FRIENDS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    constraint FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            ON DELETE CASCADE,
    constraint FRIENDS_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS DIRECTORS
(
    DIRECTOR_ID   INTEGER auto_increment,
    NAME          CHARACTER VARYING(50) not null,
    CONSTRAINT directors_pk PRIMARY KEY (DIRECTOR_ID)
);

CREATE TABLE IF NOT EXISTS MPA (
    MPA_ID INTEGER auto_increment,
    NAME   CHARACTER VARYING(50) not null,
    constraint "MPA_pk"
        primary key (MPA_ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment,
    NAME         CHARACTER VARYING(200) not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    MPA_ID       INTEGER                not null,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
            ON DELETE CASCADE
);

create table IF NOT EXISTS FILM_LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint FILMLIKES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
            ON DELETE CASCADE,
    constraint FILMLIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            ON DELETE CASCADE
);

create table IF NOT EXISTS FILM_DIRECTOR
(
    film_id     INTEGER NOT NULL,
    director_id INTEGER NOT NULL,
    CONSTRAINT pk_film_director
        PRIMARY KEY (film_id, director_id),
    CONSTRAINT fk_film_director_film_id
        FOREIGN KEY (film_id) REFERENCES films
            ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_film_director_director_id
        FOREIGN KEY (director_id) REFERENCES DIRECTORS
            ON UPDATE CASCADE ON DELETE CASCADE
);

create table IF NOT EXISTS GENRE
(
    GENRE_ID INTEGER auto_increment,
    NAME     CHARACTER VARYING(50) not null,
    constraint "GENRE_pk"
        primary key (GENRE_ID)

);

create table IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRE_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
            ON DELETE CASCADE,
    constraint FILM_GENRE_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
);

create table IF NOT EXISTS FEED
(
    EVENT_ID   INTEGER auto_increment,
    ENTITY_ID  INTEGER               not null,
    USER_ID    INTEGER               not null,
    TIME_STAMP LONG                  not null,
    EVENT_TYPE CHARACTER VARYING(10) not null,
    OPERATION  CHARACTER VARYING(10) not null,
    constraint "FEED_pk"
        primary key (EVENT_ID),
    constraint FEED_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade
);

create table IF NOT EXISTS REVIEWS
(
    REVIEW_ID   INTEGER auto_increment,
    CONTENT     CHARACTER VARYING not null,
    IS_POSITIVE BOOLEAN           not null,
    USER_ID     INTEGER           not null,
    FILM_ID     INTEGER           not null,
    constraint "REVIEWS_pk"
        primary key (REVIEW_ID),
    constraint "REVIEWS_FILMS_null_fk"
        foreign key (FILM_ID) references FILMS
        on delete cascade,
    constraint REVIEWS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
        on delete cascade
);

create table IF NOT EXISTS REVIEW_USER
(
    REVIEW_ID INTEGER not null,
    USER_ID   INTEGER not null,
    IS_USEFUL INTEGER not null,
    constraint REVIEW_USER_REVIEWS_REVIEW_ID_FK
        foreign key (REVIEW_ID) references REVIEWS
            on delete cascade,
    constraint REVIEW_USER_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade
);
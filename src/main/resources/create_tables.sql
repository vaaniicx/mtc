CREATE TABLE IF NOT EXISTS mtc.public.user
(
    uuid      uuid PRIMARY KEY NOT NULL,
    username  varchar          NOT NULL,
    password  varchar          NOT NULL,
    balance   int8 default 0,
    deck      varchar,
    name      varchar,
    biography varchar,
    image     varchar,
    elo       int4 default 100,
    wins      int4 default 0,
    losses    int4 default 0
);

CREATE TABLE IF NOT EXISTS mtc.public.card
(
    uuid       uuid PRIMARY KEY NOT NULL,
    name       varchar          NOT NULL,
    damage     int4 default 0,
    user_uuid  uuid,
    deck       bool default false,
    package_id int4             NOT NULL
);
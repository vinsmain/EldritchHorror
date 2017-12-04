PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM expansions;

DROP TABLE expansions;

CREATE TABLE expansions (
    _id            INTEGER PRIMARY KEY AUTOINCREMENT
                           UNIQUE,
    image_resource STRING,
    name           STRING
);

INSERT INTO expansions (
                           _id,
                           image_resource,
                           name
                       )
                       SELECT _id,
                              image_resource,
                              name
                         FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;

PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM ancient_ones;

DROP TABLE ancient_ones;

CREATE TABLE ancient_ones (
    _id            INTEGER PRIMARY KEY AUTOINCREMENT,
    image_resource STRING,
    name           STRING,
    expansion_id   INTEGER
);

INSERT INTO ancient_ones (
                             _id,
                             image_resource,
                             name,
                             expansion_id
                         )
                         SELECT _id,
                                image_resource,
                                name,
                                expansion_id
                           FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;

PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM investigators;

DROP TABLE investigators;

CREATE TABLE investigators (
    _id            INTEGER PRIMARY KEY AUTOINCREMENT,
    game_id        INTEGER,
    image_resource STRING,
    is_male        BOOLEAN,
    name           STRING,
    occupation     STRING,
    is_starting    BOOLEAN,
    is_replacement BOOLEAN,
    is_dead        BOOLEAN,
    expansion_id   INTEGER
);

INSERT INTO investigators (
                              _id,
                              game_id,
                              image_resource,
                              is_male,
                              name,
                              occupation,
                              is_starting,
                              is_replacement,
                              is_dead,
                              expansion_id
                          )
                          SELECT _id,
                                 game_id,
                                 image_resource,
                                 is_male,
                                 name,
                                 occupation,
                                 is_starting,
                                 is_replacement,
                                 is_dead,
                                 expansion_id
                            FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;

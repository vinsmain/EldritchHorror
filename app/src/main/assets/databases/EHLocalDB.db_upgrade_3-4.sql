PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT *
                                          FROM investigators;

DROP TABLE investigators;

CREATE TABLE investigators (
    _id            INTEGER PRIMARY KEY,
    game_id        INTEGER,
    image_resource STRING,
    is_male        BOOLEAN,
    name           STRING,
    occupation     STRING,
    is_starting    BOOLEAN,
    is_replacement BOOLEAN,
    is_dead        BOOLEAN
);

INSERT INTO investigators (
                              _id,
                              image_resource,
                              name,
                              occupation
                          )
                          SELECT _id,
                                 image_resource,
                                 name,
                                 occupation
                            FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;

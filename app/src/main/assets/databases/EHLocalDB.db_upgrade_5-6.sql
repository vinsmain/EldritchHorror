PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT * FROM investigators;

DROP TABLE investigators;

CREATE TABLE investigators (
    _id            BIGINT  PRIMARY KEY,
    game_id        BIGINT,
    image_resource STRING,
    is_male        BOOLEAN,
    name_en        STRING,
    name_ru        STRING,
    occupation_en  STRING,
    occupation_ru  STRING,
    is_starting    BOOLEAN,
    is_replacement BOOLEAN,
    is_dead        BOOLEAN,
    expansion_id   INTEGER,
    specialization_id INTEGER
);

INSERT INTO investigators (
                              _id,
                              game_id,
                              image_resource,
                              is_male,
                              name_en,
                              name_ru,
                              occupation_en,
                              occupation_ru,
                              is_starting,
                              is_replacement,
                              is_dead,
                              expansion_id
                          )
                          SELECT _id,
                                 game_id,
                                 image_resource,
                                 is_male,
                                 name_en,
                                 name_ru,
                                 occupation_en,
                                 occupation_ru,
                                 is_starting,
                                 is_replacement,
                                 is_dead,
                                 expansion_id
                            FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;

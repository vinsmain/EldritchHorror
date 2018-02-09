PRAGMA foreign_keys = 0;

CREATE TABLE sqlitestudio_temp_table AS SELECT * FROM ancient_ones;

DROP TABLE ancient_ones;

CREATE TABLE ancient_ones (
    _id            INTEGER PRIMARY KEY AUTOINCREMENT,
    image_resource STRING,
    name_en        STRING,
    name_ru        STRING,
    expansion_id   INTEGER,
    max_mysteries INTEGER
);

INSERT INTO ancient_ones (
                             _id,
                             image_resource,
                             name_en,
                             name_ru,
                             expansion_id
                         )
                         SELECT _id,
                                image_resource,
                                name_en,
                                name_ru,
                                expansion_id
                           FROM sqlitestudio_temp_table;

DROP TABLE sqlitestudio_temp_table;

PRAGMA foreign_keys = 1;

UPDATE 'ancient_ones' SET max_mysteries = 4 WHERE _id = 1;
UPDATE 'ancient_ones' SET max_mysteries = 3 WHERE _id = 2;
UPDATE 'ancient_ones' SET max_mysteries = 4 WHERE _id = 3;
UPDATE 'ancient_ones' SET max_mysteries = 4 WHERE _id = 4;
UPDATE 'ancient_ones' SET max_mysteries = 4 WHERE _id = 5;
UPDATE 'ancient_ones' SET max_mysteries = 4 WHERE _id = 6;
UPDATE 'ancient_ones' SET max_mysteries = 4 WHERE _id = 7;
UPDATE 'ancient_ones' SET max_mysteries = 5 WHERE _id = 8;
UPDATE 'ancient_ones' SET max_mysteries = 4 WHERE _id = 9;
UPDATE 'ancient_ones' SET max_mysteries = 4 WHERE _id = 10;
UPDATE 'ancient_ones' SET max_mysteries = 3 WHERE _id = 11;
UPDATE 'ancient_ones' SET max_mysteries = 4 WHERE _id = 12;
UPDATE 'ancient_ones' SET max_mysteries = 4 WHERE _id = 13;
UPDATE 'ancient_ones' SET max_mysteries = 3 WHERE _id = 14;
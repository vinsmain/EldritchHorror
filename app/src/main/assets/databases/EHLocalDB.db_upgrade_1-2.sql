PRAGMA foreign_keys = 0;

CREATE TABLE preludes (
    _id          INTEGER PRIMARY KEY
                         UNIQUE
                         NOT NULL,
    name_en      STRING,
    name_ru      STRING,
    expansion_id INTEGER
);

INSERT INTO preludes (
                         expansion_id,
                         name_ru,
                         name_en,
                         _id
                     )
                     VALUES (
                         3,
                         'Rumors from the North',
                         'Rumors from the North',
                         1
                     ),
                     (
                         3,
                         'Key to Salvation',
                         'Key to Salvation',
                         2
                     ),
                     (
                         3,
                         'Unwilling Sacrifice',
                         'Unwilling Sacrifice',
                         3
                     ),
                     (
                         3,
                         'Beginning of the End',
                         'Beginning of the End',
                         4
                     ),
                     (
                         3,
                         'Doomsayer from Antarctica',
                         'Doomsayer from Antarctica',
                         5
                     ),
                     (
                         3,
                         'Ultimate Sacrifice',
                         'Ultimate Sacrifice',
                         6
                     ),
                     (
                         5,
                         'Ghost from The Past',
                         'Ghost from The Past',
                         7
                     ),
                     (
                         5,
                         'Drastic Measures',
                         'Drastic Measures',
                         8
                     ),
                     (
                         5,
                         'Call of Cthulhu',
                         'Call of Cthulhu',
                         9
                     ),
                     (
                         5,
                         'Litany of Secrets',
                         'Litany of Secrets',
                         10
                     ),
                     (
                         5,
                         'Epidemic',
                         'Epidemic',
                         11
                     ),
                     (
                         5,
                         'Under the Pyramids',
                         'Under the Pyramids',
                         12
                     ),
                     (
                         6,
                         'Weakness to Strength',
                         'Weakness to Strength',
                         13
                     ),
                     (
                         6,
                         'Sins of the Past',
                         'Sins of the Past',
                         14
                     ),
                     (
                         6,
                         'Silver Twilight Stockpile',
                         'Silver Twilight Stockpile',
                         15
                     ),
                     (
                         6,
                         'The King in Yellow',
                         'The King in Yellow',
                         16
                     ),
                     (
                         4,
                         'Dark Blessings',
                         'Dark Blessings',
                         17
                     ),
                     (
                         4,
                         'The Coming Storm',
                         'The Coming Storm',
                         18
                     ),
                     (
                         4,
                         'The Dunwich Horror',
                         'The Dunwich Horror',
                         19
                     ),
                     (
                         4,
                         'In Cosmic Alignment',
                         'In Cosmic Alignment',
                         20
                     ),
                     (
                         7,
                         'Written in the Stars',
                         'Written in the Stars',
                         21
                     ),
                     (
                         7,
                         'Lurker Among Us',
                         'Lurker Among Us',
                         22
                     ),
                     (
                         7,
                         'Twin Blasphemies of the Black Goat',
                         'Twin Blasphemies of the Black Goat',
                         23
                     ),
                     (
                         7,
                         'Web Between Worlds',
                         'Web Between Worlds',
                         24
                     ),
                     (
                         7,
                         'Otherworldly Dreams',
                         'Otherworldly Dreams',
                         25
                     ),
                     (
                         7,
                         'Focused Training',
                         'Focused Training',
                         26
                     ),
                     (
                         8,
                         'Apocalypse Nigh',
                         'Apocalypse Nigh',
                         27
                     ),
                     (
                         8,
                         'Fall of Man',
                         'Fall of Man',
                         28
                     ),
                     (
                         8,
                         'The Price of Prestige',
                         'The Price of Prestige',
                         29
                     ),
                     (
                         8,
                         'You Know What You Must Do',
                         'You Know What You Must Do',
                         30
                     );





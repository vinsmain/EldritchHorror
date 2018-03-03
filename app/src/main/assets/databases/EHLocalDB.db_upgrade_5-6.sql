INSERT INTO preludes (
                         expansion_id,
                         name_ru,
                         name_en,
                         _id
                     )
                     VALUES (
                         9,
                         'Aid of the Elder Gods',
                         'Aid of the Elder Gods',
                         31
                     ),
                     (
                         9,
                         'The Archives',
                         'The Archives',
                         32
                     ),
                     (
                         9,
                         'Army of Darkness',
                         'Army of Darkness',
                         33
                     ),
                     (
                         9,
                         'Father of Serpents',
                         'Father of Serpents',
                         34
                     ),
                     (
                         9,
                         'Harbinger of the Outer Gods',
                         'Harbinger of the Outer Gods',
                         35
                     ),
                     (
                         9,
                         'In the Lightless Chamber',
                         'In the Lightless Chamber',
                         36
                     ),
                     (
                         9,
                         'The Stars Align',
                         'The Stars Align',
                         37
                     ),
                     (
                         9,
                         'Temptation',
                         'Temptation',
                         38
                     ),
                     (
                         9,
                         'Unto the Breach',
                         'Unto the Breach',
                         39
                     ),
                     (
                         9,
                         'Wondrous Curios',
                         'Wondrous Curios',
                         40
                     );


INSERT INTO investigators (
                              specialization_id,
                              expansion_id,
                              is_dead,
                              is_replacement,
                              is_starting,
                              occupation_ru,
                              occupation_en,
                              name_ru,
                              name_en,
                              is_male,
                              image_resource,
                              game_id,
                              _id
                          )
                          VALUES (
                              5,
                              9,
                              NULL,
                              NULL,
                              NULL,
                              'Парапсихолог',
                              'Parapsychologist',
                              'Агата Крэйн',
                              'Agatha Crane',
                              0,
                              'agatha_crane',
                              NULL,
                              49
                          ),
                          (
                              1,
                              9,
                              NULL,
                              NULL,
                              NULL,
                              'Проклятый',
                              'The Damned',
                              'Кэлвин Райт',
                              'Calvin Wright',
                              1,
                              'calvin_wright',
                              NULL,
                              50
                          ),
                          (
                              1,
                              9,
                              NULL,
                              NULL,
                              NULL,
                              'Дворецкий',
                              'The Butler',
                              'Карсон Синклер',
                              'Carson Sinclair',
                              1,
                              'carson_sinclair',
                              NULL,
                              51
                          ),
                          (
                              2,
                              9,
                              NULL,
                              NULL,
                              NULL,
                              'Механик',
                              'The Mechanic',
                              'Даниэла Рейес',
                              'Daniela Reyes',
                              0,
                              'daniela_reyes',
                              NULL,
                              52
                          ),
                          (
                              4,
                              9,
                              NULL,
                              NULL,
                              NULL,
                              'Священник',
                              'The Priest',
                              'Отец Матео',
                              'Father Mateo',
                              1,
                              'father_mateo',
                              NULL,
                              53
                          ),
                          (
                              7,
                              9,
                              NULL,
                              NULL,
                              NULL,
                              'Миллионер',
                              'The Millionaire',
                              'Престон Фэрмонт',
                              'Preston Fairmont',
                              1,
                              'preston_fairmont',
                              NULL,
                              54
                          ),
                          (
                              6,
                              9,
                              NULL,
                              NULL,
                              NULL,
                              'Художник',
                              'The Painter',
                              'Сефина Руссо',
                              'Sefina Rousseau',
                              0,
                              'sefina_rousseau',
                              NULL,
                              55
                          );

INSERT INTO expansions (
                           is_enable,
                           name_ru,
                           name_en,
                           image_resource,
                           _id
                       )
                       VALUES (
                           1,
                           'Маски Ньярлатотепа',
                           'Masks of Nyarlathotep',
                           'masks_of_nyarlathotep',
                           9
                       );


INSERT INTO ancient_ones (
                             max_mysteries,
                             expansion_id,
                             name_ru,
                             name_en,
                             image_resource,
                             _id
                         )
                         VALUES (
                             3,
                             9,
                             'Ньярлатотеп',
                             'Nyarlathotep',
                             'nyarlathotep',
                             15
                         ),
                         (
                             4,
                             9,
                             'Допотопный',
                             'Antediluvium',
                             'antediluvium',
                             16
                         );
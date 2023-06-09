TRUNCATE TABLE priority CASCADE;
TRUNCATE TABLE stat CASCADE;
TRUNCATE TABLE category CASCADE;
TRUNCATE TABLE task CASCADE;

INSERT INTO priority
VALUES (56, 'Низкий', '#caffdd'),
       (57, 'Средний', '#883bdc'),
       (58, 'Высокий', '#f05f5f'),
       (59, 'Запредельно низкий', '#fff'),
       (60, 'вЗапредельно высокий', '#fff');

INSERT INTO category (id, title)
VALUES (167, 'Семья'),
       (168, 'Работа'),
       (170, 'Отдых'),
       (171, 'Путешествия'),
       (179, 'Спорт'),
       (180, 'Здоровье'),
       (182, 'Новая категория');

INSERT INTO task
VALUES (328, 'Позвонить родителям', 1, '2020-04-29 15:27:11', 58, 167),
       (331, 'Посмотреть мультики', 0, '2020-04-27 15:27:29', 57, 167),
       (333, 'Пройти курсы по Java', 0, '2020-04-30 09:38:39', 56, NULL),
       (338, 'Сделать зеленый коктейль', 0, '2020-04-27 15:27:34', 56, 180),
       (339, 'Купить буханку хлеба', 0, '2020-04-28 07:03:03', 57, NULL),
       (341, 'Позвонить начальнику', 0, '2020-05-06 09:38:23', NULL, 168),
       (342, 'Померить давление', 0, '2020-05-01 09:38:46', NULL, 180),
       (343, 'Начать бегать по утрам', 1, NULL, 56, 179),
       (344, 'Отжаться 100 раз', 1, NULL, 58, 179),
       (349, 'Найти развивающие игры для детей', 0, '2020-04-29 09:38:51', 57, 167),
       (350, 'Купить лекарство', 1, '2020-04-30 09:38:43', 56, 180),
       (351, 'Выучить Kotlin', 0, '2020-05-06 09:38:37', 58, NULL),
       (352, 'Посмотреть ролики как построить дом', 1, NULL, NULL, NULL),
       (353, 'Посмотреть сериал', 0, '2020-04-29 09:38:29', NULL, 170),
       (354, 'Съездить на природу', 0, '2020-04-15 18:00:00', NULL, 170),
       (355, 'Создать список стран для путешествий', 1, '2020-04-29 09:38:26', NULL, 171),
       (356, 'Доделать отчеты', 1, '2020-04-30 09:38:20', NULL, 168),
       (358, 'Задача по категории', 0, '2020-05-01 12:01:18', 58, 170);

insert into stat (id, completed_total, uncompleted_total)
values (1, (select count(*) from task where completed = 1), (select count(*) from task where completed = 0));
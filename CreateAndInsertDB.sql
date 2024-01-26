
CREATE
DATABASE IF NOT EXISTS db_sekolah;


USE
db_sekolah;


CREATE TABLE IF NOT EXISTS t_nilai
(
    NIRM
    VARCHAR
(
    9
),
    KodeMK VARCHAR
(
    5
),
    Grade INT
(
    11
),
    PRIMARY KEY
(
    NIRM,
    KodeMK
)
    );

CREATE TABLE IF NOT EXISTS t_mahasiswa
(
    NIRM
    VARCHAR
(
    9
) PRIMARY KEY,
    NamaMahasiswa VARCHAR
(
    50
),
    TglLahir DATE
    );

CREATE TABLE IF NOT EXISTS t_matakuliah
(
    KodeMK
    VARCHAR
(
    5
) PRIMARY KEY,
    NamaMataKuliah VARCHAR
(
    50
),
    Pengajar VARCHAR
(
    50
)
    );


INSERT INTO t_mahasiswa (NIRM, NamaMahasiswa, TglLahir)
VALUES ('213111234', 'Natasha', '1990-01-01'),
       ('213111235', 'Erens', '1992-04-09'),
       ('213111236', 'Kristian', '1994-07-18'),
       ('213111237', 'Hana', '1996-10-27');


INSERT INTO t_matakuliah (KodeMK, NamaMataKuliah, Pengajar)
VALUES ('MK-01', 'Matematika', 'Kent'),
       ('MK-03', 'Statistika', 'Chloe'),
       ('MK-05', 'Aljabar', 'Kenny'),
       ('MK-07', 'Pancasila', 'Linda');


INSERT INTO t_nilai (NIRM, KodeMK, Grade)
VALUES ('213111234', 'MK-01', 70),
       ('213111235', 'MK-01', 40),
       ('213111236', 'MK-03', 80),
       ('213111234', 'MK-05', 78);

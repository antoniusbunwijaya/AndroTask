SELECT
    t_nilai.KodeMK AS KodeMK,
    t_matakuliah.NamaMataKuliah AS NamaMataKuliah,
    REPLACE(CAST(AVG(t_nilai.Grade) AS CHAR), '.0000', '') AS RataRataNilai
FROM
    t_nilai
        INNER JOIN
    t_matakuliah ON t_nilai.KodeMK = t_matakuliah.KodeMK
GROUP BY
    t_nilai.KodeMK, t_matakuliah.NamaMataKuliah;

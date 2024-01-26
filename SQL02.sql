SELECT *
FROM t_matakuliah
         LEFT JOIN t_nilai ON t_matakuliah.KodeMK = t_nilai.KodeMK
WHERE t_nilai.NIRM IS NULL;

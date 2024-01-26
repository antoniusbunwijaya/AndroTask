SELECT
    t_nilai.KodeMK AS KodeMK,
    t_matakuliah.NamaMataKuliah AS NamaMataKuliah,
    COUNT(t_nilai.NIRM) AS JumlahMahasiswa
FROM
    t_nilai
        INNER JOIN
    t_matakuliah ON t_nilai.KodeMK = t_matakuliah.KodeMK
GROUP BY
    t_nilai.KodeMK, t_matakuliah.NamaMataKuliah
ORDER BY
    JumlahMahasiswa DESC
LIMIT 1;

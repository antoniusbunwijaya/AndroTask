SELECT
    tm.NamaMahasiswa,
    tm.TglLahir,
    tmk.NamaMataKuliah,
    tn.Grade
FROM
    t_mahasiswa tm
        INNER JOIN t_nilai tn ON tm.NIRM = tn.NIRM
        INNER JOIN t_matakuliah tmk ON tn.KodeMK = tmk.KodeMK
WHERE
    tm.TglLahir <= DATE_SUB(CURDATE(), INTERVAL 25 YEAR)
  AND tn.Grade < 60;

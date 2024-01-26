SELECT DISTINCT tm.NamaMahasiswa
FROM t_mahasiswa tm
         INNER JOIN t_nilai tn ON tm.NIRM = tn.NIRM
         INNER JOIN t_matakuliah tmk ON tn.KodeMK = tmk.KodeMK
WHERE tmk.NamaMataKuliah IN ('Matematika', 'Aljabar');
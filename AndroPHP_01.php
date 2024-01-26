<?php
session_start();

if (!isset($_SESSION['login']) || $_SESSION['login'] !== true) {
    header("Location: AndroPHP_02.php");
    exit;
}

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Form</title>
</head>
<body>
<h2>Form</h2>

<form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>">
    <table>
        <tr>
            <td>Nama:</td>
            <td><input type="text" name="nama" pattern="[A-Za-z\s]+" title="Nama hanya boleh berisi huruf" required>
            </td>
        </tr>
        <tr>
            <td>Alamat:</td>
            <td><input type="text" name="alamat" required></td>
        </tr>
        <tr>
            <td>No Telepon:</td>
            <td><input type="text" name="telepon" pattern="[0-9]+" title="Nomor telepon hanya boleh berisi angka"
                       required></td>
        </tr>
        <tr>
            <td>Jenis Kelamin:</td>
            <td>
                <select name="jenis_kelamin" required>
                    <option value="">Pilih</option>
                    <option value="Laki-laki">Laki-laki</option>
                    <option value="Perempuan">Perempuan</option>
                </select>
            </td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" name="submit" value="Submit"></td>
        </tr>
    </table>

</form>

<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $nama = $_POST["nama"];
    $alamat = $_POST["alamat"];
    $telepon = $_POST["telepon"];
    $jenis_kelamin = $_POST["jenis_kelamin"];

    echo "<p>Formulir telah berhasil disubmit</p>";

}

?>

</body>
</html>

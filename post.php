<?php

// DB Connection Parameters
$host = 'depokiix.cepatcloud.co.id';
$port = '3306';
$database = 'situsfor_db_form_pendaftaran';
$username = 'situsfor_androtest';
$password = 'androtest123';

// Connect to MySQL database
$mysqli = new mysqli($host, $username, $password, $database);

// Check connection
if ($mysqli->connect_errno) {
    die("Connection failed: " . $mysqli->connect_error);
}

// Get data from Android app
$schoolName = $_POST['schoolName'];
$address = $_POST['address'];
$postalCode = $_POST['postalCode'];
$phoneNumber = $_POST['phoneNumber'];
$email = $_POST['email'];
$numberOfStudents = $_POST['numberOfStudents'];
$schoolType = $_POST['schoolType'];
$province = $_POST['province'];
$city = $_POST['city'];
//$imageData = $_POST['imageData']; // Assuming imageData is sent as base64 string

// // Function to handle image upload and return the path
// function uploadImage($imageData) {
//     $img = base64_decode($imageData);
//     $imageName = uniqid() . '.jpeg'; // Generate unique image name
//     $targetPath = 'uploads/' . $imageName; // Define target path
//     file_put_contents($targetPath, $img); // Save image to server
//     return $targetPath;
// }

//// Call uploadImage function to upload and get the image path
//$imagePath = uploadImage($imageData);

// Insert data into MySQL database
$sql = "INSERT INTO tsekolah (nama_sekolah, alamat, kode_pos, nomor_telepon, email, jumlah_siswa, jenis_sekolah, provinsi, kota) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
$stmt = $mysqli->prepare($sql);
$stmt->bind_param("sssssssss", $schoolName, $address, $postalCode, $phoneNumber, $email, $numberOfStudents, $schoolType, $province, $city);

if ($stmt->execute()) {
    $response["success"] = 1;
    $response["message"] = "Data berhasil disimpan di database.";
} else {
    $response["success"] = 0;
    $response["message"] = "Gagal menyimpan data.";
}

echo json_encode($response);

// Close database connection
$stmt->close();
$mysqli->close();

?>

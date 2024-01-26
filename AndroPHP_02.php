<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
</head>
<body>
<h2>Login</h2>

<form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>">
    <table>
        <tr>
            <td>Username:</td>
            <td><input type="text" name="username" required></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password" name="password" required></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" name="submit" value="Login"></td>
        </tr>
    </table>

</form>

<?php
session_start();

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = $_POST["username"];
    $password = $_POST["password"];

    $pecahPassword = str_split($password);

    $panjang = strlen($username);

    $balikanPassword = '';

    for ($i = $panjang - 1; $i >= 0; $i--) {
        $balikanPassword .= $pecahPassword[$i];
    }

    if ($balikanPassword === $username) {
        $_SESSION["login"] = true;
        header("Location: AndroPHP_01.php");
    } else {
        echo "<p>Login gagal. Password harus merupakan kebalikan dari username.</p>";
    }
}
?>

</body>
</html>

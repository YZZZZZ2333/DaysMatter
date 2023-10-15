<?php
//修改星标
header('content-type:application/json');
//数据库连接
$conf = include("config.php");
$conn1 = new mysqli($conf['DB_ADDR'], $conf['DB_USER'], $conf['DB_PWD'], $conf['DB_NAME']);
$conn2 = new mysqli($conf['DB_ADDR'], $conf['DB_USER'], $conf['DB_PWD'], $conf['DB_NAME']);
if ($conn1->connect_error) {
    $arr = array("status" => "2", "msg" => "服务器错误");
    $json = json_encode($arr);
    echo $json;
    exit();
}
$usrid = $_POST["usrid"];
$token = $_POST["token"];
$rid = $_POST["rid"];
$star = $_POST["star"];
$sql1 = mysqli_prepare($conn1, "SELECT * FROM token WHERE token=? AND usrid=?");
$sql1->bind_param("ss", $token, $usrid);
$sql1->execute();
if ($sql1->fetch()) {                      //身份校验
    $sql1->close();
    $sql2 = mysqli_prepare($conn2, "UPDATE data SET star=? WHERE id='$rid' AND usrid='$usrid'");
    $sql2->bind_param("s", $star);
    $sql2->execute();
    $sql2->close();
    $arr = array("status" => "0", "msg" => "修改成功");
} else {
    $sql1->close();
    $arr = array("status" => "1", "msg" => "身份验证失败");
}
$json = json_encode($arr);
echo $json;
$conn1->close();
$conn2->close();

<?php
//拉取数据
header('content-type:application/json');
//数据库连接
$conf = include("config.php");
$conn1 = new mysqli($conf['DB_ADDR'], $conf['DB_USER'], $conf['DB_PWD'], $conf['DB_NAME']);
$conn2 = new mysqli($conf['DB_ADDR'], $conf['DB_USER'], $conf['DB_PWD'], $conf['DB_NAME']);
$data = array();
if ($conn1->connect_error) {
    $arr = array("status"=>"2","msg"=>"服务器错误","count"=>"0","data"=>"0");
    $json = json_encode($arr);
    echo $json;
    exit();
}
$usrid = $_POST["usrid"];
$token = $_POST["token"];
$sql = mysqli_prepare($conn1,"SELECT * FROM token WHERE token=? AND usrid=?");
$sql->bind_param("ss",$token,$usrid);
$sql->execute();
if ($sql->fetch()){
    $count=0;
    $sql2="select * from data where usrid=$usrid";
    $res2=mysqli_query($conn2,$sql2);
    while ($row2=mysqli_fetch_assoc($res2)){
        $data[$count]=$row2;
        $count++;
    }
    $arr = array("status"=>"0","msg"=>"读取成功","count"=>$count,"data"=>$data);
    $json = json_encode($arr);
    echo $json;
}
else {
    $arr = array("status"=>"1","msg"=>"身份验证失败","count"=>"0","data"=>"0");
    $json = json_encode($arr);
    echo $json;
}
$conn1->close();
$conn2->close();

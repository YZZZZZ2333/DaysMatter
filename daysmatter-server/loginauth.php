<?php
//登录认证
header('content-type:application/json');
//数据库连接
$conf = include("config.php");
$conn1 = new mysqli($conf['DB_ADDR'], $conf['DB_USER'], $conf['DB_PWD'], $conf['DB_NAME']);
$conn2 = new mysqli($conf['DB_ADDR'], $conf['DB_USER'], $conf['DB_PWD'], $conf['DB_NAME']);
if ($conn1->connect_error) {
    $arr=array("status"=>"2","msg"=>"服务器错误","usrid"=>"0","uname"=>"0","token"=>"0");
    $json=json_encode($arr);
    echo $json;
    exit();
}
//读取登录信息
$email = $_POST["email"];
$pwd = $_POST["pwd"];
$sql = mysqli_prepare($conn1,"SELECT id,pwd,uname FROM users WHERE email=?");
$sql->bind_param("s",$email);
$sql->execute();
$sql->bind_result($id0,$pwd0,$uname);
//验证邮箱和密码
if ($sql->fetch()){
    if ($pwd0==$pwd) {
        $token=md5($uname.$email.$pwd.date('Y-m-d', time()));
        $arr=array("status"=>"0","msg"=>"登录成功","usrid"=>$id0,"uname"=>$uname,"token"=>$token);
        $time=time();
        $sql2="INSERT INTO token VALUES (null,'$id0','$token','$time')";
        $retval=mysqli_query($conn2, $sql2);
        $json=json_encode($arr);
        echo $json;
    }
    else {
        $arr=array("status"=>"1","msg"=>"用户名或密码错误","usrid"=>"0","uname"=>"0","token"=>"0");
        $json=json_encode($arr);
        echo $json;
    }
}
else {
    $arr=array("status"=>"1","msg"=>"用户名或密码错误","usrid"=>"0","uname"=>"0","token"=>"0");
    $json=json_encode($arr);
    echo $json;
}
$conn1->close();
$conn2->close();

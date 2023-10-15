<?php
//与我共享
header('content-type:application/json');
//数据库连接
$conf = include("config.php");
$conn1 = new mysqli($conf['DB_ADDR'], $conf['DB_USER'], $conf['DB_PWD'], $conf['DB_NAME']);
$conn2 = new mysqli($conf['DB_ADDR'], $conf['DB_USER'], $conf['DB_PWD'], $conf['DB_NAME']);
if ($conn1->connect_error) {
    $arr=array("status"=>"2","msg"=>"服务器错误","count"=>"0","data"=>"0");
    $json=json_encode($arr);
    echo $json;
    exit();
}
$usrid=$_POST["usrid"];
$token=$_POST["token"];
$sql1 = mysqli_prepare($conn1,"SELECT * FROM token WHERE token=? AND usrid=?");
$sql1 -> bind_param("ss",$token,$usrid);
$sql1 -> execute();
if ($sql1 -> fetch()){                      //身份校验
    $sql1 -> close();
    $sql2 = "SELECT * FROM shared WHERE destid='$usrid'";                 //从共享表读取终点是自己的条目
    $res2 = mysqli_query($conn2,$sql2);
    $count = 0;
    $data=array();
    while ($row2 = mysqli_fetch_assoc($res2)){
        $detail=array();
        $sql3="SELECT uname FROM users WHERE id=".$row2["sourceid"];    //读取共享者用户名
        $res3=mysqli_query($conn1,$sql3);
        $row3=mysqli_fetch_assoc($res3);
        $sql4="SELECT * FROM data WHERE id=".$row2["contid"];           //读取共享条目详情
        $res4=mysqli_query($conn1,$sql4);
        $row4=mysqli_fetch_assoc($res4);
        $detail["rid"]=$row4["id"];
        $detail["title"]=$row4["title"];
        $detail["name"]=$row3["uname"];
        $detail["dsc"]=$row4["dsc"];
        $detail["typ"]=$row4["typ"];
        $detail["tim"]=$row4["tim"];
        $data[$count]=$detail;
        $count++;
    }
    $arr = array("status"=>"0","msg"=>"读取成功","count"=>$count,"data"=>$data);
}
else{
    $sql1->close();
    $arr = array("status"=>"1","msg"=>"身份验证失败","count"=>"0","data"=>"0");
}
$json=json_encode($arr);
echo $json;
$conn1->close();
$conn2->close();

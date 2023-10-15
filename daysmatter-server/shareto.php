<?php
//共享给指定用户（email地址）或取消共享
header('content-type:application/json');
//数据库连接
$conf = include("config.php");
$conn1 = new mysqli($conf['DB_ADDR'], $conf['DB_USER'], $conf['DB_PWD'], $conf['DB_NAME']);
$conn2 = new mysqli($conf['DB_ADDR'], $conf['DB_USER'], $conf['DB_PWD'], $conf['DB_NAME']);
if ($conn1->connect_error) {
    $arr=array("status"=>"2","msg"=>"服务器错误");
    $json=json_encode($arr);
    echo $json;
    exit();
}
$usrid = $_POST["usrid"];
$token = $_POST["token"];
$dest = $_POST["dest"];
$rid = $_POST["rid"];
$sql1 = mysqli_prepare($conn1,"SELECT * FROM token WHERE token=? AND usrid=?");
$sql1 -> bind_param("ss",$token,$usrid);
$sql1 -> execute();
if ($sql1 -> fetch()){                      //身份校验
    $sql1 -> close();
    $sql2 = mysqli_prepare($conn2, "SELECT id FROM users WHERE email=?");
    $sql2 -> bind_param("s",$dest);
    $sql2 -> execute();
    $sql2 -> bind_result($sid);
    if ($sql2 -> fetch()){                  //取得目标用户id
        $sql2 -> close();
        $sql3 = mysqli_prepare($conn1,"SELECT * FROM shared WHERE sourceid=? AND destid=? AND contid=?");
        $sql3 -> bind_param("sss",$usrid,$sid,$rid);
        $sql3 -> execute();
        if ($sql3 -> fetch()){         //重复检查
            $sql3 -> close();
            $sqlc="DELETE FROM shared WHERE sourceid='$usrid' AND destid='$sid' AND contid='$rid'";
            $retval = mysqli_query($conn2,$sqlc);
            $arr = array("status"=>"0","msg"=>"取消共享成功");
        }
        else{
            $sql3 -> close();
            $sql4 = mysqli_prepare($conn2,"INSERT INTO shared VALUES (null,?,?,?)");
            $sql4 -> bind_param("sss",$usrid,$sid,$rid);
            $sql4 -> execute();
            $sql4 -> close();
            $arr = array("status"=>"0","msg"=>"共享成功");
        }
    }
    else{
        $sql2 -> close();
        $arr = array("status"=>"1","msg"=>"目标用户不存在");
    }
}
else{
    $sql1->close();
    $arr = array("status"=>"1","msg"=>"身份验证失败");
}
$json=json_encode($arr);
echo $json;
$conn1->close();
$conn2->close();
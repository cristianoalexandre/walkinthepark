<?php

header("Content-Type:application/json");

include_once '../settings/base_settings.php';
include_once './database/user.php';

if (isset($_GET['userid'])) {
    $userid = $_GET['userid'];

    $user = getUserInfoByID($userid);

    $id = $user['id'];
    $realname = $user['realname'];
    $email = $user['email'];

    $toReturn = array('id' => $id, 'name' => $realname, 'email' => $email);

    echo json_encode($toReturn);
} else {
    echo json_encode(array()); 
}
?>

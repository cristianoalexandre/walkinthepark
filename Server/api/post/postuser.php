<?php

header("Content-Type:application/json");

include_once '../../settings/base_settings.php';
include_once '../database/user.php';

if (isset($_GET['userid']) && isset($_GET['realname']) && isset($_GET['email']) && isset($_GET['password'])) {
    insertUser($_GET['userid'],$_GET['realname'],$_GET['email'],$_GET['password']);
} else {
    header("HTTP/1.0 400 Bad Request");
}
?>

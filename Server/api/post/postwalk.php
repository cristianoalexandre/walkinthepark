<?php

header("Content-Type:application/json");

include_once '../../settings/base_settings.php';
include_once '../database/walk.php';

if (isset($_GET['userid']) && isset($_GET['name']) && isset($_GET['date']) && isset($_GET['distance']) && isset($_GET['time']) && isset($_GET['elevation']) && isset($_GET['avgspeed'])) {
    $newid = postWalk($_GET['userid'], $_GET['name'], $_GET['date'], $_GET['distance'], $_GET['time'], $_GET['elevation'], $_GET['avgspeed']);
    echo json_encode(array("id" => $newid));
} else {
    header("HTTP/1.0 400 Bad Request");
}

?>
<?php

include_once '../../settings/base_settings.php';
include_once '../database/walk.php';

if (isset($_GET['walkid'])) {
    $walkid = $_GET['walkid'];

    $walk = getWalkInfoByID($walkid);

    echo json_encode($walk['name']);
} else {
    header("HTTP/1.0 404 Not Found");
}
?>

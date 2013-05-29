<?php
function getWaypointInfoByWalkID($walkid) {
    global $db;
    $result = $db->prepare("SELECT * FROM waypoints WHERE walkid = ?");
    $result->execute(array($walkid));
    $waypoint = $result->fetch();

    return $waypoint;
}
?>
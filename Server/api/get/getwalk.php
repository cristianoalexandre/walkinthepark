<?php

include_once '../../settings/base_settings.php';
include_once '../database/walk.php';

if (isset($_GET['walkid'])) {
    $walkid = $_GET['walkid'];

    $walk = getWalkInfoByID($walkid);
    
    $walk_name = $walk['name'];
    $walk_date = $walk['date'];
    $walk_distance = $walk['distance'];
    $walk_time = $walk['time'];
    $walk_elevation = $walk['elevation'];
    $walk_avgspeed = $walk['avgspeed'];

    $to_encode_walk_info = array('name' => $walk_name,
    							'date' => $walk_date,
    							'distance' => $walk_distance,
    							'time' => $walk_time,
    							'elevation' => $walk_elevation,
    							'avgspeed' => $walk_avgspeed);

    $waypoint = getWaypointInfoByWalkID($walkid);

    $waypoints_to_append = array();

    foreach ($waypoint as $row){

    $waypoint_latitude = $row['latitude'];
    $waypoint_longitude = $row['longitude'];
    $waypoint_altitude = $row['altitude'];
    $waypoint_speed = $row['speed'];

    $to_encode_waypoints_info = array('latitude' => $waypoint_latitude,
    						'longitude' => $waypoint_longitude,
    						'altitude' => $waypoint_altitude,
    						'speed' => $waypoint_speed);

    array_push($waypoints_to_append, $to_encode_waypoints_info);

    }

    $final_array = array($to_encode_walk_info,$waypoints_to_append);

    echo json_encode($walk['name'], $walk['']);

} else {
    json_encode(array());
}
?>
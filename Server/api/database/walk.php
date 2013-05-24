<?php
function getWalkInfoByID($walkid) {
    global $db;
    $result = $db->prepare("SELECT * FROM walks WHERE id = ?");
    $result->execute(array($walkid));
    $walk = $result->fetch();

    return $walk;
}

function postWalk($userid, $name, $date, $distance, $time, $elevation, $avgspeed) {
    global $db;
    $result1 = $db->prepare("BEGIN TRANSACTION;");
    $result1->execute();
    
    $result2 = $db->prepare("insert into walks values(NULL,?,?,?,?,?,?);");
    $result2->execute(array($name,$date,$distance,$time,$elevation,$avgspeed));
    
    // need to get the last walkid (and return it, later)!
    
    $result3 = $db->prepare("END TRANSACTION;");
    $result3->execute();
}
?>
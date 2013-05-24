<?php
    function getUserInfoByID($userid) {
        global $db;
        $result = $db->prepare("SELECT * FROM users WHERE id = ?;");
        $result->execute(array($userid));
        $user = $result->fetch();
        
        return $user;
    }
    
    function postUser($userid,$realname,$email,$password) {
        global $db;
        $result = $db->prepare("INSERT INTO users VALUES(?,?,?,?);");
        $result->execute(array($userid,$realname,$email,sha1($password)));
    }
?>

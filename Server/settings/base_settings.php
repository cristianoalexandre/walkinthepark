<?php

/* Here we can add useful vars for the entire application */
$BASE_URL = '/home/cristianoalexandre/public_html/walkinthepark';
/* Defining the database */
try {
    $db = new PDO("sqlite:$BASE_URL/sqldb/maindatabase.sqlite3");
    $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_WARNING);
} catch (PDOException $e) {
    echo ($e->getMessage());
}
?>
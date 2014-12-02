<?php
    
    $useq = $_GET['useq'];
    $long = $_GET['long'];
    $lat = $_GET['lat'];
    
    $dbhost = 'localhost';
    $dbuser = 'root';
    $dbpass = 'lincoln9';

    $mysqli = new mysqli($dbhost, $dbuser, $dbpass, 'test');
    if (mysqli_connect_errno()) {
        printf("Connect failed: %s\n", mysqli_connect_error());
        exit();
    }
    $sqli = "INSERT INTO trackinglocations(userseq,longitude,latitude,dated) VALUES (?,?,?,now()) ";
    
    if (!($stmt = $mysqli->prepare($sqli))) {
        echo "Prepare failed: (" . $mysqli->errno . ") " . $mysqli->error;
    }
    
    $stmt = $mysqli->prepare($sqli);
    $stmt->bind_param("iii",$useq, $long, $lat);
    
    $stmt->execute();
    $error = $stmt->error;
    $stmt->close();
    $mysqli->close();
    if($error != null){
        echo "0";
    }else{
        echo "1";
    }
?>

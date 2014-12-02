<?php
    $dbhost = 'localhost';
    $dbuser = 'root';
    $dbpass = 'lincoln9';
    $dbname = "test";
    
    $userSeq = $_GET['useq'];
    if($userSeq == null){
         echo "{\"data\":null}";
         die;
    }
    
    $conn = new mysqli($dbhost, $dbuser, $dbpass, $dbname);
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    } 

    $sql = "SELECT * FROM trackinglocations where userseq = ". $userSeq;
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
         $dataRows = array();
         $i=0;
         while($row = $result->fetch_assoc()) {
             $data = array();
             $data['longitude'] = $row['longitude'];
             $data['latitude'] = $row['latitude']; 
             $dataRows[$i] = $data;
             $i++; 
         }
    }
    header("Content-type: application/json"); 
    echo "{\"data\":" .json_encode($dataRows). "}";
    
    $conn->close();
?>

<?php

class MailerUtils{

    public static function sendContactEmail($GET){
        $name = $GET['name'];
        $employeeId = $GET['employeeId'];
        $workLocation = $GET['workLocation'];
        $internetSpeed = $GET['internetSpeed'];
        $yourLocation = $GET['yourLocation'];
        $phoneNo = $GET['phoneNo'];
        $emailId = $GET['emailId'];
        $problemDetails = $GET['problemDetails'];

        $txt = "Name: ". $name;
        $txt .= "\nEmployeeId: ". $employeeId;
        $txt .= "\nWork Location: ". $workLocation;
        $txt .= "\nInternet Speed: ". $internetSpeed;
        $txt .= "\nYour Location: ". $yourLocation;
        $txt .= "\nPhone No: ". $phoneNo;
        $txt .= "\nEmail Id: ". $emailId;
        $txt .= "\nProblem: ". $problemDetails;

        $to = "munishsethi777@gmail.com";
        $subject = "Contact Form at EZAE.IN";
        $headers = "From: noreply@ezae.in" . "\r\n" .
    "CC: amandeepdubey@gmail.com";
        mail($to,$subject,$txt,$headers);
        return true;
    }


}
?>

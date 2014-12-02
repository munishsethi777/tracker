<?php
    session_start();
    require_once('IConstants.inc');
    require_once($ConstantsArray['dbServerUrl'] ."Utils/SessionUtil.php");
    $sessionUtil = SessionUtil::getInstance();
    $bool = $sessionUtil->isSessionAdmin();
	if($bool == false)
	{
	    header("location: adminLogin.php");
	}
?>
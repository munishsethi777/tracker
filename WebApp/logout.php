<?php
    require_once('IConstants.inc');
    require_once($ConstantsArray['dbServerUrl'] ."Utils/SessionUtil.php5");
    $sessionUtil = SessionUtil::getInstance();
    $sessionUtil->destroySession();

?>

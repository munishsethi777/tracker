<?php
    require_once('..\\IConstants.inc');
    require_once($ConstantsArray['dbServerUrl'] ."BusinessObjects\\User.php5");
    require_once($ConstantsArray['dbServerUrl'] ."DataStores\\UserDataStore.php5");

    $userImporter = new UserImporter();
    $userImporter->importUsers("G:/Webdocs/EZAsessmentEngine/resources/UshaUsers.txt");

    class UserImporter{
        private static $mainTableFields = array("emailid");
        public function importUsers($filePath){
            $userDataStore = UserDataStore::getInstance();
            $content = file($filePath);
            $totalLines = count($content);
            $fieldNames = explode(',', trim($content[0]));
            for ($i = 1; $i < $totalLines; $i++) {
                $paramsLine = $content[$i];
                $paramsArray = explode(",",$paramsLine);
                $user = new User();
                $user->setUserName($paramsArray[0]);
                //$pass =  substr(str_shuffle("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"), 0, 6);
                $pass = $paramsArray[5];
                $pass = str_replace("/","",$pass);
                $user->setPassword($pass);
                $user->setCompanySeq(1);
                $user->setCreatedOn(new DateTime());
                $user->setIsEnabled(true);
                $customVal = "";
                for($j=0;$j<count($fieldNames);$j++){
                    $customVal .= $fieldNames[$j] .":". $paramsArray[$j] .";";
                }
                $user->setCustomFieldValues($customVal);
                //$userDataStore->save($user);

            }
        }
    }
?>

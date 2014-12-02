<?php
class ChartsUtil{
    private static $ATTEMPTED_NOTATTEMPTED = "attemptedNotAttempted";
    private static $COMPLETED_NOTCOMPLETED = "completedNotCompleted";
    private static $COMPLETED_NOTCOMPLETED_NOTATTEMPTED = "completedNotCompletedNotAttempted";
    private static $GET_OVERALL_INFO = "getOverAllInfo";

    private static $chartsUtil;
    public static function getInstance(){
        if (!self::$chartsUtil)
        {
            self::$chartsUtil = new ChartsUtil();
            return self::$chartsUtil;
        }
        return self::$chartsUtil;
    }

    //called from AdminMgr for Comparative Metrics
    public function getComparativeData($moduleSeq, $customField, $criteria){
        $activityDS = ActivityDataStore::getInstance();
        $dataArr = $activityDS->getUsersAndActivity($moduleSeq);
        $fullArr = array();
        foreach($dataArr as $data ){
            $arr = array();
            $arrCustomFields = ActivityMgr::getCustomValuesArray($data['customfieldvalues']);
            $arr['field'] = $arrCustomFields[$customField];
            $arr['score'] = $data['score'];
            $arr['progress'] = $data['progress'];
            $arr['isCompleted']  = $data['iscompleted'];
            array_push($fullArr,$arr);
        }
        //received data with key as field and value as criteria based value
        //$arr(field=> value, field=> value) where field is unique field
        $dataArr = self::getCriteriaBasedValues($fullArr,$criteria);

        //product format for chart
        $finalArr = array();
        foreach($dataArr as $key=>$value){
            $arr = array();
            $arr['fieldname']  = $key;
            $arr['score'] = round($value);
            array_push($finalArr,$arr);
        }
        return json_encode($finalArr);
    }

    private static function getCriteriaBasedValues($fullArr,$criteria){
            $mainArray = array();
            $array = array();
            foreach($fullArr as $arr){
                $field  = $arr['field'];
                if($criteria == "completePercent"){
                    $val =  $arr['isCompleted'];
                }else{
                    $val =  $arr['score'];
                }

                if($val == null){
                    $val = 0;
                }
                if($array[$field] == null){
                    $array[$field] = array();
                }
                array_push($array[$field],$val);
            }
            foreach($array as $key=>$value){
                $mainArray[$key] = self::getCalculatedValue($value, $criteria);
            }
            return $mainArray;
    }
    private static function getCalculatedValue($array , $criteria){
            switch($criteria){
                case 'average':
                    $count = count($array);
                    $sum = array_sum($array);
                    $total = $sum / $count;
                    $total = round($total);
                break;
                case 'median':
                    rsort($array);
                    $middle = round(count($array) / 2);
                    $total = $array[$middle-1];
                break;
                case 'mode':
                    $v = array_count_values($array);
                    arsort($v);
                    foreach($v as $k => $v){$total = $k; break;}
                break;
                case 'range':
                    sort($array);
                    $sml = $array[0];
                    rsort($array);
                    $lrg = $array[0];
                    $total = $lrg - $sml;
                break;
                case 'completePercent':
                    $count = count($array);
                    $sum = array_sum($array);
                    $total = ($sum / $count) * 100;
                    $total = round($total,2);
                break;
            }
            return $total;
    }

    //called from AdminMgr for Completion Metrics
    public function getCompletionData($moduleSeq, $mode){
        $ads = ActivityDataStore::getInstance();
        $countArr =  $ads->getCompletionCounts($moduleSeq);

        $mainArr = array();
        $arr = array();
        $total = intval($countArr[0][0]);
        $attempted = intval($countArr[0][1]);
        $completed = intval($countArr[0][2]);
        $uncompleted = $attempted -$completed;
        $unAttempted = $total - $attempted;
        $completedPercent = round(($completed * 100)/$total);
        $uncompletedPercent = ($uncompleted * 100)/$total;
        $unattemptedPercent = round(($unAttempted * 100)/$total);

        if($mode == self::$ATTEMPTED_NOTATTEMPTED){
            $arr['Status'] = "Unattempted";
            $arr['Share'] = $unattemptedPercent;
            array_push($mainArr,$arr);
            $arr['Status'] = "Attempted";
            $arr['Share'] = 100 - $unattemptedPercent;
            array_push($mainArr,$arr);

        }else if($mode == self::$COMPLETED_NOTCOMPLETED){
            $arr['Status'] = "Completed";
            $arr['Share'] = $completedPercent;
            array_push($mainArr,$arr);
            $arr['Status'] = "Not Completed";
            $arr['Share'] = 100 - $completedPercent;
            array_push($mainArr,$arr);
        }else if($mode ==  self::$COMPLETED_NOTCOMPLETED_NOTATTEMPTED || $mode == ""){
            $arr['Status'] = "Completed";
            $arr['Share'] = round($completedPercent,2);
            array_push($mainArr,$arr);
            $arr['Status'] = "Not Completed";
            $arr['Share'] = round($uncompletedPercent,2);
            array_push($mainArr,$arr);
            $arr['Status'] = "Not Attempted";
            $arr['Share'] = round($unattemptedPercent,2);
            array_push($mainArr,$arr);
        }else if($mode == self::$GET_OVERALL_INFO){
            $arr['total'] = $total;
            $arr['completed'] = $completed;
            $arr['uncompleted'] = $uncompleted;
            $arr['attempted'] = $attempted;
            $arr['unattempted'] = $unAttempted;
            array_push($mainArr,$arr);
        }
        return json_encode($mainArr);


    }

    //called from AdminMgr for Performance Metrics - pass percentage chart
    public function getPassPercentData($moduleSeq, $percent){
        $ads = ActivityDataStore::getInstance();
        $passCount =  $ads->getPassCountGreaterThanPercentage($moduleSeq,$percent);
        $allCountArr =  $ads->getCompletionCounts($moduleSeq);
        $totalCount = $allCountArr[0][0];
        $passPercent = $passCount/$totalCount * 100;

        $mainArr = array();

        $arr['Status'] = "Passed";
        $arr['Share'] = round($passPercent,2);
        array_push($mainArr,$arr);
        $arr['Status'] = "Failed";
        $arr['Share'] = round(100 - $passPercent,2);
        array_push($mainArr,$arr);
        return json_encode($mainArr);
    }
}//ends class
?>

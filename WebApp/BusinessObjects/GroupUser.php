<?php
   class GroupUser{
      public static $tableName = "groupusers";
      public static $className = "GroupUser"; 
      private $seq,$groupseq,$userseq,$addedon;
      
      public function setSeq($seq_){
          $this->seq = $seq_;
      }
      public function getSeq(){
        return $this->seq;
      }
      
      public function setGroupSeq($groupseq_){
          $this->groupseq = $groupseq_;
      }
      public function getGroupSeq(){
        return $this->groupseq;
      }
      
      public function setUserSeq($userseq_){
          $this->userseq = $userseq_;
      }
      public function getUserSeq(){
        return $this->userseq;
      }
      
      public function setAddedOn($addedon_){
          $this->addedon = $addedon_;
      }
      public function getAddedOn(){
        return $this->addedon;
      }
   }
           
?>

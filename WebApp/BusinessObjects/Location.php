<?php
  class Location{
      public static $tableName = "locations";
      private $seq,$longitude,$latitude,$dated,$userseq;
      
      public function setSeq($seq_){
          $this->seq = $seq_;
      }
      public function getSeq(){
          return $this->seq;
      }
      
      public function setLongitude($longitude_){
          $this->longitude = $longitude_;
      }
      public function getLongitude(){
          return $this->longitude;
      }
      
      public function setLatitude($latitude_){
          $this->latitude = $latitude_;
      }
      public function getLatitude(){
          return $this->latitude;
      }
      public function setDated($dated_){
         $this->dated = $dated_;
      }
      public function getDated(){
          return $this->dated;
      }
      
      public function setUserSeq($userseq_){
          $this->userseq = $userseq_;
      }
      public function getUserSeq(){
          return $this->userseq;
      }
     }
?>

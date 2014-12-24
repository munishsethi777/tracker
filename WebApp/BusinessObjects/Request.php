<?php
  class Request{
      public static $tableName = "requests";
      public static $className = "Request";
      private $seq,$byuser,$touser,$groupseq,$requestdate,$status,$responsedate;

      public function setSeq($seq_){
          $this->seq = $seq_;
      }
      public function getSeq(){
          return $this->seq;
      }

      public function setByuser($byuser_){
          $this->byuser = $byuser_;
      }
      public function getByuser(){
          return $this->byuser;
      }

      public function setTouser($touser_){
          $this->touser = $touser_;
      }
      public function getTouser(){
          return $this->touser;
      }
      public function setGroupSeq($groupseq_){
          $this->groupseq = $groupseq_;
      }
      public function getGroupSeq(){
          return $this->groupseq;
      }

      public function setRequestDate($requestdate_){
          $this->requestdate = $requestdate_;
      }
      public function getRequestDate(){
          return $this->requestdate;
      }

      public function setStatus($status_){
          $this->status = $status_;
      }
      public function getStatus(){
          return $this->status;
      }

      public function setResponseDate($responsedate_){
          $this->responsedate = $responsedate_;
      }
      public function getResponseDate(){
          return $this->requestdate;
      }

  }
?>

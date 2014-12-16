<?php
  class Request{
      private $seq,$byuser,$touser,$circleseq,$requestdate,$status,$responsedate;
      
      public function setSeq($seq_){
          $this->seq = $seq_;
      }
      public function getSeq(){
          return $this->seq;
      }
      
      public function setByuser($byuser_){
          $this->c = $byuser_;
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
      public function setCircleSeq($circleseq_){
          $this->circleseq = $circleseq_;
      }
      public function getCircleSeq(){
          return $this->circleseq;
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

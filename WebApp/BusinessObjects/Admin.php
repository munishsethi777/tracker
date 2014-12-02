<?php
class Admin{

	public static $tableName = "admins";
	private $seq,$mobile,$password,$emailid,$fullname,$createdon,$isenabled;

	public function setSeq($seq_){
		$this->seq = $seq_;
	}
	public function getSeq(){
		return $this->seq;
	}

	public function setMobile($mobile_){
		$this->mobile = $mobile_;
	}
	public function getMobile(){
		return $this->mobile;
	}

	public function setPassword($password_){
		$this->password = $password_;
	}
	public function getPassword(){
		return $this->password;
	}

	public function setEmailId($emailId_){
		$this->emailid = $emailId_;
	}
	public function getEmailId(){
		return $this->emailid;
	}

	public function setFullName($fullName_){
		$this->fullname = $fullName_;
	}
	public function getFullName(){
		return $this->fullname;
	}

	public function setCreatedOn($dateOfJoining_){
		$this->createdon = $dateOfJoining_;
	}
	public function getCreatedOn(){
		return $this->createdon;
	}
	public function setIsEnabled($isEnabled_){
		$this->isenabled = $isEnabled_;
	}
	public function getIsEnabled(){
		return $this->isenabled;
	}


}

?>

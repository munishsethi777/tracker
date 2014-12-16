<?php
class User{

	public static $tableName = "users";
	private $seq,$mobile,$password,$email,$createdon,$isenabled,$isactivated;

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

	public function setEmail($emailId_){
		$this->email = $emailId_;
	}
	public function getEmail(){
		return $this->email;
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
    public function setIsActivated($isActivated_){
        $this->isactivated = $isActivated_;
    }
    public function getIsActivated(){
        return $this->isactivated;
    }
    

}

?>

<?php
class User{

	public static $tableName = "users";
	private $seq,$adminseq,$mobile,$password,$email,$fullname,$issubmitter,$createdon,$isenabled;

	public function setSeq($seq_){
		$this->seq = $seq_;
	}
	public function getSeq(){
		return $this->seq;
	}

    public function setAdminSeq($adminseq_){
        $this->adminseq = $adminseq_;
    }
    public function getAdminSeq(){
        return $this->adminseq;
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

    public function setFullName($fullName_){
        $this->fullname = $fullName_;
    }
    public function getFullName(){
        return $this->fullname;
    }

	public function setIsSubmitter($bool_){
		$this->issubmitter = $bool_;
	}
	public function getIsSubmitter(){
		return $this->issubmitter;
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

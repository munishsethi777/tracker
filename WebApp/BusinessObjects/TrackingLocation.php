<?php
class TrackingLocation{

	public static $tableName = "trackinglocations";
	private $seq,$userseq,$longitude,$latitude,$dated;

	public function setSeq($seq_){
		$this->seq = $seq_;
	}
	public function getSeq(){
		return $this->seq;
	}

	public function setUserSeq($userseq_){
		$this->userseq = $userseq_;
	}
	public function getUserSeq(){
		return $this->userseq;
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


}

?>

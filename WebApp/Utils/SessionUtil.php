<?php

class SessionUtil{
    private static $LOGIN_MODE = "loginMode";
    private static $ADMIN_SEQ = "adminSeq";
    private static $ADMIN_NAME = "adminName";
    private static $ADMIN_EMAIL = "adminEmail";
    private static $ADMIN_LOGGED_IN = "adminLoggedIn";

    private static $USER_SEQ = "userSeq";
    private static $USER_USERNAME = "userUserName";
    private static $USER_LOGGED_IN = "userLoggedIn";


    private static $sessionUtil;
    public static function getInstance(){

        //ini_set('session.save_path', '/home/kalpanad/public_html/www.ezae.in/sessions');
	//ini_set('session.cookie_lifetime', 10);
	//ini_set('session.gc_maxlifetime', 10);
	session_start();
        //printf("gc: %s", ini_get('session.gc_maxlifetime'));

	if (!self::$sessionUtil){
		self::$sessionUtil = new SessionUtil();
		return self::$sessionUtil;
	}
	return self::$sessionUtil;
    }

    public function createAdminSession(Admin $admin){
        $arr = new ArrayObject();
        $arr[$ADMIN_SEQ] = $admin->getSeq();
        $arr[$ADMIN_EMAIL] = $admin->getEmailId();
        $_SESSION[self::$ADMIN_LOGGED_IN] = $arr;
        $_SESSION[self::$LOGIN_MODE] = 'admin';
    }
    public function createUserSession(User $user){
        $arr = new ArrayObject();
        $arr[$USER_SEQ] = $user->getSeq();
        $arr[$USER_USERNAME] = $user->getUserName();
        $_SESSION[self::$USER_LOGGED_IN] = $arr;
        $_SESSION[self::$LOGIN_MODE] = 'user';
    }

    public function isSessionAdmin(){
        if($_SESSION[self::$LOGIN_MODE] == "admin" &&
            $_SESSION[self::$ADMIN_LOGGED_IN] != null){
                return true;
        }
    }
    public function isSessionUser(){
        if($_SESSION[self::$LOGIN_MODE] == "user" &&
            $_SESSION[self::$USER_LOGGED_IN] != null){
                return true;
        }
    }
    public function getAdminLoggedInName(){
      if($_SESSION[self::$LOGIN_MODE] == "admin" &&
            $_SESSION[self::$ADMIN_LOGGED_IN] != null){
                $arr = $_SESSION[self::$ADMIN_LOGGED_IN];
                return $arr[1];
        }
    }

    public function getUserLoggedInName(){
      if($_SESSION[self::$LOGIN_MODE] == "user" &&
            $_SESSION[self::$USER_LOGGED_IN] != null){
                $arr = $_SESSION[self::$USER_LOGGED_IN];
                return $arr[1];
        }
        return null;
    }

    public function getUserLoggedInSeq(){
      if($_SESSION[self::$LOGIN_MODE] == "user" &&
            $_SESSION[self::$USER_LOGGED_IN] != null){
                $arr = $_SESSION[self::$USER_LOGGED_IN];
                return $arr[0];
        }
        return null;
    }

    public function destroySession(){
        $boolAdmin = self::isSessionAdmin();
        $boolUser = self::isSessionUser();
        $_SESSION = array();
        session_destroy();
        if($boolAdmin == true){

            header("Location:adminLogin.php");
        }

        if($boolUser == true){
            header("Location:index.php");

        }

    }
    public function sessionCheck($isUser){
    	$bool = self::isSessionAdmin();
        if($isUser){
            $bool = self::isSessionUser();
            if($bool == false){
                header("location: index.php");
            }
        }else{
            if($bool == false){
                header("location: adminLogin.php");
            }
        }

    }


  }
?>
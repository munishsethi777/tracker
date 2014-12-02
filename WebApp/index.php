<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="jqwidgets/styles/jqx.base.css" type="text/css" />
        <link rel="stylesheet" href="jqwidgets/styles/jqx.arctic.css" type="text/css" />
        <script type="text/javascript" src="scripts/jquery-1.10.2.min.js"></script>
        <script type="text/javascript" src="jqwidgets/jqxcore.js"></script>
        <script type="text/javascript" src="jqwidgets/jqxexpander.js"></script>
        <script type="text/javascript" src="jqwidgets/jqxvalidator.js"></script>
        <script type="text/javascript" src="jqwidgets/jqxbuttons.js"></script>
        <script type="text/javascript" src="jqwidgets/jqxcheckbox.js"></script>
        <script type="text/javascript" src="jqwidgets/globalization/globalize.js"></script>
        <script type="text/javascript" src="jqwidgets/jqxcalendar.js"></script>
        <script type="text/javascript" src="jqwidgets/jqxdatetimeinput.js"></script>
        <script type="text/javascript" src="jqwidgets/jqxmaskedinput.js"></script>
        <script type="text/javascript" src="jqwidgets/jqxinput.js"></script>
        <link type="text/css" href="styles/bootstrap.css" rel="stylesheet" />
        <script type="text/javascript">
            $(document).ready(function () {
                //$('.form-control').jqxInput({  });
                $('#loginForm').jqxValidator({
                    rules: [
                           { input: '#usernameInput', message: 'UserName is required!', action: 'keyup, blur', rule: 'required' },
                           { input: '#usernameInput', message: 'UserName must be less than 250 characters!', action: 'keyup, blur', rule: 'length=0,250' },

                           { input: '#passwordInput', message: 'Password is required!', action: 'keyup, blur', rule: 'required' },
                           { input: '#passwordInput', message: 'Password must be less than 250 characters!', action: 'keyup, blur', rule: 'length=0,250' },
                           ]
                });
            });
            function submitFormAction(){
                var validationResult = function (isValid) {
                    if (isValid) {
                        submitLogin();
                    }
                }
                $('#loginForm').jqxValidator('validate', validationResult);
                return false;
            }
            function submitLogin(){
                $(".errorMessage").removeClass('bg-danger');
                $(".errorMessage").addClass("bg-success");
                $(".errorMessage").html("<img src = 'images/loading.gif'> Trying to login...");
                $(".loadingDiv").show();
                $formData = $("#loginForm").serializeArray();
                $.get( "ajaxAdminMgr.php?call=loginAdmin", $formData,function( data ){
                    if(data == 0){
                        $(".errorMessage").addClass("bg-danger");
                        $(".errorMessage").html("Invaid username or Password");
                        $(".loadingDiv").hide();
                    }else{
                        window.location = "adminHome.php";
                    }
                });
            }
            </script>
    </head>
<body>

<div style="width:330px;margin:50px auto;padding:20px;border:1px silver solid">
    <center>
    <p class="errorMessage" style="line-height:34px;"></p>
    </center>
    <h2>Please Sign in.</h2>
    <form class="form-horizontal" id="loginForm" method="POST" name="loginForm" onsubmit="return submitFormAction()">
      <div class="form-group">
        <div class="col-sm-12">
          <input type="text" name="username" class="form-control" id="usernameInput" placeholder="Enter Email">
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-12">
              <input type="password" name="password" class="form-control" id="passwordInput"  placeholder="Enter Password">
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-12">
          <input type="submit" id = "loginButton" class="btn btn-default btn-primary" value = "Login" style="width:100%"/>
        </div>
      </div>
      <a href="contact.php" target="new">Contact Support</a>
    </form>
 </div>
</body>
</html>


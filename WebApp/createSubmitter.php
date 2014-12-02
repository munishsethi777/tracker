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
            $('#createSubmitterForm').jqxValidator({
                rules: [
                       { input: '#isdCodeInput', message: 'UserName is required!', action: 'keyup, blur', rule: 'required' },
                       { input: '#isdCodeInput', message: 'UserName must be less than 250 characters!', action: 'keyup, blur', rule: 'length=0,250' },

                       { input: '#mobileInput', message: 'Password is required!', action: 'keyup, blur', rule: 'required' },
                       { input: '#mobileInput', message: 'Password must be less than 250 characters!', action: 'keyup, blur', rule: 'length=0,250' },
                       ]
            });
        });
        function submitForm(){
            var validationResult = function (isValid) {
                if (isValid) {
                    submitFormAction();
                }
            }
            $('#createSubmitterForm').jqxValidator('validate', validationResult);
            return false;
        }
        function submitFormAction(){
            $(".errorMessage").removeClass('bg-danger');
            $(".errorMessage").addClass("bg-success");
            $(".errorMessage").html("<img src = 'images/loading.gif'> Trying to login...");
            $(".loadingDiv").show();
            $formData = $("#createSubmitterForm").serializeArray();
            $.get( "ajaxAdminMgr.php?call=createSubmitter", $formData,function( data ){
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

<div style="width:500px;margin:50px auto;padding:20px;border:1px silver solid">
    <center>
    <p class="errorMessage" style="line-height:34px;"></p>
    </center>
    <h2>Create New Submitter</h2>
    <form class="form-horizontal" id="createSubmitterForm" method="POST" name="createSubmitterForm" onsubmit="return submitForm()">
        <div class="form-group">
            <div class="col-sm-2">
              <input type="text" name="isdCode" class="form-control" id="isdCodeInput" placeholder="Code">
            </div>
            <div class="col-sm-10">
              <input type="text" name="mobile" class="form-control" id="mobileInput" placeholder="Mobile Number">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-12">
              <input type="text" name="email" class="form-control" id="emailInput" placeholder="Email Id">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-12">
              <input type="text" name="fullName" class="form-control" id="fullNameInput" placeholder="Full Name">
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


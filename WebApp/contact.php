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
   // $('.input-lg').jqxInput({  });
    $('#contactForm').jqxValidator({
        animationDuration: 2,
        rules: [
               { input: '#nameInput', message: 'Name is required!', action: 'keyup, blur', rule: 'required' },
               { input: '#emailIdInput', message: 'E-mail is required!', action: 'keyup, blur', rule: 'required' },
               { input: '#emailIdInput', message: 'Invalid e-mail!', action: 'keyup', rule: 'email' },
               { input: '#employeeIdInput', message: 'Employee Id is required!', action: 'keyup, blur', rule: 'required' },
               { input: '#workLocationInput', message: 'Work Location is required!', action: 'keyup, blur', rule: 'required' },
               { input: '#internetSpeedInput', message: 'Your Internet speed is required!', action: 'keyup, blur', rule: 'required' },
               { input: '#yourLocationInput', message: 'Your Location is required!', action: 'keyup, blur', rule: 'required' },
               { input: '#phoneNoInput', message: 'Phone Number is required!', action: 'keyup, blur', rule: 'required' },
               { input: '#problemDetailsInput', message: 'Problem Details is required!', action: 'keyup, blur', rule: 'required' },

        ]
    });
    $('#submitButton').jqxButton({ width: 120, height: 40 });
    $("#submitButton").click(function () {
        var validationResult = function (isValid) {
            if (isValid) {
                submitRegisteration();
            }
        }
        $('#contactForm').jqxValidator('validate', validationResult);
    });
    $("#contactForm").on('validationSuccess', function () {
        $("#contactForm-iframe").fadeIn('fast');
    });
});

function submitRegisteration(){

    $formData = $("#contactForm").serializeArray();
    $.get( "ajaxUsersMgr.php?call=submitContactForm", $formData,function( data ){
        $(".result").fadeIn();
        $(".result").html( data );
        $('#contactForm').fadeOut();
        $('#contactForm')[0].reset();
    });
}
</script>
</head>
<body>
<div style="width:600px;margin:auto;border:1px silver solid;padding:10px">
<p style="padding:10px;display:none" class="bg-success result"></p>
<form class="form-horizontal" id="contactForm" method="POST" name="contactForm">
<p style="padding-bottom:10px">If you are facing any problem with the system please provide the information below and we will get back to you</p>

  <div class="form-group">
    <label class="col-sm-4 control-label">Name</label>
    <div class="col-sm-6">
      <input type="text" name="name" class="form-control input-lg" id="nameInput">
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-4  control-label">Employee Id</label>
    <div class="col-sm-6">
      <input type="text" name="employeeId" class="form-control input-lg" id="employeeIdInput">
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-4 control-label">Work Location</label>
    <div class="col-sm-6">
      <input type="text" name="workLocation" class="form-control input-lg" id="workLocationInput">
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-4  control-label">Internet Speed</label>
    <div class="col-sm-6">
      <input type="text" name="internetSpeed" class="form-control input-lg" id="internetSpeedInput">
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-4 control-label">Your Location</label>
    <div class="col-sm-6">
      <input type="text" name="yourLocation" class="form-control input-lg" id="yourLocationInput">
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-4  control-label">Phone No.</label>
    <div class="col-sm-6">
      <input type="text" name="phoneNo" class="form-control input-lg" id="phoneNoInput">
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-4  control-label">Email Id</label>
    <div class="col-sm-6">
      <input type="text" name="emailId" class="form-control input-lg" id="emailIdInput">
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-4  control-label">Problem Details</label>
    <div class="col-sm-6">
      <input type="text" name="problemDetails" class="form-control input-lg" id="problemDetailsInput">
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-offset-4 col-sm-6">
      <input type="button" id = "submitButton" class="btn btn-default" value = "Submit" style="width:100px"/>
    </div>
  </div>
</form>
</div>
</body>
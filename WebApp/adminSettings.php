<?php
    require_once('IConstants.inc');
    require_once($ConstantsArray['dbServerUrl'] ."Managers/AdminMgr.php");
?>
<?include("adminMenu.php5");?>
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

<script type="text/javascript">
$(document).ready(function () {
    $('.text-input').jqxInput({  });
    $('#registerForm').jqxValidator({
        rules: [
               { input: '#nameInput', message: 'Name is required!', action: 'keyup, blur', rule: 'required' },
               { input: '#nameInput', message: 'Name must be less than 250 characters!', action: 'keyup, blur', rule: 'length=0,250' },

               { input: '#emailInput', message: 'E-mail is required!', action: 'keyup, blur', rule: 'required' },
               { input: '#emailInput', message: 'Invalid e-mail!', action: 'keyup', rule: 'email' },
               { input: '#emailInput', message: 'Email must be less than 250 characters!', action: 'keyup, blur', rule: 'length=0,250' },

               //{ input: '#organizationInput', message: 'Organization is required!', action: 'keyup, blur', rule: 'required' },
               //{ input: '#organizationInput', message: 'Organization must be less than 500 characters!', action: 'keyup, blur', rule: 'length=0,500' },
               //{ input: '#designationInput', message: 'Designation is required!', action: 'keyup, blur', rule: 'required' },
               //{ input: '#designationInput', message: 'Designation must be less than 500 characters!', action: 'keyup, blur', rule: 'length=0,500' },
        ]
    });
    $('#registerButton').jqxButton({ width: 100, height: 25 });
    $("#registerButton").click(function () {
        var validationResult = function (isValid) {
            if (isValid) {
                submitRegisteration();
            }
        }
        $('#registerForm').jqxValidator('validate', validationResult);
    });
    $("#registerForm").on('validationSuccess', function () {
        $("#registerForm-iframe").fadeIn('fast');
    });
});

function submitRegisteration(){
    $formData = $(".registerForm").serializeArray();
    $.get( "ajaxcalls.php?action=registerAdmin", $formData,function( data ){
        $(".result").html( data );
        $('#registerForm')[0].reset();
    });
}
</script>
     <style type="text/css">
        .text-input
        {
            height: 31px;
            width: 350px;
        }
        .register-table
        {

            border:1px silver solid;
            padding:20px;
            width:600px;
        }
        .register-table td,
        .register-table tr
        {
            margin: 0px;
            padding: 5px;
            border-spacing: 0px;
            border-collapse: collapse;
            font-family: Verdana;
            font-size: 12px;
        }
        h3
        {
            display: inline-block;
            margin: 0px;
        }
    </style>
</head>
<body class='default'>
        <form id="registerForm" class="registerForm" action="adminSettings.php" method ="post">
        <table class="register-table" align="center">
            <tr>
                <td colspan="2" align="center" style="padding:10px;background-color:#F5F5F5;color:Grey;margin:10px;">
                    Update your Settings
                    <div class="result" style="color:blue"></div>
                </td>
            </tr>
            <tr>
                <td>Name</td>
                <td><input type ="text" name="nameInput" id="nameInput" class="text-input"/></td>
            </tr>
            <tr>
                <td>UserName</td>
                <td><input type ="text" name="nameInput" id="nameInput" class="text-input"/></td>
            </tr>
            <tr>
                <td>Password</td>
                <td><input type ="password" name="passwordInput" id="passwordInput" class="text-input"/></td>
            </tr>
            <tr>
                <td>Description</td>
                <td><input type ="text" name="descriptionInput" id="descriptionInput" class="text-input"/></td>
            </tr>
            <tr>
                <td>Email</td>
                <td><input type ="text" name="emailInput" id="emailInput" class="text-input"/></td>
            </tr>
            <tr>
                <td>Mobile No.</td>
                <td><input type ="text" name="mobileInput" id="mobileInput" class="text-input"/></td>
            </tr>
            <tr>
                <td>Contact Person</td>
                <td><input type ="text" name="contactPersonInput" id="contactPersonInput" class="text-input"/></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="button" value="Register" id="registerButton" /></td>
            </tr>
        </table>
        </form>
</body>
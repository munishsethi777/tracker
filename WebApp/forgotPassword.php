<?php
If($_POST['submit']<>""){
    require("configuration.php5");
    $configuration = new  Configuration();
    $email = $configuration->getConfiguration($configuration->adminEmailId);
    $Password = $configuration->getConfiguration($configuration->adminPassword);
    $message = "Password for admin panel is ". $Password['configvalue'];
    $ok = mail($email['configvalue'], 'Retrieve Password at MyApp55.be', " . $message . ");
    if($ok){
        $msg="your password emailed to your email account";
    }else{
        $errorMsg="error during retrieve password";        
    }
}
?>

<!DOCTYPE html>
<html>
    <head>
        <link type="text/css" href="css/cupertino/jquery-ui-1.8.14.custom.css" rel="stylesheet" />
        <link type="text/css" href="css/custom.css" rel="stylesheet" />    
    </head>      
    <table align="center" width="40%" border="0">
      
      <tr>
        <td class="ui-widget-header" style="padding:10px 10px 10px 10px;"> Forgot Password </td>
        </tr>
      <tr>
        <td class="ui-widget-content">
            <form name="frm1" method="post" action="<?php echo $_SERVER['PHP_SELF']; ?>">        
                <table width="100%" border="0" style="padding:10px 10px 10px 10px;">
                  <td colspan="2" align="center" class="message"><?php echo($msg);?></td>
                  <td colspan="2" align="center" class="errormessage"> <?php echo($errorMsg);?></td> 
                  <tr>
                    <td>&nbsp;</td>
                    <td><input type="submit" name="submit" value=" Retreive " onclick="return submitform()" />
                         
                    
                    </td>
                  </tr>
                </table>
              </form> 
         </td>
        </tr>
        
    </table>

    
    
    
    
    </Div>

    </body>
</html>



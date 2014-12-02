<?
    require_once('IConstants.inc');
    require_once($ConstantsArray['dbServerUrl'] ."Utils/SessionUtil.php5");
    $session = SessionUtil::getInstance();
    $session->sessionCheck(false);
    $adminName = $session->getAdminLoggedInName();

?>
<link type="text/css" href="styles/bootstrap.css" rel="stylesheet" />
<script type="text/javascript" src="scripts/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="scripts/bootstrap.min.js"></script>
<link href="styles/font-awesome.min.css" rel="stylesheet">

<nav class="navbar navbar-default" role="navigation">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">USHA</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li class="active1"><a href="dashboard.php">Dashboard</a></li>
        <!--<li><a href="showUsers.php">Users</a></li>
        <li><a href="showModules.php">Modules</a></li>-->
        <li><a href="adminCompletionMetrics.php">Completion Metrics</a></li>
        <li><a href="adminPerformanceMetrics.php">Performance Metrics</a></li>
        <li><a href="adminComparativeMetrics.php">Comparative Metrics</a></li>
        <li><a href="analytics.php">Analytics</a></li>
      </ul>

      <ul class="nav navbar-nav navbar-right">
        <li id="myAccount">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                <i class="fa fa-1g fa-user"></i> <? echo $adminName;?>
                <i class="fa fa-1g fa-angle-down" style="margin-left:8px;"></i>
            </a>
              <ul class="dropdown-menu nav">
                <!--<li><a href="adminSettings.php"><i class="fa fa-pencil fa-cogs"></i> Settings</a></li>
                <li><a href="#"><i class="fa fa-trash-o fa-medkit"></i> Change Password</a></li>
                <li class="divider"></li>-->
                <li><a href="logout.php"><i class="fa fa-trash-o fa-sign-out"></i> Logout</a></li>
              </ul>
        </li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html lang="en" ng-app="salesdemo" class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html lang="en" ng-app="salesdemo" class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html lang="en" ng-app="salesdemo" class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en" ng-app="salesdemo" class="no-js"> <!--<![endif]-->
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>Sales Demo</title>
  <meta name="description" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
  <link rel="stylesheet" href="css/app.css">

</head>
<body>

  <!-- Main header definition -->
  <div class="masterheader"></div>
    <nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
      <!-- Brand and toggle get grouped for better mobile display -->
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-salesTax-navbar-collapse-1">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#/viewOrder">Home</a>
      </div>

      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse" id="bs-salesTax-navbar-collapse-1">
        <ul class="nav navbar-nav navbar-right">
          <li class="dropdown">
            <a href="" class="dropdown-toggle" data-toggle="dropdown">History<b class="caret"></b></a>
            <ul class="dropdown-menu">
              <li><a href="#/viewOrders" >Orders</a></li>
              <li><a href="#/viewOrder?new" >New Order</a></li>
              <li><a href="#/viewProducts" >Products</a></li>
            </ul>
          </li>
        </ul>
      </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
  </nav>

  <!-- View embedded -->
  <div ng-view></div>

  <!-- Footer Definition -->
  <div id="footer" class="container text-center">
    <nav class="navbar navbar-default navbar-fixed-bottom">
      <div class="navbar-inner navbar-content-center">
        <p class="text-muted credit">Sales Tax</p>
      </div>
    </nav>
  </div>


  <script src="https://code.jquery.com/jquery-2.1.4.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.20/angular.min.js"></script>

  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.20/angular-route.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.20/angular-resource.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
  <script src="js/salesdemo.js"></script>
</body>
</html>


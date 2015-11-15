'use strict';

// Declare app level module which depends on views, and components
var salesTaxApp = angular.module('salesdemo', [
  'ngRoute',
  'orderController',
  'productService',
  'orderService'

]);

salesTaxApp.config(['$routeProvider', function($routeProvider) {

    var salesDemoConfig = SalesDemo.SalesDemoConfig.getInstance();
    $routeProvider.when('/order', {
        templateUrl: salesDemoConfig.getUpdateOrderViewURL(),
        controller: 'UpdateOrderController'
    }).when( '/products',{
        templateUrl: salesDemoConfig.getProductsViewURL(),
        controller: 'ProductsController'
    }).when( '/orders',{
        templateUrl: salesDemoConfig.getOrdersViewURL(),
        controller: 'OrdersController'
    }).when('/order/:orderId', {
        templateUrl: salesDemoConfig.getUpdateOrderViewURL(),
        controller: 'UpdateOrderController'
    })
    .otherwise({
        redirectTo: '/orders'
    });
}]);








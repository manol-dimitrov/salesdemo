
//'use strict';
/**
 * The following is the orders controller.  These controllers will
 * be responsible for getting managing the orders page
 * It will be responsible for viewing products, orders
 * and updating and creating an order.  
 *
 * @author Shannon Lal
 */
var orderController = angular.module('orderController', []);


/**
 * The following controller is the update and creating a new order 
 *
 */
orderController.controller('UpdateOrderController', ['$scope','$log','$routeParams', 'ProductService','OrderService',
        function($scope, $log, $routeParams,ProductService, OrderService) {

            //Check to see if displaying an existing order or new order
            var orderId = $routeParams.orderId;

            if(( typeof orderId  === 'undefined') || ( orderId < 1) ){
                //Create new order
                $scope.order = {};
            }else{
                //Load existing order
                OrderService.getOrder(orderId).then(function( response ){
                   $scope.order = response;
                }, function( response ){
                    //Error getting products 
                    $log.log('Error getting order ->'+ orderId);
                    $scope.errorMessage = response;
                    
                });
            }

            //Set the selected order to empy
            $scope.selectedProduct = {};
            ProductService.getProducts().then( function(response ){
                    //Success
                    $scope.availableProducts = response;
                }, function( response ){
                    //Error getting products 
                    $log.log('Error getting products');
                    $scope.errorMessage = response;
                    
                });

            /**
             * The following method will add the selected product
             * to the order.  It will also send a request to the 
             * server to get the product
             * @param {type} selectedProduct
             * @returns {Order}
             */
            $scope.addProduct = function(selectedProduct){
                
                //Check if the selected product is undefined
                if(( typeof selectedProduct === 'undefined' ) 
                        || (typeof selectedProduct.id === 'undefined')){
                    return;
                }
                //Get the latest order from the scope
                var order = $scope.order;
                order = OrderService.addItemToOrder(order, selectedProduct );

                //Send the request to the server to update the order and totals
                OrderService.updateOrder( order ).then(function(response){
                    $scope.order = response;
                }, function( response ){
                    //Error getting products 
                    $log.log('Error adding product to order');
                    $scope.errorMessage = response;
                    
                }); 
            };
            
            /**
             * The following method will remove the selected 
             * item from the order
             * @param {type} itemId
             * @returns {Order}
             */
            $scope.removeItem = function( itemId ){
                //Check if the selected product is undefined
                if(( typeof itemId === 'undefined' ) 
                        || (typeof itemId === 0)){
                    return;
                }  
                
                var order = $scope.order;
                //var usedProducts = $scope.usedProducts;

                order = OrderService.removeItemFromOrder(order, itemId);

                //Send the request to the server to update the order and totals
                OrderService.updateOrder( order ).then(function(response){
                    $scope.order = response;
                }, function( response ){
                    //Error getting products 
                    $log.log('Error removing order from product');
                    $scope.errorMessage = response;
                    
                });                
            };
            
            /**
             * The following method will check if the error message is empty
             * @param {String} errorMessage
             * @returns {Boolean}
             */
            $scope.hasErrorResponse = function(errorMessage){
                if( ( typeof errorMessage !== 'undefined')&& (errorMessage !== '')){
                    return true;
                }else{
                    return false;
                }
            };

        }]
);


/**
 * The following controller is the get products controller
 * it will send a REST GET request to obtain a list
 *
 */
orderController.controller('ProductsController', ['$scope', '$log','ProductService',
        function($scope,  $log,ProductService) {

            //Get a list of products
            ProductService.getProducts().then( function(response ){
                    $scope.products = response;
                }, function( response ){
                    //Error getting products 
                    $log.log('Error getting products');
                    $scope.errorMessage = response;
                    
                });
            
             /**
             * The following method will check if the error message is empty
             * @param {String} errorMessage
             * @returns {Boolean}
             */
            $scope.hasErrorResponse = function(errorMessage){
                if( ( typeof errorMessage !== 'undefined')&& (errorMessage !== '')){
                    return true;
                }else{
                    return false;
                }
            };
        }]
);

/**
 * The following controller is the get orders controller
 * it will send a REST GET request to obtain a list
 *
 */
orderController.controller('OrdersController', ['$scope',  '$location', '$log','OrderService',
        function($scope,  $location, $log,OrderService) {

            OrderService.getOrders().then( function(response ){
                    $scope.orders = response;
                }, function( response ){
                    //Error getting products 
                    $log.log('Error getting products');
                    $scope.errorMessage = response;
                    
             });
    
            /**
             * The following method will redirect the user
             * to the new order page
             * @returns {undefined}
             */
            $scope.newOrder = function(){
                $location.path('/order');
            };
            
             /**
             * The following method will check if the error message is empty
             * @param {String} errorMessage
             * @returns {Boolean}
             */
            $scope.hasErrorResponse = function(errorMessage){
                if( ( typeof errorMessage !== 'undefined')&& (errorMessage !== '')){
                    return true;
                }else{
                    return false;
                }
            };

        }]
);
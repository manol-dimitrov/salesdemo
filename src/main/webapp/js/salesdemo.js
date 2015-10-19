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
        templateUrl: '/view/viewOrder',
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
        redirectTo: '/products'
    });
}]);









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
/**
 * The following file defines the order service which is
 * used for managing request for order and updating the
 * available products on the UI
 *
 * @author Shannon Lal on 2015-10-19.
 */

var orderService = angular.module('orderService', []);

/**
 * The following is the product service which will
 * manage the requests to interface to the REST product.
 *
 */

orderService.factory( 'OrderService',['$log','$http','$q',
    function( $log, $http, $q) {
        $log.log("Start of Order Service");
        var salesDemoConfig = SalesDemo.SalesDemoConfig.getInstance();
        return {

            /**
             * The following method will send a request to the server
             * to get a list of orders
             * @return Promise{List<Orders>}
             */
            getOrders : function(){
                var def = $q.defer();

                var url = salesDemoConfig.getOrdersAPIURL();
                $http.get(url).success(function (data) {
                    def.resolve( data );
                    return data;

                }).error(function (data, status) {
                    console.log('Error Getting Order '+ ' Status -> '+ status);

                    def.reject(data);
                    return data;
                });


                return def.promise;
            },

            /**
             * The following method will send a request to the server
             * to get a list of orders
             * @param orderId
             * @return{Promise(Order)}
             */
            getOrder : function(orderId){
                var def = $q.defer();
                //var url = 'SalesTax/rest/orders/'+orderId;

                var url = salesDemoConfig.getOrderAPIURL( orderId );
                $http.get(url).success(function (data) {
                    def.resolve( data );
                    return data;

                }).error(function (data, status) {
                    console.log('Error Getting Order '+ ' Status -> '+ status);

                    def.reject(data);
                    return data;
                });


                return def.promise;
            },
            /**
             * The following method will send a request to the server
             * to update the order.  It will send a REST request to the
             * server to perform a post and the response will be displayed
             *
             * @param order
             * @return Promise(Order)
             */
            updateOrder : function( order){
                var def = $q.defer();

                var url = salesDemoConfig.updateOrderAPIURL();
                $http.post(url, order).
                    success(function(data, status, headers, config) {
                        //Ensure that the status code is ok and data success
                        // response is TRUE
                        if (status === 200)  {
                            def.resolve(data);
                            return;
                        }else{
                            $log.log("There was an error updating the order.  Status ->"+ status);
                            def.reject( data );
                            return;
                        }
                    }).error(function(data, status, headers, config) {
                        console.log('ERROR updating order successfully.  Status ->' + status);
                        def.reject( data );
                        return;
                    });
                return def.promise;
            },

            /**
             * The following method will add the selected product to
             * the order.  If the order already has an item with the
             * product it will increment the item quantity
             * @param order
             * @param selectedProduct
             * @return order
             */
            addItemToOrder : function( order, selectedProduct ){
                //Create the item
                var item =  {};
                item['productId'] = selectedProduct.id;
                item['productName'] = selectedProduct.name;

                //Update the order in memory
                var orderItems = order['orderItems'];
                if( typeof orderItems === 'undefined'){
                    orderItems = new Array();
                    order['orderItems'] = orderItems;
                }

                var orderHasItem = false;
                //Check if order already has an item
                for( var i=0; i< orderItems.length;i++){
                    if( orderItems[i].productId === selectedProduct.id){
                        //Order already has an item.  Increment the quantity
                        orderItems[i].quantity = orderItems[i].quantity+1;
                        orderHasItem= true;
                        break;
                    }
                }

                if( !orderHasItem ){
                    item['quantity'] = 1;
                    orderItems[orderItems.length] = item;
                }
                
                return order;
            },

            /**
             * The following method will remove the item from the order
             * If there is more then 1 order it will reduce the quantity
             * @param order
             * @param itemId
             * @returns {*}
             */
            removeItemFromOrder : function (order, itemId){

                var orderItems = order.orderItems;
                var item= {};
                for( var i=0; i< orderItems.length;i++){
                    if( orderItems[i].id === itemId) {
                        //Remove from list
                        item = orderItems[i];
                        if (item.quantity === 1) {
                            orderItems.splice(i, 1);
                        }else{
                            item.quantity = item.quantity -1;
                        }
                        break;
                    }
                }

                return order;
           }
        };
    }
]);


/**
 * The following file defines the product service which is
 * used for managing request for products and updating the
 * available products on the UI
 *
 * @author Shannon Lal on 2015-10-19.
 */

var productService = angular.module('productService', []);

/**
 * The following is the product service which will
 * manage the requests to interface to the REST product.
 *
 */

productService.factory( 'ProductService',['$log','$http','$q',
    function( $log, $http, $q) {
        $log.log("Start of Protocol Service");

        var salesDemoConfig = SalesDemo.SalesDemoConfig.getInstance();
        return {

            /**
             * The following method will send a request to the server
             * to get a list of products
             */
            getProducts : function(){
                var def = $q.defer();

                var url = salesDemoConfig.getProductsAPIURL();
                $http.get(url).success(function (data) {
                    def.resolve( data );
                    return data;

                }).error(function (data, status) {
                    console.log('Error Getting Products '+ ' Status -> '+ status);

                    def.reject(data);
                    return data;
                });


                return def.promise;
            },

            /**
             * The following method will send a request to the server
             * to get a list of products.  It will then compare this
             * with the list of used products.  It will return an array
             * of products which are not in the list usedProducts
             * This will be used by drop down to see which ones
             * are available
             * @param usedProducts
             */
            getUnUsedProducts : function( usedProducts){
                var def = $q.defer();

                var url = salesDemoConfig.getProductsAPIURL();
                $http.get( url ).success(function (data) {
                    var products = data;
                    if( ( typeof usedProducts  === 'undefined') || (usedProducts.length ==0 )){

                        def.resolve( products );
                        return products;
                    }else{
                        var unUsedProducts = new Array();
                        for( var i=0; i< products.length;i++){

                            var product = products[i];
                            var used = false;
                            for( var j=0; j< usedProducts.length; j++){
                                if( usedProducts[j].id === product.id){
                                    used = true;
                                }
                            }

                            if( !used){
                                unUsedProducts[unUsedProducts.length] = product;
                            }
                        }

                        def.resolve( unUsedProducts );
                        return unUsedProducts;
                    }


                }).error(function (data, status) {
                    console.log('Error Getting Products '+ ' Status -> '+ status);

                    def.reject(data);
                    return data;
                });


                return def.promise;
            }
        }
    }
]);


/**
 * Created by shannonlal on 15-10-18.
 */
/**
 * Created by shannonlal on 15-10-18.
 */

var SalesDemo = SalesDemo || {REVISION: '1.0.0'};

/**
 * The following is the class definition for the
 * Sales Demo Config.  This will encapsulate
 * environment specific variables and methods
 *
 * @constructor SalesDemoConfig
 * @class SalesDemoConfig
 */

SalesDemo.SalesDemoConfig = (function() {

    function SalesDemoConfig(){
        this.getProductsAPIURL = function(){
            return 'api/product/list';
        };

        this.getProductsViewURL = function(){
            return 'viewproducts';
        };

        this.getOrdersAPIURL = function(){
            return 'api/order/list';
        };

        this.getOrderAPIURL = function(orderId){
            return 'api/order/'+orderId;
        };

        this.updateOrderAPIURL = function(){
            return 'api/order/update';
        };

        this.getOrdersViewURL = function(){
            return 'vieworders';
        };

        this.getUpdateOrderViewURL = function(){
            return 'vieworder';
        };

    };


    var instance;

    return {

        /**
         * The following method will return a reference to the
         * BootstrapCommonConfig instance
         * @returns {*}
         */
        getInstance : function(){
            if( ( instance == null ) || (typeof instance === 'undefined') ){
                instance = new SalesDemoConfig();

            }

            return instance;
        }
    };

})();
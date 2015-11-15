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


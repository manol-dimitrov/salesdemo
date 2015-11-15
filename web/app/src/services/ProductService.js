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


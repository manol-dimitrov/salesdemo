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
            return '/salesdemo-web/test/sample_data/list_products.json';
        };

        this.getProductsViewURL = function(){
            return 'views/products.html';
        };

        this.getOrdersAPIURL = function(){
            return '/salesdemo-web/test/sample_data/list_orders.json';
        };

        this.getUpdateOrderAPIURL = function(){
            return '/salesdemo-web/test/sample_data/list_orders.json';
        };

        this.getOrdersViewURL = function(){
            return 'views/orders.html';
        };

        this.getUpdateOrderViewURL = function(){
            return 'views/updateorder.html';
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
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
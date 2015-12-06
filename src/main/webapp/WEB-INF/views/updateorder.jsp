
<div class="container" id="order ViewContainer">
     <div class="row">

        <div class="col-xs-12">
            <table id="orderItemsTable" class="table table-striped">
                <thead>
                <tr>
                    <th class="col-xs-2">Quantity</th>
                    <th class="col-xs-2">Product</th>
                    <th class="col-xs-3">Price</th>
                    <th class="col-xs-3"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in order.orderItems">
                    <td class="col-xs-2">{{ item.quantity}}</td>
                    <td class="col-xs-3">{{item.productName}}</td>
                    <td class="col-xs-3">{{item.itemPrice}}</td>
                    <td class="col-xs-3 text-right">
                        <button type="button" class="btn btn-default btn-sm" ng-click="removeItem(item.id)">
                            <span class="glyphicon glyphicon-minus-sign" aria-hidden="true"></span>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
            
        </div>
    </div>
    <div class="row">
        <br />
        <div class="col-xs-12 text-right">
            <label for="salesTax" >Sales Tax&nbsp;&nbsp;</label><input type="text" id="salesTax" ng-model="order.salesTax" name="salesTax"  readonly/>
        </div>
    </div>   
    <div class="row">
        <br />
        <div class="col-xs-12 text-right">
            <label for="salesTotal" >Total&nbsp;&nbsp;</label><input type="text" id="salesTotal" ng-model="order.salesTotal" name="salesTotal"  readonly/>
        </div>
    </div>
    <div class="row">
        <br />
        <br />
        <div class="col-xs-12 text-center">
            <select id="productSelection" name="productSelection" ng-model="selectedProduct" ng-options=" product.name for product in availableProducts">
            </select>
        </div>
    </div>   
    <div class="row">
        <div class="col-xs-12 text-center">
            <br />
            <button class="btn btn-primary " id="addProductButton" ng-click="addProduct(selectedProduct)">Add</button>
        </div>
    </div>  
    <div class="row" ng-show="hasErrorResponse(errorMessage)">
        <div class="col-sm-2 col-md-3 col-lg-4"></div>   
        <div class="visible-xs col-xs-3"></div>
        <div class="col-xs-9 col-sm-8 col-md-offset-2 col-md-6 col-lg-4">
            <span >{{errorMessage}}</span>
        </div>
        <div class="col-sm-2 col-md-3 col-lg-4"></div>    
    </div>
</div>
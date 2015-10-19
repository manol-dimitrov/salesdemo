<div class="container" id="produtSection" style="display:table-cell; vertical-align:middle;">

    <div class="row">
        <div class="visible-xs col-xs-6"></div>
        <div class="col-xs-12 col-sm-12 col-sm-offset-1 col-md-12 col-md-offset-1 col-lg-12 col-lg-offset-1">
            <table id="productsTable" class="table table-striped col-md-4 center-block">
                <thead>
                <tr>
                    <th class="col-xs-1  col-sm-1 col-md-1 col-lg-1">Id</th>
                    <th class="col-xs-3  col-sm-3 col-md-3 col-lg-3">Name</th>
                    <th class="col-xs-4 col-sm-4 col-md-4 col-lg-4">Sales Tax Exempt</th>
                    <th class="col-xs-4 col-sm-4 col-md-4 col-lg-4">Import Tax Exempt</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="product in products">
                    <td class="col-xs-1  col-sm-1 col-md-1 col-lg-1">{{ product.id}}</td>
                    <td class="col-xs-3 col-sm-3 col-md-3 col-lg-3">{{product.name}}</td>
                    <td class="col-xs-4 col-sm-4 col-md-4 col-lg-4">{{product.salesTaxExempt}}</td> 
                    <td class="col-xs-4 col-sm-4 col-md-4 col-lg-4">{{product.importTaxExempt}}</td> 
                </tr>
                </tbody>
            </table>
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
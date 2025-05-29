
  function addProduct(productId,productName){
  $('#addProductModal').modal("hide");
      getShippingCost(productId);
  }

  function getShippingCost(productId){
    selectedCountry=$("#country option:selected");
    countryId=selectedCountry.val();

    state=$("#state").val();
    if(state.length==0){
    state=$("#city").val();
    }
    requestUrl=contextPath+"get_shipping_cost";

    params={productId:productId,countryId:countryId,state:state};

     $.ajax({
        type:'POST',
        url:requestUrl,
        beforeSend:function(xhr){
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        },
        data:params
    }).done(function(shippingCost){
      getProductInfo(productId,shippingCost);
    }).fail(function(err){
        $('#modalDialog').modal("show");
        getProductInfo(productId,0.0);
    });
  }

  function isProductAlreadyAdded(productId){
    productExists=false;

    $(".hiddenProductId").each(function(e){
      newProductId=$(this).val();

      if(newProductId==productId){
        productExists=true;
        return productExists;
      }
    });
    return productExists;
  }

//making ajax call to get products details
  function getProductInfo(productId,shippingCost) {
      requestURL = contextPath + "products/get/" + productId;
      $.get(requestURL, function(productJSON) {

          productName=productJSON.name;
          mainImagePath=contextPath.substring(0,contextPath.length-1)+productJSON.imagePath;
          productCost=$.number(productJSON.cost,2);
          productPrice=$.number(productJSON.price,2);

         htmlCode= generateProductCode(productId,productName,mainImagePath,productCost,productPrice,shippingCost);
         $("#productList").append(htmlCode);
         updateOrderAmounts();
      }).fail(function(err){
        alert("Opps!! there is some error");
      })
  }

  //generateProductCode
  function generateProductCode(productId,productName,mainImagePath,productCost,productPrice,shippingCost){

      nextCount=$(".hiddenProductId").length+1;
      quantityId="quantity"+nextCount
      shippingId="shippingCost"+nextCount
      priceId="price"+nextCount
      subTotalId="subTotal"+nextCount
      rowId="row"+nextCount

      htmlCode=`
        <div class="row  p-3" id="${rowId}">
          <div class="col-1">
            <input type="hidden" name="detailId" value="0">
            <div class="div-count">${nextCount}</div>
            <div><a class="fas fa-trash linkRemove" href="" rowNumber="${nextCount}"></a> </div>
          </div>
          <input type="hidden" name="productId" value="${productId}" class="hiddenProductId"/>
          <div class="col-3">
            <img src="${mainImagePath}" class="img-fluid">
          </div>
          <div class="col-8">
            <div class="text-wrap  align-items-center h-100 d-flex ">
              <b>${productName}</b>
            </div>
          </div>
          <div class="row">
            <div class="input-grids product-block m-4">
              <div class="row form-group">
                <label class="col-form-label col-sm-2">Product Cost : </label>
                <input type="text" value="${productCost}" name="productCosts" rowNumber="${nextCount}" class=" col-sm-10 cost-input"/>
              </div>
              <div class="row form-group">
                <label class="col-form-label col-sm-2">Quantity : </label>
                <input type="number" step="1" min="1" name="quantity" rowNumber="${nextCount}" id="${quantityId}" max="5" value="1"  class="quantity-input col-sm-10"/>
              </div>
              <div class="row form-group">
                <label class="col-form-label col-sm-2">Unit Price : </label>
                <input type="text" value="${productPrice}" name="productPrice" rowNumber="${nextCount}" id="${priceId}" class=" col-sm-10 price-input"/>
              </div>
              <div class="row form-group">
                <label class="col-form-label col-sm-2">Shipping Cost : </label>
                <input type="text" value="${shippingCost}" name="productShippingCost" id="${shippingId}" class=" col-sm-10 shippingCost-input"/>
              </div>
              <div class="row form-group">
                <label class="col-form-label col-sm-2">Subtotal : </label>
                <input type="text" value="${productPrice}"name="productSubTotal" readonly id="${subTotalId}" class=" col-sm-10 subTotal-output"/>
              </div>
            </div>
          </div>
        </div>
        <hr class="w-75 " style="margin:auto">
      `

      return htmlCode;
    }


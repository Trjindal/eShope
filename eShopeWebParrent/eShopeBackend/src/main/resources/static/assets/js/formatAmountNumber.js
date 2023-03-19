var fieldProductCost;
var fieldSubTotal;
var fieldTax;
var fieldShippingCost;
var fieldTotal;

$(document).ready(function(){
    fieldProductCost=$("#productCost");
    fieldSubTotal=$("#subTotal");
    fieldTax=$("#tax");
    fieldShippingCost=$("#shippingCost");
    fieldTotal=$("#total");

    formatOrderAmounts();
    formatProductAmounts();

    fieldTax.change(function(){
        taxValue=fieldTax.val().replace(",","");
        subTotalValue=fieldSubTotal.val().replace(",","");
        shippingCostValue=fieldShippingCost.val().replace(",","");
        total=parseFloat(taxValue)+parseFloat(subTotalValue);
        console.log(taxValue)
        console.log(subTotalValue)
        console.log(total)
        fieldTotal.val($.number(total,2))

    })

     fieldShippingCost.change(function(){
            taxValue=fieldTax.val().replace(",","");
            subTotalValue=fieldSubTotal.val().replace(",","");
            shippingCostValue=fieldShippingCost.val().replace(",","");
            total=parseFloat(taxValue)+parseFloat(subTotalValue);
            console.log(taxValue)
            console.log(subTotalValue)
            console.log(total)
            fieldTotal.val($.number(total,2))

        })

    $("#productList").on("change",".quantity-input",function(e){
        updateSubTotalWhenQuantityChanged($(this));
        updateOrderAmounts();
    })

    $("#productList").on("change",".price-input",function(e){
            updateSubTotalWhenPriceChanged($(this));
            updateOrderAmounts();
    })

     $("#productList").on("change",".shippingCost-input",function(e){
                updateOrderAmounts();
    })

    $("#productList").on("change",".cost-input",function(e){
                    updateOrderAmounts();
        })

});

function updateOrderAmounts(){
   totalCost=0.0;

   $(".cost-input").each(function(e){
      costInputField=$(this)
      rowNumber=costInputField.attr("rowNumber");
      quantityValue=$("#quantity"+rowNumber).val();

      productCost=getNumberValueRemovedThousandSeparator(costInputField);
      totalCost+=parseInt(quantityValue)*productCost;
//      alert(totalCost)
   })
    $("#productCost").val($.number(totalCost,2))

    orderSubTotal=0.0;

    $(".subTotal-output").each(function(e){
        productSubTotal=getNumberValueRemovedThousandSeparator($(this));
        orderSubTotal+=productSubTotal;
    });
     $("#subTotal").val($.number(orderSubTotal,2))

     shippingCost=0.0;

     $(".shippingCost-input").each(function(e){
             productShippingCost=getNumberValueRemovedThousandSeparator($(this));
             shippingCost+=productShippingCost;
     });
     $("#shippingCost").val($.number(shippingCost,2))

     tax=getNumberValueRemovedThousandSeparator(fieldTax);
     orderTotal=orderSubTotal+tax;
     $("#total").val($.number(orderTotal,2))
}

function getNumberValueRemovedThousandSeparator(fieldRef){
    fieldValue=fieldRef.val().replace(",","")
    return parseFloat(fieldValue);
}

function updateSubTotalWhenPriceChanged(input){

    priceValue=input.val().replace(",","");
    rowNumber=input.attr("rowNumber");
    quantityField=$("#quantity"+rowNumber);
    shippingCostField=$("#shippingCost"+rowNumber)

    quantityValue=parseFloat(quantityField.val());
    shippingCostValue=parseFloat(shippingCostField.val().replace(",",""));

    newSubTotal=(parseFloat(quantityValue)*priceValue)+shippingCostValue;

    subTotalField=$("#subTotal"+rowNumber);
    subTotalField.val($.number(newSubTotal,2))
}


function updateSubTotalWhenQuantityChanged(input){
    quantityValue=input.val();
    rowNumber=input.attr("rowNumber");
    priceField=$("#price"+rowNumber);
    shippingCostField=$("#shippingCost"+rowNumber)
    priceValue=parseFloat(priceField.val().replace(",",""));
    shippingCostValue=parseFloat(shippingCostField.val().replace(",",""));
    newSubTotal=(parseFloat(quantityValue)*priceValue)+shippingCostValue;

    subTotalField=$("#subTotal"+rowNumber);
    subTotalField.val($.number(newSubTotal,2))
}

function formatOrderAmounts(){
    formatNumberForField(fieldProductCost);
    formatNumberForField(fieldSubTotal);
    formatNumberForField(fieldTax);
    formatNumberForField(fieldShippingCost);
    formatNumberForField(fieldTotal);
}

function formatProductAmounts(){
    $(".cost-input").each(function(e){
        formatNumberForField($(this));
    })
    $(".price-input").each(function(e){
            formatNumberForField($(this));
    })
    $(".shippingCost-input").each(function(e){
            formatNumberForField($(this));
    })
    $(".subTotal-output").each(function(e){
                  formatNumberForField($(this));
    })


}

function formatNumberForField(fieldRef){
    fieldRef.val($.number(fieldRef.val(),2));
}

function processFormBeforeSubmit(){
    setCountryName()
    removeThousandSeparatorForField(fieldProductCost);
    removeThousandSeparatorForField(fieldShippingCost)
    removeThousandSeparatorForField(fieldTax)
    removeThousandSeparatorForField(fieldSubTotal)
    removeThousandSeparatorForField(fieldTotal)

    $(".cost-input").each(function(e){
            removeThousandSeparatorForField($(this))
    })
    $(".price-input").each(function(e){
                removeThousandSeparatorForField($(this))
    })
    $(".shippingCost-input").each(function(e){
                removeThousandSeparatorForField($(this))
    })
    $(".subTotal-output").each(function(e){
                removeThousandSeparatorForField($(this))
    })
}

function removeThousandSeparatorForField(fieldRef){
    fieldRef.val(fieldRef.val().replace(",",""))
}

function setCountryName(){
    selectedCountry=$("#country option:selected");
    console.log(selectedCountry.text())
    countryName=selectedCountry.text()
    $("#countryName").val(countryName);
}
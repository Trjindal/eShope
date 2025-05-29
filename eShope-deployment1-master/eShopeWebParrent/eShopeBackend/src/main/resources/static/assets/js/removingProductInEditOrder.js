$(document).ready(function(){
    $("#productList").on("click",".linkRemove",function(e){
        e.preventDefault();
        if(doesOrderHaveOnlyOneProduct()){
        console.log("here");
        warningModalDialog('Could not remove product. The order must have atleast 1 product.')
        }else{
                removeProduct($(this));
                updateOrderAmounts();
        }
    })
})

function removeProduct(link){
    rowNumber=link.attr("rowNumber");
    $("#row"+rowNumber).remove();

    $(".div-count").each(function(index,element){
        element.innerHTML=""+(index+1)
    })
}

function doesOrderHaveOnlyOneProduct(){
    productCount=$(".hiddenProductId").length;
    return productCount==1;
}
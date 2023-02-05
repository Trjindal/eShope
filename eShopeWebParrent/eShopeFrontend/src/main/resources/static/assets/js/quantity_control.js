$(document).ready(function(){


//    setTimeout(quantityControlButton, 1000);

    quantityControlButton()

    $(".linkMinus").on("click",function(e){
        e.preventDefault();
        quantityControlButton()
        productId=$(this).attr("pid");
        quantityInput=$("#quantity"+productId);
        newQuantity=parseInt(quantityInput.val())-1;
        quantityInput.val(newQuantity);
         quantityControlButton()
    })
    $(".linkPlus").on("click",function(e){
        e.preventDefault();

               productId=$(this).attr("pid");
               quantityInput=$("#quantity"+productId);
               newQuantity=parseInt(quantityInput.val())+1;
              quantityInput.val(newQuantity);
                quantityControlButton()
    });

    function quantityControlButton(){
        productId=$(".linkMinus").attr("pid");
        quantityInput=$("#quantity"+productId);
        quantityInput=parseInt(quantityInput.val())
        if(quantityInput==1){
             $('.linkMinus').css({pointerEvents: "none"})
             $('.linkMinus').css({color: "grey"})
        }else if(quantityInput==5){
            $('.linkPlus').css({pointerEvents: "none"})
            $('.linkPlus').css({color: "grey"})
        }else{
             $('.linkMinus').css({pointerEvents: "inherit"})
             $('.linkMinus').css({color: "inherit"})
             $('.linkPlus').css({pointerEvents: "inherit"})
             $('.linkPlus').css({color: "inherit"})
        }

    }

});
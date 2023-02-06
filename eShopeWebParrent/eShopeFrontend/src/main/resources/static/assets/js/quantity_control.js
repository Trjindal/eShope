$(document).ready(function(){


//    setTimeout(quantityControlButton, 1000);

    quantityControlButton($('.linkMinus'))


    $(".linkMinus").on("click",function(e){
        e.preventDefault();
        decreaseQuantity($(this))
         quantityControlButton($(this))
         if(shoppingCart){
        updateQuantity(productId,newQuantity)
        }
    })
    $(".linkPlus").on("click",function(e){
        e.preventDefault();
            increaseQuantity($(this))
            quantityControlButton($(this))
            if(shoppingCart){
              updateQuantity(productId,newQuantity)
           }
    });



});



     function quantityControlButton(link){
        link.each( function () {
           productId=$(this).attr('pid');
           quantityInput=$("#quantity"+productId);
           quantityInput=parseInt(quantityInput.val())
           if(quantityInput==1){

                if(shoppingCart){
                var html=`<i class="fa-solid fa-trash"></i>`
                $(this).empty();
                $(this).append(html)

                }else{
                    $('#decrease'+productId).css({pointerEvents: "none"})
                    $('#decrease'+productId).css({color: "grey"})
                }
           }else if(quantityInput==5){
              $('#increase'+productId).css({pointerEvents: "none"})
               $('#increase'+productId).css({color: "grey"})
           }else if(quantityInput==2||quantityInput==4){
                if(shoppingCart){
                    html=`   <svg focusable="false" width="10" height="2" class="icon icon--minus-big" viewBox="0 0 10 2">
                                          <path fill="currentColor" d="M0 0h10v2H0z"></path>
                                      </svg>`
                     $('#decrease'+productId).empty();
                      $('#decrease'+productId).append(html)
                }
               $('#decrease'+productId).css({pointerEvents: "inherit"})
                $('#decrease'+productId).css({color: "inherit"})
                 $('#increase'+productId).css({pointerEvents: "inherit"})
                 $('#increase'+productId).css({color: "inherit"})
           }
        });
        }


  function decreaseQuantity(link){
        productId=link.attr("pid");
        quantityInput=$("#quantity"+productId);
        newQuantity=parseInt(quantityInput.val())-1;
        quantityInput.val(newQuantity);

  }

  function increaseQuantity(link){

               productId=link.attr("pid");
               quantityInput=$("#quantity"+productId);
               newQuantity=parseInt(quantityInput.val())+1;
              quantityInput.val(newQuantity);

  }
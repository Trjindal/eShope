//$(document).ready(function(){

    quantityControlButton($('.linkMinus'))

//  CLICK HANDLER TO REMOVE ITEM
     $(document).on('click', ".linkRemove", function(e) {
         e.preventDefault();
         removeProduct($(this));
        url=$(this).attr("href");
     });

//  CLICK HANDLER TO DECREASE ITEM
    $(".linkMinus").on("click",function(e){
        e.preventDefault();
        decreaseQuantity($(this))
         quantityControlButton($(this))
//         IF SHOPPING CART IS TRUE THEN UPDATE QUANTITY
         if(shoppingCart){
        updateQuantity(productId,newQuantity)

        }
    })

//  CLICK HANDLER TO INCREASE ITEM
    $(".linkPlus").on("click",function(e){
        e.preventDefault();
            increaseQuantity($(this))
            quantityControlButton($(this))
//         IF SHOPPING CART IS TRUE THEN UPDATE QUANTITY
            if(shoppingCart){
              updateQuantity(productId,newQuantity)
           }
    });


//
//
//
//});

     function quantityControlButton(link){
        link.each( function (index) {
           productId=$(this).attr('pid');
           quantityInput=$("#quantity"+productId);
           quantityInput=parseInt(quantityInput.val())

            if(quantityInput==0){
                $(link[index]).prop("href", contextPath+"cart/delete/"+productId)
                $('#decrease'+productId).addClass("linkRemove")
                $('#decrease'+productId).removeClass("linkMinus")
            }
           else if(quantityInput==1){

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
//                      console.log($(link[index]))
                      $('#decrease'+productId).removeClass("linkRemove");
//  $('#decrease'+productId).toggleClass("linkRemove",false);
//    $('#decrease'+productId).toggleClass("linkMinus",true);
                       $('#decrease'+productId).addClass("linkMinus")
                      $('#decrease'+productId).prop("href", "")
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


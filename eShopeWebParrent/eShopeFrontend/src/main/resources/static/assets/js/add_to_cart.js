$(document).ready(function(){
    $("#buttonAddToCart").on('click',function(e){
        addToCart()
    })
})

function addToCart(){
     quantity=$("#quantity"+productId).val();
     url=contextPath+"cart/add/"+productId+"/"+quantity;

    $.ajax({
        type:"POST",
        url:url,
        beforeSend:function(xhr){
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        }
    }).done(function(response){
       if(response.startsWith("You must login ")||response.startsWith("The maximum allowed quantity")){
            $("#error").removeClass("d-none");
             $("#success").addClass("d-none");
           $("#error").text(response);
           setTimeout(function(){
            $("#error").addClass("d-none");
            $("#error").text("");
           }, 10000);
       }else{
            $("#error").addClass("d-none");
            $("#success").removeClass("d-none");
           $("#success").text(response);

           setTimeout(function(){
            $("#success").addClass("d-none");
            $("#success").text("");
           }, 10000);
       }


    }).fail(function(){
        console.log("error");

    })

 }

 function updateQuantity(productId,quantity){
    url=contextPath+"cart/update/"+productId+"/"+quantity;
    $.ajax({
        type:"POST",
        url:url,
        beforeSend:function(xhr){
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        }
    }).done(function(subTotal){
        updateOtherFeatures(subTotal,newQuantity,productId)
        updateTotal();
//        console.log(subTotal)
    }).fail(function(){
        console.log("error");
    })
 }

 function updateOtherFeatures(updatedSubTotal,newQuantity,productId){
    $('#subTotal'+productId).text(updatedSubTotal);
    $('#newQuantity'+productId).text(newQuantity);

 }

 function updateTotal(){
    total=0.0;
    totalItems=0
    $(".subTotal").each(function(index,element){
        console.log((element.innerHTML).replaceAll(",",""))
        total+=parseFloat(element.innerHTML.replaceAll(",",""))
    });
    $(".newQuantity").each(function(index,element){
     totalItems+=parseFloat(element.innerHTML)
     });
    $("#total").text(total);
    $("#totalItem").text(totalItems);

 }
var extraImagesCount=0;
defaultImageThumbnailSrc="http://localhost:8080/eShopeAdmin/images/image-thumbnail.png";

//for showing Main image preview
$(document).ready(function(){
    $("#fileImage").change(function(){
//        if(!checkFileSize(this)){
//            return;
//        }
        showImageThumbnail(this);
    });

//for showing Extra image preview
    $("input[name='extraImage']").each(function(index){
     extraImagesCount++;
        $(this).change(function(){
//        if(!checkFileSize(this)){
//                    return;
//                }
             showExtraImageThumbnail(this,index);
        });
    });

//FOR DELETING IMAGE IN EDIT MODE
    $("a[name='linkRemoveExtraImage']").each(function(index){
        $(this).click(function(){
            removeExtraImage(index);
        })
    })

    //FOR DELETING DETAIL IN EDIT MODE
     $("a[name='linkRemoveDetail']").each(function(index){
            $(this).click(function(){
                removeDetailsSectionByIndex(index);
            })
        })


    });

    function checkFileSize(fileInput){
        fileSize=fileInput.files[0].size;
        if(fileSize>1048576){
            fileInput.setCustomValidity("You must choose an image less than 1 MB!");
            fileInput.reportValidity();
            return false;
        }
        else{
            fileInput.setCustomValidity("");
            return true;
        }
    }ā

    function showExtraImageThumbnail(fileInput,index){
        var file=fileInput.files[0];
        var reader=new FileReader();
        reader.onload=function(e){
        $('#extraThumbnail'+index).attr("src",e.target.result);
        };
        reader.readAsDataURL(file);

        //Extra Image Section
        if(index>=extraImagesCount-1){
         addNextExtraImageSection(index+1);
        }

    }

    function addNextExtraImageSection(index){
        htmlExtraImage= `
        <div class="col-sm-4 p-2" id="divExtraImage${index}">
        <div class="myBorder">
        <div id="extraImageHeader${index}" class="p-2">
            <label class="my-col-form-label ">Extra Image ${index+1}: </label>
        </div>
            <img id="extraThumbnail${index}" alt="Extra Image ${index+1} Preview" class="img-fluid row mx-auto mb-2 thumbnail" src="${defaultImageThumbnailSrc}">
            <input type="file"  class="col-form-label col-sm-12 mb-2" name="extraImage" onchange="showExtraImageThumbnail(this,${index})" accept="image/png,image/jpeg">
        </div>
        </div>
        `;

        htmlLinkRemove=`<a class=" fas fa-thin fa-circle-xmark mybtn"
        href="javascript:removeExtraImage(${index-1})"
        title="Remove this image"></a>`;

        $("#divProductImages").append(htmlExtraImage);

        $("#extraImageHeader"+(index-1)).append(htmlLinkRemove);

        extraImagesCount++;
    }

    function removeExtraImage(index){
        console.log(index);
        $("#divExtraImage"+index).remove();
    }

    function showImageThumbnail(fileInput){
        var file=fileInput.files[0];
        var reader=new FileReader();
        reader.onload=function(e){
         $('#thumbnail').attr("src",e.target.result);
        };
        reader.readAsDataURL(file);
    }


//  FOR DETAILS
    function addNextDetailSection(){

       allDivDetails=$("[id^='divDetails']");
       divDetailsCount=allDivDetails.length;


        htmlDetailsSection=` <div class="row form-inline" id="divDetails${divDetailsCount}">
                                <label class="col-form-label col-sm-2 p-3">Name : </label>
                                <input type="text" placeholder="Name"  class="contact-input col-sm-3" name="detailsName" maxlength="255"/>
                                <div class="col-sm-1"></div>
                                <label class="col-form-label col-sm-2 p-3">Value : </label>
                                <input type="text" placeholder="Value"  class="contact-input col-sm-3" name="detailsValue" maxlength="255"/>
                              </div>`;

        //ADDING NEW INPUT
        $("#divProductDetails").append(htmlDetailsSection);


//        CODE FOR ADDING REMOVE BUTTON
        previousDivDetailsSection=allDivDetails.last();
        previousDivDetailsId=previousDivDetailsSection.attr("id");

        htmlLinkRemove=`<a class=" fas fa-thin fa-circle-xmark fa-2x mybtn col-sm-1 p-3"
        href="javascript:removeDetailsSectionById('${previousDivDetailsId}')"
        title="Remove this detail"></a>`;


        previousDivDetailsSection.append(htmlLinkRemove);

        //ADDING FOCUS TO LAST INPUT AFTER ADDING DIV
        $("input[name='detailsName']").last().focus();
    }

function removeDetailsSectionById(id){
    $("#"+id).remove();
}

function removeDetailsSectionByIndex(index){
    $("#divDetails"+index).remove();
}


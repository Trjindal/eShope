var extraImagesCount=0;
defaultImageThumbnailSrc="http://localhost:8080/eShopeAdmin/images/image-thumbnail.png";

$(document).ready(function(){
    $("#fileImage").change(function(){
        if(!checkFileSize(this)){
            return;
        }
        showImageThumbnail(this);
    });

    $("input[name='extraImage']").each(function(index){
     extraImagesCount++;
        $(this).change(function(){
             showExtraImageThumbnail(this,index);
        });
    });


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
    }

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



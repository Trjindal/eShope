var loadButton;
var countriesDropDown;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function(){
  loadButton=$("#buttonLoadCountries");
  countriesDropDown=$("#countriesDropDown");
  buttonAddCountry=$("#buttonAddCountry");
  buttonUpdateCountry=$("#buttonUpdateCountry");
  buttonDeleteCountry=$("#buttonDeleteCountry");
  labelCountryName=$("#labelCountryName");
  fieldCountryName=$("#fieldCountryName");
  fieldCountryCode=$("#fieldCountryCode");

  loadButton.click(function(){
    loadCountries();
  });

  countriesDropDown.on("change",function(){
    changeFormStateToSelectedCountry();
  });

  buttonAddCountry.click(function(){
    if(buttonAddCountry.val()=="Add"){
    console.log("chk");
        addCountry();
    }else{
        changeFormStateToNew();
    }
  });

  buttonUpdateCountry.click(function(){
    updateCountry();
  })

  buttonDeleteCountry.click(function(){
    deleteCountry();
  })

});

function changeFormStateToNew(){
    buttonAddCountry.text("Add");
    buttonAddCountry.val("Add");
    labelCountryName.text("Country Name :");

     buttonUpdateCountry.prop("disabled",true);
     buttonDeleteCountry.prop("disabled",true);

     fieldCountryName.val("").focus();
     fieldCountryCode.val("");
}

function updateCountry(){

    url=contextPath+"countries/save";
    countryId=countriesDropDown.val().split("-")[0];
    countryName=fieldCountryName.val();
    countryCode=fieldCountryCode.val();
    jsonData={id:countryId,name:countryName,code:countryCode};

    $.ajax({
        type:'POST',
        url:url,
        beforeSend:function(xhr){
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        },
        data:JSON.stringify(jsonData),
        contentType:'application/json'
    }).done(function(countryId){
     $("#countriesDropDown option:selected").val(countryId+"-"+countryCode);
        $("#countriesDropDown option:selected").text(countryName);
        showToastMessage("The Country has been Updated");
        changeFormStateToNew();

    }).fail(function(){
          showToastMessage("oops...: Could not connect ot server");
    });}

function addCountry(){

    url=contextPath+"countries/save";
    countryName=fieldCountryName.val();
    countryCode=fieldCountryCode.val();
    jsonData={name:countryName,code:countryCode};

    $.ajax({
        type:'POST',
        url:url,
        beforeSend:function(xhr){
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        },
        data:JSON.stringify(jsonData),
        contentType:'application/json'
    }).done(function(countryId){
        selectNewlyAddedCountry(countryId,countryCode,countryName);
        showToastMessage("The New Country has been Added");
    }).fail(function(){
          showToastMessage("oops...: Could not connect ot server");
    });
}

function deleteCountry() {
	optionValue = countriesDropDown.val();
	countryId = optionValue.split("-")[0];

	 url=contextPath+"countries/delete/"+countryId;

	$.ajax({
		type: 'GET',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).fail(function() {
	        console.log("Fail "+url);
      	  showToastMessage("Oops...: Could not connect to server");
      	}).done(function() {
      	    console.log(url);
		 $("#countriesDropDown option[value='"+optionValue+"']").remove();
		 changeFormStateToNew();
		  showToastMessage("The country has been deleted successfully.");
	});

}



function selectNewlyAddedCountry(countryId,countryCode,countryName){
    optionValue=countryId+"-"+countryCode;
    $("<option>").val(optionValue).text(countryName).appendTo(countriesDropDown);

    $("#countriesDropDown option[value='"+optionValue+"']").prop("selected",true);

    fieldCountryName.val("").focus();
    fieldCountryCode.val("");
}

function changeFormStateToSelectedCountry(){
    buttonAddCountry.text("New");
    buttonAddCountry.val("New");
    buttonUpdateCountry.prop("disabled",false);
    buttonDeleteCountry.prop("disabled",false);

    labelCountryName.text("Selected Country :")
    selectedCountryName=$("#countriesDropDown option:selected").text();
    fieldCountryName.val(selectedCountryName);

    countryCode=countriesDropDown.val().split("-")[1];
    fieldCountryCode.val(countryCode);
}

function loadCountries(){
  url=contextPath+"countries/list";

  $.get(url,function(responseJSON){
    countriesDropDown.empty();

    $.each(responseJSON,function(index,country){
      optionValue=country.id+"-"+country.code;
      $("<option>").val(optionValue).text(country.name).appendTo(countriesDropDown);
    });
  }).done(function(){
    loadButton.val("Refresh Country List");
    showToastMessage("All countries have been loaded.");
  }).fail(function(){
    showToastMessage("oops...: Could not connect to server");
  })
}

function showToastMessage(message){
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}
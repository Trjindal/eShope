var loadStatesButton;
var dropDownCountriesForStates;
var statesDropDown;
var addStatesButton;
var updateStatesButton;
var deleteStatesButton;
var labelStateName;
var fieldStateName;
//const myInterval = setInterval(loadStatesForCountry, 1000);
$("#formState").submit(function(e){
    e.preventDefault();
});

$(document).ready(function(){
    loadStatesButton=$('#buttonLoadCountriesForStates');
    dropDownCountriesForStates=$("#dropDownCountriesForStates");
    statesDropDown=$("#statesDropDown");
    addStatesButton=$("#buttonAddState");
    updateStatesButton=$("#buttonUpdateState");
    deleteStatesButton=$("#buttonDeleteState");
    labelStateName=$("#labelStateName");
    fieldStateName=$("#fieldStateName");

    fieldStateName.prop("disabled", true);

    loadStatesButton.click(function(){
        loadCountriesForStates();

    })

    dropDownCountriesForStates.on("change",function(){
        loadStatesForCountry();

    });

    statesDropDown.on("change",function(){
        changeFormStateToSelectedState();
    });

    addStatesButton.click(function(){

        if(addStatesButton.val()=="Add"){
            if(checkUniqueState()){
                addState();
            }
        }else{
            changeFormStateToNewState();
        }
    });

    updateStatesButton.click(function(){
        updateState();
    });

    deleteStatesButton.click(function(){
        deleteState();
    })

});

function loadStatesForCountry(){
    selectedCountry=$("#dropDownCountriesForStates option:selected");
    countryId=selectedCountry.val();
    url=contextPath+"states/listByCountry/"+countryId;

    $.get(url,function(responseJSON){
        statesDropDown.empty();

        $.each(responseJSON,function(index,state){
            $("<option>").val(state.id).text(state.name).appendTo(statesDropDown);
        });
    }).done(function(){
        changeFormStateToNewState();
        showToastMessage("All states have been loaded for country "+selectedCountry.text());
    }).fail(function(){
        showToastMessage(" Error: Could not connect to server or server encountered an error");
    });
}

function loadCountriesForStates(){
    url=contextPath+"countries/list";
    $.get(url,function(responseJSON){
        dropDownCountriesForStates.empty();

        $.each(responseJSON,function(index,country){
            $("<option>").val(country.id).text(country.name).appendTo(dropDownCountriesForStates);
            $("#dropDownCountriesForStates option[value='" + country.id + "']").prop("selected", true);
        });
    }).done(function(){
        addStatesButton.prop("disabled", false);
        loadStatesForCountry();
        loadStatesButton.val("Refresh Country List");
        showToastMessage("All countries have been loaded");
    }).fail(function(){
                showToastMessage(" Error: Could not connect to server or server encountered an error");
    })
}

function addState(){
     if (!validateFormState()) return;
    url=contextPath+"states/save";
    console.log(url);
    stateName=fieldStateName.val();

    selectedCountry=$("#dropDownCountriesForStates option:selected");
    countryId=selectedCountry.val();
    countryName=selectedCountry.text();

    jsonData={name:stateName,country:{id:countryId,name:countryName}};

    $.ajax({
        type:'POST',
        url:url,
        beforeSend:function(xhr){
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        },
        data:JSON.stringify(jsonData),
        contentType:'application/json'
    }).done(function(stateId){
        selectNewlyAddedState(stateId,stateName);
        changeFormStateToSelectedState();
        showToastMessage("The new state has been added");
    }).fail(function(){
       showToastMessage(" Error: Could not connect to server or server encountered an error");
    })
}

function updateState(){
     if (!validateFormState()) return;
    url=contextPath+"states/save";
    console.log(url);
    stateId=statesDropDown.val();
    stateName=fieldStateName.val();


    selectedCountry=$("#dropDownCountriesForStates option:selected");
    countryId=selectedCountry.val();
    countryName=selectedCountry.text();

    jsonData={id: stateId,name:stateName,country:{id:countryId,name:countryName}};

    $.ajax({
            type:'POST',
            url:url,
            beforeSend:function(xhr){
                xhr.setRequestHeader(csrfHeaderName,csrfValue);
            },
            data:JSON.stringify(jsonData),
            contentType:'application/json'
        }).done(function(stateId){
//            selectNewlyAddedState(stateId,stateName);
            $("#statesDropDown option:selected").text(stateName);
            showToastMessage("The  state has been updated successfully");
            changeFormStateToNewState();
        }).fail(function(){
            showToastMessage(" Error: Could not connect to server or server encountered an error");
        })
}

function deleteState(){
    stateId=statesDropDown.val();
    url=contextPath+"states/delete/"+stateId;

    $.ajax({
    		type: 'DELETE',
    		url: url,
    		beforeSend: function(xhr) {
    			xhr.setRequestHeader(csrfHeaderName, csrfValue);
    		}
    	}).done(function(){
        $("#statesDropDown option[value='"+stateId+"']").remove();
        changeFormStateToNewState();
        showToastMessage("The state has been deleted successfully");
    }).fail(function(){
        showToastMessage("Oops...: Could not connect to server");

    })
}

function selectNewlyAddedState(stateId, stateName) {
	$("<option>").val(stateId).text(stateName).appendTo(statesDropDown);

	$("#statesDropDown option[value='" + stateId + "']").prop("selected", true);

	fieldStateName.val("").focus();
}

function changeFormStateToNewState() {
	addStatesButton.val("Add");
	labelStateName.text("State/Province Name:");

	updateStatesButton.prop("disabled", true);
	deleteStatesButton.prop("disabled", true);

	fieldStateName.prop("disabled", false);

	fieldStateName.val("").focus();
}

function changeFormStateToSelectedState() {
	addStatesButton.text("New");
	addStatesButton.val("New");
	updateStatesButton.prop("disabled", false);
	deleteStatesButton.prop("disabled", false);

	fieldStateName.prop("disabled", false);

	labelStateName.text("Selected State/Province:");

	selectedStateName = $("#statesDropDown option:selected").text();
	fieldStateName.val(selectedStateName);

}

function showToastMessage(message){
    $("#toastMessage1").text(message);
    $(".toast").toast('show');
}

function validateFormState() {
	formState = document.getElementById("formState");
	if (!formState.checkValidity()) {
		formState.reportValidity();
		return false;
	}

	return true;
}

function checkUniqueState() {

	console.log("checkUniqueState is working");

	stateName = $("#fieldStateName").val();

	console.log(stateName);

	csrfValue = $("input[name='_csrf']").val();

	jsonData = {name: stateName, _csrf: csrfValue};

	checkUniqueStateUrl = contextPath + "states/check_unique";

	$.ajax({
		type: 'POST',
		url: checkUniqueStateUrl,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(response) {
		if (response == "OK") {
		    addState();
			return true;
		} else if (response == "Duplicate") {
			showToastMessage("State already exist :  " + stateName);
			return false;
		} else {
			showToastMessage("Unknown response from server");
			return false;
		}
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
		return false;
	});

	return false;
}


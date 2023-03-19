var trackRecordCount;

//window.onload=setTimeout((){
var myDate=$(".myDate");
myDate.each(function( i ) {
   var date=$( this ).val();
   console.log(date)
   array=date.split("T")
   console.log(array)
    console.log(array[0]+'\'T\''+array[1])
   $(this).val(array[0]+'T'+array[1]);
});

function datetimeLocal(datetime) {
    const dt = new Date(datetime);
    dt.setMinutes(dt.getMinutes() - dt.getTimezoneOffset());
    return dt.toISOString().slice(0, 16);
}

//}, 3000)


//console.log(myDate);
console.log(formatCurrentDateTime());
$(document).ready(function() {
	
	trackRecordCount = $(".hiddenTrackId").length;
	
	$("#trackList").on("click", ".linkRemoveTrack", function(e) {
		e.preventDefault();
		deleteTrack($(this));
		updateTrackCountNumbers();
	});

	$("#track").on("click", "#linkAddTrack", function(e) {
		e.preventDefault();
		addNewTrackRecord();
		updateTrackCountNumbers();
	});

	$("#trackList").on("change", ".dropDownStatus", function(e) {
		dropDownList = $(this);
		rowNumber = dropDownList.attr("rowNumber");
		selectedOption = $("option:selected", dropDownList);

		defaultNote = selectedOption.attr("defaultDescription");
		$("#trackNote" + rowNumber).text(defaultNote);
	});	
});

function deleteTrack(link) {
	rowNumber = link.attr('rowNumber');
	$("#rowTrack" + rowNumber).remove();
	$("#emptyLine" + rowNumber).remove();	
}

function updateTrackCountNumbers() {
	$(".divCountTrack").each(function (index, element) {
		element.innerHTML = "" + (index + 1);
	});
}

function addNewTrackRecord() {	
	htmlCode = generateTrackCode();	
	$("#trackList").append(htmlCode);
}

function generateTrackCode() {
	nextCount = trackRecordCount + 1;
	trackRecordCount++;
	rowId = "rowTrack" + nextCount;
	emptyLineId = "emptyLine" + nextCount;
	trackNoteId = "trackNote" + nextCount;
	currentDateTime = formatCurrentDateTime();

	htmlCode = `
			<div class="row " id="${rowId}">
                                  <input type="hidden" name="trackId" value="0" class="hiddenTrackId">
                                  <div class="col-md-1 col-sm-12">
                                    <div class="row form-group">
                                      <div class="divCountTrack">${nextCount}</div>
                                      <div><a class="fas fa-trash linkRemoveTrack" href="" rowNumber="${nextCount}"></a> </div>
                                    </div>
                                  </div>
                                  <div class="col-md-3 col-sm-12">
                                    <div class="row form-group">
                                      <label class="col-form-label col-sm-12">Time : </label>
                                      <input type="datetime-local" name="trackDate" value="${currentDateTime}"  required class=" col-sm-12 cost-input"/>
                                    </div>
                                  </div>
                                  <div class="col-md-3 col-sm-12">
                                    <div class="row form-group">
                                      <label class="col-form-label col-sm-12">Status : </label>
                                      <select name="trackStatus" class="my-form-control dropDownStatus col-sm-12" required rowNumber="${nextCount}">

			`;



	htmlCode += $("#trackStatusOptions").clone().html();

	htmlCode += `
				       </select>
                                              </div>
                                            </div>
                                            <div class="col-md-5 col-sm-12">
                                              <div class="row form-group">
                                                <label class="col-form-label col-sm-12">Notes : </label>
                                                <textarea rows="1" cols="10" class="form-control col-12" name="trackNotes" id="${trackNoteId}"  required></textarea>
                                              </div>
                                            </div>
                                          </div>
	`;




	return htmlCode;
}

function formatCurrentDateTime() {
	date = new Date();
	year = date.getFullYear();
	month = date.getMonth() + 1;
	day = date.getDate();
	hour = date.getHours();
	minute = date.getMinutes();
	second = date.getSeconds();

	if (month < 10) month = "0" + month;
	if (day < 10) day = "0" + day;

	if (hour < 10) hour = "0" + hour;
	if (minute < 10) minute = "0" + minute;
	if (second < 10) second = "0" + second;

	return year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second;

} 
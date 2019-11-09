$(function () {
    loadEvents();
    showTodaysDate();
    initialiseTimePicker();
    initializeCalendar();
    getCalendars();
    initializeRightCalendar();
    disableEnter();
});

/* --------------------------initialize timepicker-------------------------- */
var initialiseTimePicker = function () {
    $('#duration').datetimepicker({
        format: 'HH:mm',
        stepping: 5
    });
}
/* --------------------------initialize calendar-------------------------- */
var initializeCalendar = function () {
    $('.calendar').fullCalendar({
        editable: true,
        eventLimit: true, // allow "more" link when too many events
        eventOverlap: false, //cannot overlap events
        eventDurationEditable: false, //cannot resize objects to change the duration. duration can only be changed through edit event modal
        //append activity type to title of event
        //set duration for calendar view
        eventRender: function (event, element) {
            if (event.activityType == "Food") {
                element.css('background-color', '#ffbaba');
            } 
            if (event.activityType == "Attraction") {
                element.css('background-color', '#ffff9f');
            } 
            if (event.activityType == "Events") {
                element.css('background-color', '#a5ffa5');
            } 
            if (event.activityType == "Accomodation") {
                element.css('background-color', '#ffdaa1');
            }
            if (event.imageUrl) {
                //element.find(".fc-title").prepend(event.activityType);
                //element.find('.fc-event-title').html('<p>Ingen migræne idag</p><img src="icons/kalender_glad_smiley.png"/>');
                element.find(".fc-title").prepend("<img src='" + event.imageUrl +"' width='16' height='16'>");
            }
        },
        height: screen.height - 160,
    });
}

/*--------------------------calendar variables--------------------------*/
var getCalendars = function () {
    $cal = $('.calendar');
    $cal2 = $('#calendar2');
}

/* -------------------manage cal2 (right pane)------------------- */
var initializeRightCalendar = function () {
    $cal2.fullCalendar('changeView', 'agendaDay');
    $cal2.fullCalendar('option', {
        slotEventOverlap: false,
        allDaySlot: false,
        header: {
            right: 'prev,next today'
        },
        selectable: true,
        selectHelper: true,
        select: function (start, end) {
            newEvent(start);
        },
        //display tooltip

    });
}


/* -------------------moves right pane to date------------------- */
//when ever 
var loadEvents = function () {
    $.getScript("js/events.js", function () {
    });
}


var newEvent = function (start) {
    //clear values from previous event
    $('input#title').val(""); 
    $("#duration").find("input").val(""); 
    $('textarea#notes').val("");
    $("#activityType :selected").text("");
    
    $('#newEvent').modal('show');
    $('#submit').unbind();
    $('#submit').on('click', function () {
        var title = $('input#title').val();
        var activityType = $("#activityType :selected").text();
        var duration = $("#duration").find("input").val();
        var notes = $('textarea#notes').val();
        var imageUrl = $('input#image').val();
        console.log(imageUrl);
        //split the duration into hours and minutes
        var hours = duration.split(":")[0];
        var mins = duration.split(":")[1];
        //convert hours to mins
        if (hours != "0") {
            var hoursInMins = parseInt(hours) * 60;
            var durationInMinutes = hoursInMins + parseInt(mins);
            var end = moment(start).add(durationInMinutes, 'minutes');
        } else {
            var durationInMinutes = hoursInMins + parseInt(mins);
            var end = moment(start).add(durationInMinutes, 'minutes');
        }
        //check
        if (!title && duration) {
            alert("Title of activity is required.")
        } 
        if (title && !duration) {
            alert("Duration for activity is required.")
        }
        if (!title && !duration) {
            alert("Title and duration required.")
        }
        //user did not type any notes
        if (title && duration && !notes && !imageUrl) {
            var eventData = {
                title: title,
                start: start,
                end: end,
                activityType: activityType
            };
            $cal.fullCalendar('renderEvent', eventData, true);
            $('#newEvent').modal('hide');
        }
        //user did not upload image
        if (title && duration && notes && !imageUrl) {
            var eventData = {
                title: title,
                start: start,
                end: end,
                activityType: activityType,
                notes: notes
            };
            $cal.fullCalendar('renderEvent', eventData, true);
            $('#newEvent').modal('hide');
        }
        if (title && duration && notes && imageUrl) {
            var eventData = {
                title: title,
                start: start,
                end: end,
                activityType: activityType,
                notes: notes,
                imageUrl: imageUrl,
            };
            $cal.fullCalendar('renderEvent', eventData, true);
            $('#newEvent').modal('hide');
        }
    });
}

var editEvent = function (calEvent) {
    $('input#editTitle').val(calEvent.title);
    $('#editEvent').modal('show');
    $('#update').unbind();
    $('#update').on('click', function () {
        var title = $('input#editTitle').val();
        $('#editEvent').modal('hide');
        var eventData;
        if (title) {
            calEvent.title = title
            $cal.fullCalendar('updateEvent', calEvent);
        } else {
            alert("Title can't be blank. Please try again.")
        }
    });
    $('#delete').on('click', function () {
        $('#delete').unbind();
        if (calEvent._id.includes("_fc")) {
            $cal1.fullCalendar('removeEvents', [getCal1Id(calEvent._id)]);
            $cal2.fullCalendar('removeEvents', [calEvent._id]);
        } else {
            $cal.fullCalendar('removeEvents', [calEvent._id]);
        }
        $('#editEvent').modal('hide');
    });
}

/* --------------------------load date in navbar-------------------------- */
var showTodaysDate = function () {
    n = new Date();
    y = n.getFullYear();
    m = n.getMonth() + 1;
    d = n.getDate();
    $("#todaysDate").html("Today is " + m + "/" + d + "/" + y);
};
/* full calendar gives newly created given different ids in month/week view
 and day view. create/edit event in day (right) view, so correct for
 id change to update in month/week (left)
 */
var getCal1Id = function (cal2Id) {
    var num = cal2Id.replace('_fc', '') - 1;
    var id = "_fc" + num;
    return id;
}

var disableEnter = function () {
    $('body').bind("keypress", function (e) {
        if (e.keyCode == 13) {
            e.preventDefault();
            return false;
        }
    });
}

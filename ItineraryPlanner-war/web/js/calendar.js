$(function () {
    loadEvents();
    showTodaysDate();
    initialiseTimePicker();
    initializeCalendar();
    getCalendars();
    initializeRightCalendar();
    //initializeEditCalendar();
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
        },
        height: screen.height - 160,
    });
}

/*--------------------------calendar variables--------------------------*/
var getCalendars = function () {
    $cal = $('.calendar');
    $cal2 = $('#calendar2');
    $cal3 = $('#calendar3');
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
        eventClick: function (calEvent, jsEvent, view) {
            editEvent(calEvent);
        },
    });
}
/* -------------------edit calendar------------------- */

/* -------------------moves right pane to date------------------- */
var loadEvents = function () {
    $.getScript("js/events.js", function () {
    });
}


var newEvent = function (start) {
//clear values from previous event
    $('input#title').val("");
    $('textarea#notes').val("");
    $("#activityType").val('default');
    $('#newEvent').modal('show');
    $('#submit').unbind();
    $('#submit').on('click', function () {
        var title = $('input#title').val();
        var activityType = $("#activityType :selected").text();
        console.log(activityType);
        //var duration = $("#duration").find("input").val();
        var notes = $('textarea#notes').val();
//check
//user did not type any notes
        if (title && !notes) {
            var eventData = {
                title: title,
                start: start,
                activityType: activityType
            };
            $cal.fullCalendar('renderEvent', eventData, true);
            $('#newEvent').modal('hide');
        }
//user did not upload image
        if (title && notes) {
            var eventData = {
                title: title,
                start: start,
                activityType: activityType,
                notes: notes
            };
            $cal.fullCalendar('renderEvent', eventData, true);
            $('#newEvent').modal('hide');
        }
    });
}

var editEvent = function (calEvent) {
    $('input#editTitle').val(calEvent.title);
    $("#editActivityType :selected").text(calEvent.activityType);
    console.log(calEvent.title);
    console.log(calEvent.activityType);
    if (calEvent.notes) {
        $('textarea#editNotes').val(calEvent.notes);
    }
    $('#editEvent').modal('show');
    $('#update').unbind();
    $('#update').on('click', function () {
        var title = $('input#editTitle').val();
        var notes = $('textarea#editNotes').val();
        console.log(title);
        var activityType = $("#editActivityType :selected").text();
        console.log(activityType);
        $('#editEvent').modal('hide');

        //user did not type any notes
        if (title && !notes) {
            calEvent.title = title;
            calEvent.activityType = activityType;
            //calEvent.start = calEvent.start;
            //calEvent.end = end;
            //calEvent.activityType = activityType;
            $cal.fullCalendar('updateEvent', calEvent);
        }
//user did not upload image
        if (title && notes) {
            calEvent.title = title;
            calEvent.activityType = activityType;
            calEvent.notes = notes;
            $cal.fullCalendar('updateEvent', calEvent);
        }
    });
    $('#delete').on('click', function () {
        $('#delete').unbind();
        if (calEvent._id.includes("_fc")) {
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

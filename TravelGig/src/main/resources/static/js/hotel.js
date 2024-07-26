$(document).ready(function () {
	// Binding event listeners to buttons
    $('#submitQuestionButton').on('click', function() {
        submitQuestion();
    });

    $('#answerQuestionButton').on('click', function() {
        var questionId = $(this).data('question-id'); 
        var adminAnswer = $('#adminAnswer').val(); 
        answerQuestion(questionId, adminAnswer);
    });

    function submitQuestion() {
        var userQuestion = $('#userQuestion').val();
        if (!userQuestion.trim()) {
            alert("Please enter a question.");
            return;
        }

        // AJAX call to submit the question
        $.ajax({
            type: 'POST',
            url: '/submit-question',
            contentType: 'application/json',
            data: JSON.stringify({ content: userQuestion }),
            success: function(response) {
                alert('Your question has been submitted successfully!');
                $('#userQuestion').val('');  // Clear the textarea
                $('#qaModal').modal('hide'); // Hide the modal
            },
            error: function(error) {
                alert('Failed to submit the question. Please try again.');
            }
        });
    }

    function answerQuestion(questionId, adminAnswer) {
        if (!adminAnswer.trim()) {
            alert("Please enter an answer.");
            return;
        }

        // AJAX call to answer the question
        $.ajax({
            type: 'POST',
            url: '/answer-question/' + questionId,
            contentType: 'application/json',
            data: JSON.stringify({ answer: adminAnswer }),
            success: function(response) {
                alert('Answer submitted successfully!');
                $('#adminAnswer').val(''); // Clear the textarea
            },
            error: function(error) {
                alert('Failed to submit the answer. Please try again.');
            }
        });
    }



		
    // Hardcoded mapping of hotel names to room types
    const hotelRoomTypes = {
        "HolidayInn": ["Deluxe Room", "Executive Room", "Suite Room"], 
        "Radisson": ["Superior Room", "Queen Room", "Economy Room"], 
        "Sheraton": ["King Room"]
    };

    // Populate room types based on selected hotel name
    function populateRoomTypes(hotelName) {
        const roomTypes = hotelRoomTypes[hotelName] || [];
        const select = $('#select_roomTypes');
        select.empty(); // Clear existing options
        roomTypes.forEach(function (type) {
            select.append($('<option></option>').val(type).text(type));
        });
    }
  
  //add in on May_15th  
    //loadBookings('UPCOMING'); // Initial load for upcoming bookings
    $('#upcomingBtn').trigger('click');
    
    $('.filter-checkbox').change(function() {
        $('.filter-checkbox').not(this).prop('checked', false); // Ensure only one checkbox can be checked at a time
        if (this.checked) {
            loadBookings(this.value); // Load bookings based on the value of the checkbox
        } else {
            $('#booking-table-body').empty(); //clear the table or load a default set of bookings
            $('#booking-table-body').append($('<tr>').append($('<td>').attr('colspan', '9').text('Select a filter to view bookings')));
        }
    });

    // Handler for category buttons
    /*$('#upcomingBtn').click(function() { loadBookings('UPCOMING'); });
    $('#completedBtn').click(function() { loadBookings('COMPLETED'); });
    $('#canceledBtn').click(function() { loadBookings('CANCELED'); });*/

    function loadBookings(status) {
        $.ajax({
            url: '/bookings/status/' + status,     
            type: 'GET',
            success: function(bookings) {
				//console.log(bookings); 
                displayBookings(bookings, status);
            },
            error: function() {
                alert('Failed to load bookings.');
            }
        });
    }

    function displayBookings(bookings, status) {
    var $tbody = $('#booking-table-body'); //ID must matches HTML table
    $tbody.empty(); // Clear existing bookings

    console.log('Rendering bookings:', bookings);

    if (bookings.length === 0) {
        console.log('No bookings to display');
        $tbody.append($('<tr>').append($('<td>').attr('colspan', '9').text('No bookings found')));
    } else {
        bookings.forEach(function(booking) {
            console.log('Processing booking:', booking);
            var row = $('<tr>').append(
                $('<td>').text(booking.bookingId),
                $('<td>').text(booking.hotelName || 'No hotel name'), // Assuming hotelName might be undefined
                $('<td>').text(booking.checkInDate),
                $('<td>').text(booking.checkOutDate),
                $('<td>').text(booking.customerMobile),
                $('<td>').text(booking.price.toFixed(2)),
                $('<td>').text(booking.status),
                $('<td>').append($('<button>').addClass('btn btn-danger cancel-btn').text('Cancel').data('booking-id', booking.bookingId)),
                $('<td>').append($('<button>').addClass('btn btn-primary review-btn').text('Review').data('booking-id', booking.bookingId))
            );
            $tbody.append(row);
        });
    }

    attachEventHandlers();
}

function attachEventHandlers() {
    $('.cancel-btn').click(function() {
        var bookingId = $(this).data('booking-id');
        //updateBookingStatus(bookingId, 'CANCELED');
        cancelBooking(bookingId);
    });

    $('.review-btn').click(function() {
        var bookingId = $(this).data('booking-id');
        showReviewModal(bookingId);
    });
}
/*
    function updateBookingStatus(bookingId, newStatus) {
       $.ajax({
          url: '/bookings/' + bookingId + '/status?status=' + newStatus, // Include status as a query parameter
          type: 'POST',
          success: function() {
             alert('Booking status updated successfully.');
             loadBookings('UPCOMING'); // Reload to reflect changes
          },
          error: function(xhr) {
             alert('Failed to update booking status: ' + xhr.responseText);
         }
     });
  }*/
  function cancelBooking(bookingId) {
        $.ajax({
            url: '/cancel/' + bookingId,  
            type: 'POST',
            success: function(response) {
                alert('Booking cancelled successfully.');
                loadBookings($('.filter-checkbox:checked').val()); 
                //loadBookings('UPCOMING'); // Reload to reflect changes
            },
            error: function(xhr) {
                alert('Failed to cancel booking: ' + xhr.responseText);
            }
        });
    }

   
    
     function showReviewModal(bookingId) {
    $('#reviewModal').modal('show');
    $('#review-submit').data('booking-id', bookingId); // Attach booking ID to the submit button
   }

     
     $('#review-submit').click(function() {
    var bookingId = $(this).data('booking-id');
    console.log("Submit button clicked, booking ID:", bookingId);
    var reviewData = {
        bookingId: bookingId,
        text: $('#review-text').val(),
        serviceRating: parseInt($('#serviceRating').val(), 10),
        amenitiesRating: parseInt($('#amenitiesRating').val(), 10),
        bookingProcessRating: parseInt($('#bookingProcessRating').val(), 10),
        wholeExpRating: parseInt($('#wholeExpRating').val(), 10),
        overallRating: (parseInt($('#serviceRating').val(), 10) + parseInt($('#amenitiesRating').val(), 10) + parseInt($('#bookingProcessRating').val(), 10) + parseInt($('#wholeExpRating').val(), 10)) / 4
    };
    console.log("Sending review data:", reviewData);

    // AJAX call to submit the review
    $.ajax({
        url: '/review',    
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(reviewData),
        success: function(response) {
            alert('Review submitted successfully.');
            $('#reviewModal').modal('hide');
        },
        error: function(xhr) {
            alert('Failed to submit review: ' + xhr.responseText);
        }
    });
});


    // Search button handler to fetch hotels based on search location
    $("#searchBtn").click(function () {
        var searchLocation = $("#searchLocation").val();
        var noRooms = $("#noRooms").val();
        var noGuests = $("#noGuests").val();
        var checkInDate = $("#checkInDate").val();
        var checkOutDate = $("#checkOutDate").val();

        // Store the search details for later use in the modal
        sessionStorage.setItem("searchDetails", JSON.stringify({
            noRooms: noRooms,
            noGuests: noGuests,
            checkInDate: checkInDate,
            checkOutDate: checkOutDate
        }));

        // AJAX call to fetch hotels based on the search location
        $.get("/findHotel/" + searchLocation, function (response, status) {
            $("#hotelTbl tbody").empty(); // Clear the existing rows
            $.each(response, function (key, value) {
                fetchAverageRating(value.hotelId).then(function(averageRating) {
                    console.log(`Average rating for hotel ${value.hotelId}: ${averageRating}`);
                    // Adding the image and aligning the Details button properly
                    var detailsButton = "<button class='btn btn-info details-btn' data-hotel-name='" + value.hotelName + "' data-hotel-id='" + value.hotelId + "'>Details</button>";
                    var row = `<tr>
                        <td>${value.hotelName}</td>
                        <td><img height='200' width='200' src='${value.imageURL}'/></td>
                        <td>${value.starRating}</td>
                        <td>${detailsButton}</td>
                        <td class="average-rating" data-hotel-id="${value.hotelId}" style="cursor:pointer;">${averageRating.toFixed(1) || 'No rating'}</td>
                    </tr>`;
                    $("#hotelTbl tbody").append(row);
                }).fail(function(jqXHR, textStatus, errorThrown) {
                    console.error(`Failed to fetch average rating: ${jqXHR.status} : ${jqXHR.responseText}`);
                });
            });
        });

        // Filter button handler
        $("#filterBtn").click(function() {
            var ratings = "";
            var lengthRating = $(".star_rating:checked").length;
            var count = 0;
            $(".star_rating:checked").each(function(key, value) {
                if(count == lengthRating - 1) {
                    ratings += $(this).val();
                } else {
                    ratings += $(this).val() + ",";
                }
                count++;
            });

            var amount = $("#priceValue").text();  
            var hotelSearch = {"searchLocation": searchLocation, "ratings": ratings, "amount": amount};
            
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/filterHotel",
                data: JSON.stringify(hotelSearch),
                dataType: 'json',
                success: function(result) {
                    $("#hotelTbl tbody").empty(); // Clear the existing rows, except the header
                    $.each(result, function(key, value) {
                        fetchAverageRating(value.hotelId).then(function(averageRating) {
                            var detailsButton = "<button class='btn btn-info details-btn' data-hotel-name='" + value.hotelName + "' data-hotel-id='" + value.hotelId + "'>Details</button>";
                            var row = `<tr>
                                <td>${value.hotelName}</td>
                                <td><img height='200' width='200' src='${value.imageURL}'/></td>
                                <td>${value.starRating}</td>
                                <td>${detailsButton}</td>
                                <td class="average-rating" data-hotel-id="${value.hotelId}" style="cursor:pointer;">${averageRating.toFixed(1) || 'No rating'}</td>
                            </tr>`;
                            $("#hotelTbl tbody").append(row);
                        }).fail(function(jqXHR, textStatus, errorThrown) {
                            console.error(`Failed to fetch average rating: ${jqXHR.status} : ${jqXHR.responseText}`);
                        });
                    });
                },
                error: function(e) {
                    alert("Error filtering hotels.");
                }
            });
        });
    });

    // Event handler for the details button to open the modal with hotel details
    $('#hotelTbl').on('click', '.details-btn', function () {
        var hotelId = $(this).data('hotel-id');
        var hotelName = $(this).data('hotel-name');

        fetchHotelDetails(hotelId, hotelName);
    });

    // Event delegation to handle clicks on dynamically loaded average ratings
    $('#hotelTbl').on('click', '.average-rating', function () {
        var hotelId = $(this).data('hotel-id');
        fetchAndDisplayReviews(hotelId);
    });

    // Fetch hotel details and populate the modal
    function fetchHotelDetails(hotelId, hotelName) {
        $.ajax({
            url: '/searchHotelDetails/' + hotelId,
            method: 'GET',
            success: function (hotelData) {
                var searchDetails = JSON.parse(sessionStorage.getItem("searchDetails"));
                populateModal(hotelData, searchDetails, hotelName);
                $('#myModal').modal('show');
            },
            error: function () {
                alert('Error fetching hotel details.');
            }
        });
    }

    // Fetch average rating for a hotel
    function fetchAverageRating(hotelId) {
        return $.get(`/api/hotels/${hotelId}/average-rating`);
    }

    // Fetch and display reviews for a hotel
    function fetchAndDisplayReviews(hotelId) {
        $.get(`/api/hotels/${hotelId}/reviews`).done(function(reviews) {
            console.log(`Fetched reviews for hotel ${hotelId}:`, reviews);
            const list = $('#reviewsList');
            list.empty();
            reviews.forEach(function(review) {
                var item = `<li>${review.text} - Rating: ${review.overallRating}</li>`;
                list.append(item);
            });
            $('#reviewsModal').modal('show');
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.error(`Failed to load reviews: ${jqXHR.status} : ${jqXHR.responseText}`);
            alert('Failed to load reviews.');
        });
    }


    // Populate the modal with hotel and search details, including room types
    function populateModal(hotelData, searchDetails, hotelName) {
        $('#modal_hotelId').val(hotelData.hotelId);
        $('#modal_hotelName').val(hotelData.hotelName);
        $('#modal_noGuests').val(searchDetails.noGuests); // Use user input for number of guests
        $('#modal_checkInDate').val(searchDetails.checkInDate); // Use user input for check-in date
        $('#modal_checkOutDate').val(searchDetails.checkOutDate); // Use user input for check-out date
        populateRoomTypes(hotelName); // Populate room types using the hardcoded mapping
    }

    // First modal search button click to get booking details and show the second modal
    $('#btn-searchHotelRooms').on('click', function () {
        // Collect inputs from the first modal
        var modal_hotelId = $('#modal_hotelId').val();
        var noGuests = $('#modal_noGuests').val();
        var checkInDate = $('#modal_checkInDate').val();
        var checkOutDate = $('#modal_checkOutDate').val();
        var roomType = $('#select_roomTypes').val(); // dropdown
        var noRooms = $('#modal_noRooms').val();
        
       

        // Make an AJAX request to the server
        $.ajax({
            url: '/fetch-booking-details', 
            type: 'GET',
            data: {
                hotelId: modal_hotelId,
                noGuests: noGuests,
                checkInDate: checkInDate,
                checkOutDate: checkOutDate,
                roomType: roomType,
                noRooms: noRooms
            },
            success: function (response) {
                // Populate the second modal with the booking details
                $('#booking_hotelId').val(modal_hotelId);
                $('#booking_hotelName').val(response.hotelName); // Set the hotel name
                $('#booking_customerMobile').val(response.customerMobile);
                $('#booking_noGuests').val(noGuests);
                $('#booking_checkInDate').val(checkInDate);
                $('#booking_checkOutDate').val(checkOutDate);
                $('#booking_roomType').val(roomType);
                $('#booking_noRooms').val(noRooms);
                // Populate any other fields from the response if necessary
                $('#booking_discount').text(response.discount);
                $('#booking_price').text(response.price);

                // Show the second modal for confirmation
                $('#bookingHotelRoomModal').modal('show');
            },
            error: function () {
                alert('Error loading booking details.');
            }
        });
    });


//05_11
    $('.btn-confirm-booking').on('click', function () {
        // Fetch the number of guests
        var numberOfGuests = parseInt($('#noGuests').val(), 10);

        // Clear previous rows and dynamically populate the guest table with empty rows
        var tbody = $("#guestTable tbody");
        tbody.empty();
        for (var i = 0; i < numberOfGuests; i++) {
            var row = `
                <tr>
                    <td><input type="text" class="form-control guest-firstname" placeholder="First Name"></td>
                    <td><input type="text" class="form-control guest-lastname" placeholder="Last Name"></td>
                    <td><input type="number" class="form-control guest-age" placeholder="Age"></td>
                    <td>
                        <select class="form-control guest-gender">
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                        </select>
                    </td>
                </tr>`;
            tbody.append(row);
        }

        // Show the guest details modal
        $("#guestDetailsModal").modal("show");
    });

    // Event handler for the "Submit Booking" button
    $("#submitBooking").click(function() {
        var guests = [];
        $("#guestTable tbody tr").each(function() {
            var firstName = $(this).find(".guest-firstname").val();
            var lastName = $(this).find(".guest-lastname").val();
            var age = $(this).find(".guest-age").val();
            var gender = $(this).find(".guest-gender").val();
            guests.push({ firstName, lastName, age, gender });
        });

        // Collect final booking details
        var bookingData = {
            hotelId: $('#booking_hotelId').val(),
            noGuests: $('#noGuests').val(),
            checkInDate: $('#booking_checkInDate').val(),
            checkOutDate: $('#booking_checkOutDate').val(),
            roomType: $('#booking_roomType').val(),
            noRooms: $('#booking_noRooms').val(),
            customerMobile: $('#booking_customerMobile').val(),
            guests: guests
        };

        // Make AJAX POST request to submit booking
        $.ajax({
            url: '/book', // link from TravelGig BookingController
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(bookingData),
            success: function (response) {
                alert('Booking created successfully: Booking ID - ' + response.bookingId);
                $("#guestDetailsModal").modal("hide"); // Close the modal on success
                $('#bookingHotelRoomModal').modal("hide"); // Optionally, close all modals
            },
            error: function (xhr) {
                alert('Error creating booking: ' + xhr.responseText);
            }
        });
    });
 });
  
    
  

    
    





   



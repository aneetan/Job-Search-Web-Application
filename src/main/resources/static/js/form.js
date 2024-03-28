$(document).ready(function() {
    $('#educationForm').submit(function(e) {
        e.preventDefault(); // Prevent normal form submission
        var formData = $(this).serializeArray(); // Serialize form data as an array
        var jsonData = {};

        // Function to format date in "yyyy-MM" format
        function formatDate(date) {
            var yyyy = date.getFullYear().toString();
            var mm = (date.getMonth() + 1).toString().padStart(2, '0'); // January is 0!
            return yyyy + '-' + mm;
        }

        $(formData).each(function(index, obj){
            jsonData[obj.name] = obj.value;
        });

        // Parse and format start and end dates
        var startDate = new Date($('#startDate').val());
        var endDate = new Date($('#endDate').val());
        jsonData.startDate = formatDate(startDate);
        jsonData.endDate = formatDate(endDate);

        var educationData = {
            school: jsonData.school,
            degree: jsonData.degree,
            field: jsonData.field,
            grade: jsonData.grade,
            startDate: jsonData.startDate,
            endDate: jsonData.endDate,
            personalDetails: {
                userId: $('#userId').val(),
                name: $('#pName').val(),
                password: $('#password').val(),
                phoneNo: $('#phoneNo').val(),
                address: $('#address').val(),
                email: $('#email').val()
            }
        };


        $.ajax({
            type: "POST",
            url: "/submitEducation",
            data: JSON.stringify(educationData), // Send JSON data
            contentType: "application/json", // Set content type to JSON
            success: function(response) {
                // Handle success response
                alert(response);
                // console.log(jsonData)
            },
            error: function(xhr, status, error) {
                // Handle error response
                console.error(xhr.responseText);
            }
        });
    });
});


document.addEventListener('DOMContentLoaded', function() {
    var educationForm = document.getElementById('educationForm');

    educationForm.addEventListener('submit', function(event) {
        event.preventDefault();

        var eduDetails = document.querySelector('.edu-details');
        eduDetails.style.display = 'block';
    });
});


$(document).ready(function() {
    $('#experienceForm').submit(function(e) {
        e.preventDefault(); // Prevent normal form submission
        var formData = $(this).serializeArray(); // Serialize form data as an array
        var jsonData = {};

        $(formData).each(function(index, obj){
            jsonData[obj.name] = obj.value;
        });

        var experienceData = {
            title: jsonData.title,
            company: jsonData.company,
            empType: jsonData.empType,
            locationType: jsonData.locationType,
            startDateEx: jsonData.startDateEx,
            endDateEx: jsonData.endDateEx,
            personalDetails: {
                userId: $('#userIdEx').val(),
                name: $('#pNameEx').val(),
                password: $('#passwordEx').val(),
                phoneNo: $('#phoneNoEx').val(),
                address: $('#addressEx').val(),
                email: $('#emailEx').val()
            }
        };


        $.ajax({
            type: "POST",
            url: "/submitExperience",
            data: JSON.stringify(experienceData), // Send JSON data
            contentType: "application/json", // Set content type to JSON
            success: function(response) {
                $('#experienceModal').modal('hide');
                $('#exDetails').html(response);
            },
            error: function(xhr, status, error) {
                // Handle error response
                console.error(xhr.responseText);
            }
        });
    });
});

document.addEventListener('DOMContentLoaded', function() {
    var experienceForm = document.getElementById('certificateForm');

    experienceForm.addEventListener('submit', function(event) {
        event.preventDefault();

    });
});


$(document).ready(function() {
    $('#certificateForm').submit(function(e) {
        e.preventDefault(); // Prevent normal form submission
        var formData = $(this).serializeArray(); // Serialize form data as an array
        var jsonData = {};

        $(formData).each(function(index, obj){
            jsonData[obj.name] = obj.value;
        });

        var certificationFile = $('#certification')[0].files[0];

        // Create FormData object to handle file upload
        var formDataWithFile = new FormData();
        formDataWithFile.append('cerTitle', jsonData.cerTitle);
        formDataWithFile.append('certification', certificationFile);
        formDataWithFile.append('userId', $('#userId').val());
        formDataWithFile.append('name', $('#pName').val());
        formDataWithFile.append('password', $('#password').val());
        formDataWithFile.append('phoneNo', $('#phoneNo').val());
        formDataWithFile.append('address', $('#address').val());
        formDataWithFile.append('email', $('#email').val());


        $.ajax({
            type: "POST",
            url: "/submitCertificate",
            data: formDataWithFile, // Send FormData object for file upload
                contentType: false, // Set contentType to false for file upload
                processData: false, // Send JSON data
            success: function(response) {
                // $('#certificationModal').modal('hide');
                // $('#exDetails').html(response);
            },
            error: function(xhr, status, error) {
                // Handle error response
                console.error(xhr.responseText);
            }
        });
    });
});


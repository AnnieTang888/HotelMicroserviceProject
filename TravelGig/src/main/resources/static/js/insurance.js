$(document).ready(function() {
    loadProvinces();

    $('#provinceDropdown').change(function() {
        var province= $(this).val();
        province = JSON.parse(province);
        //loadCities(provinceId);
           $('#cityDropdown').empty();
            $('#cityDropdown').append('<option>Select City</option>');
            $.each(province.cities, function(key, value) {
                $('#cityDropdown').append('<option value="' + value.id + '">' + value.name + '</option>');
            });
    });
});

function loadProvinces() {
    $.ajax({
        url: '/api/provinces',
        method: 'GET',
        success: function(data) {
            $('#provinceDropdown').empty();
            $('#provinceDropdown').append('<option>Select Province</option>');
            $.each(data, function(key, value) {
                $('#provinceDropdown').append($('<option></option>').attr('value', JSON.stringify(value)).text(value.name));
                //append('<option value="' + value.id + '">' + value.name + '</option>');
            });
        }
    });
}



function saveInsurance() {
    var insuranceData = {
        name: $('#name').val(),
        age: $('#age').val(),
        salary: $('#salary').val(),
        noOfMember: $('#noOfMember').val(),
        cityId: $('#cityDropdown').val()
    };
    $.ajax({
        url: '/insurance/submit',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(insuranceData),
        success: function() {
            alert('Insurance saved successfully');
        },
        error: function() {
            alert('Error saving insurance');
        }
    });
}
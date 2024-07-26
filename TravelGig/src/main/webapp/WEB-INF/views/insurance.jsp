<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Insurance Policy Page</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="js/insurance.js"></script> <!-- Link to the external JS file -->
</head>
<body>
    <form id="insuranceForm">
        <label>Enter Name:</label><input type="text" id="name"><br>
        <label>Enter Age:</label><input type="number" id="age"><br>
        <label>Enter Salary:</label><input type="number" id="salary"><br>
        <label>No. Of members to be insured:</label><input type="number" id="noOfMember"><br>
        <label>Select Province:</label><select id="provinceDropdown"></select><br>
        <label>Select City:</label><select id="cityDropdown"></select><br>
        <button type="button" onclick="saveInsurance()">Save</button>
    </form>
</body>
</html>
$(document).ready(function() {

    $('.getEmployees').on("click", function(event){
        console.log("Clicked");
        event.preventDefault();
        get_employees_url();
    })

    $('.getEmployee').on("click", function(event){
        event.preventDefault();
        $('#right-container').load("getEmployee.html", function(){
            $('.btn-get').on("click", function (event){
                let employeeId = $("#employee-id").val();
                get_employee_url(employeeId);
            })
        })
    })

    $('.deleteEmployee').on("click", function (event){
        $('#right-container').load("deleteEmployee.html", function(){
            $('.btn-get').on("click", function (event){
                let employeeId = $("#employee-id").val();
                delete_employee_url(employeeId);
            })
        })
    })

    $('.insertEmployee').on("click", function (event){
        $('#right-container').load("insertEmployee.html", function(){
            $('.btn-get').on("click", function (event){
                let firstName = $("#employee-first-name").val();
                let lastName = $("#employee-last-name").val();
                let departmentId = $("#department-id").val();
                let jobTitle = $("#employee-job-title").val();
                insert_employee_url(firstName, lastName, departmentId, jobTitle);
            })
        })
    })

    $('.updateEmployee').on("click", function (event){
        $('#right-container').load("updateEmployee.html", function(){
            $('.btn-fill-form').on("click", function (event){
               fill_form($("#employee-id").val());
            });

            $('.btn-get').on("click", function (event){
                let employeeId = $("#employee-id").val();
                let firstName = $("#employee-first-name").val();
                let lastName = $("#employee-last-name").val();
                let departmentId = $("#department-id").val();
                let jobTitle = $("#employee-job-title").val();
                update_employee_url(employeeId, firstName, lastName, departmentId, jobTitle);
            })
        })
    })
})

function get_employees_url(){
    $.ajax({
        url: "getEmployees"
    }).then(function(data) {
        let json = JSON.stringify(data, null, 4);
        $('#right-container').html(json);
    })
}

function fill_form(employeeId){
    $.get("getEmployee/" + employeeId, function(data){
        let json = JSON.stringify(data, null, 4);
        let jsonData = JSON.parse(json);
        $("#employee-first-name").val(jsonData["firstName"]);
        $("#employee-last-name").val(jsonData['lastName']);
        $("#department-id").val(jsonData['departmentId']);
        $("#employee-job-title").val(jsonData['jobTitle']);
    })
}


function get_employee_url(employeeId){

    $.get("getEmployee/" + employeeId, function(data){
        let json = JSON.stringify(data, null, 4);
        $('#right-container').html(json);
    })
}

function delete_employee_url(employeeId){
    $.ajax({
        url: "deleteEmployee/" + employeeId,
        type: 'DELETE',
        success: function (){
            get_employees_url();
        }
    })
}

function insert_employee_url(firstName, lastName, departmentId, jobTitle){

    let employee = {};
    employee['firstName'] = firstName;
    employee['lastName'] = lastName;
    employee['departmentId'] = departmentId;
    employee['jobTitle'] = jobTitle;

    $.ajax({
        url: "insertEmployee/",
        type: 'POST',
        data: JSON.stringify(employee),
        contentType: "application/json",
        dataType: "json",
        success: function (){
            alert("Data added");
            get_employees_url();
        }
    })
}

function update_employee_url(employeeId, firstName, lastName, departmentId, jobTitle){

    let employee = {};
    employee['firstName'] = firstName;
    employee['lastName'] = lastName;
    employee['departmentId'] = departmentId;
    employee['jobTitle'] = jobTitle;

    $.ajax({
        url: "updateEmployee/" + employeeId,
        type: 'PUT',
        data: JSON.stringify(employee),
        contentType: "application/json",
        dataType: "json",
        success: function (){
            alert("Data updated");
            get_employees_url();
        }
    })
}
   $(document).ready(function() {
    $('#loader').hide();
    $("#submit").on("click", function() {
    	$("#submit").prop("disabled", true);
    	var firstName = $("#firstName").val();
        var file = $("#image").val(); 
        var lastName = $("#lastName").val();
        var email = $("#email").val();
        var form = $("#form").serialize();
    	var data = new FormData($("#form")[0]);
    	data.append('firstName', firstName);
    	data.append('lastName', lastName);
    	data.append('email', email); 
    	//alert(data);
        $('#loader').show();
        if (firstName === "" || file === "" || lastName === "" || email === "") {
        	$("#submit").prop("disabled", false);
            $('#loader').hide();
            $("#firstName").css("border-color", "red");
            $("#image").css("border-color", "red");
            $("#lastName").css("border-color", "red");
            $("#email").css("border-color", "red");
            $("#error_firstName").html("Please fill the required field.");
            $("#error_file").html("Please fill the required field.");
            $("#error_lastName").html("Please fill the required field.");
            $("#error_email").html("Please fill the required field.");
        } else {
            console.log('>>>>>>>>>>>HERE in student.js!!');
            $("#firstName").css("border-color", "");
            $("#image").css("border-color", "");
            $("#lastName").css("border-color", "");
            $("#email").css("border-color", "");
            $('#error_firstName').css('opacity', 0);
            $('#error_file').css('opacity', 0);
            $('#error_lastName').css('opacity', 0);
            $('#error_email').css('opacity', 0);
                    $.ajax({
                        type: 'POST',
                        enctype: 'multipart/form-data',
                        data: data,
                        url: "/students/saveImageDetails", 
                        processData: false,
                        contentType: false,
                        cache: false,
                        success: function(data, statusText, xhr) {
                        console.log(xhr.status);
                        if(xhr.status == "200") {
                        	$('#loader').hide(); 
                        	$("#form")[0].reset();
                        	$('#success').css('display','block');
                            $("#error").text("");
                            $("#success").html("Person Inserted OK.");
                            $('#success').delay(2000).fadeOut('slow');
                         }	   
                        },
                        error: function(e) {
                        	$('#loader').hide();
                        	$('#error').css('display','block');
                            $("#error").html("Oops! something went wrong.");
                            $('#error').delay(50000000000).fadeOut('slow');
                            location.reload();
                        }
                    });
        }
            });
        });

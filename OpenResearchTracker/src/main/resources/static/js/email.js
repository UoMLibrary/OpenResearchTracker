$(window).on("load", function () {
	let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
	
	var table = $('#emailLogTable').DataTable({
    	dom: '<"top"lfip>t<"bottom"lp><"clear">',
		columns : [
			{"data" : "sender"},
			{"data" : "subject"},
			{"data" : "message"},
			
			],
		searching : true    
    
    });
    
	$("#sendEmail").submit(function(e) {
	
	    e.preventDefault(); // avoid to execute the actual submit of the form.
	    
	    var form = $(this);
	    var subject = $('#subject').val();
	    var message = $('#message').val();
	    
	    if(subject && message){
	    	 $.ajax({
			        type: "POST",
			        url: "/superadmin/email/send",
			        headers: {[header]: token},
			        data: form.serialize(), 
			        success: function(data){			        
			           if(data=="success"){
			        	   $('.toast').toast('show');
							  
							$('.toast-title').text("Notification!");
							$('.toast-body').text("Email sent successfully!");
							
			        	   document.getElementById('sendEmail').reset();
			        	   
			           }
			           if(data=="error"){
			        	   $('.toast').toast('show');
							  
							$('.toast-title').text("Notification!");
							$('.toast-body').text("Email unable to send.\nPlease try again.");
										        	   
			           }
			        }
			    });
	    }
	    
	});
	 
});
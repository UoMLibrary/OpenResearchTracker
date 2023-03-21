$(window).on("load", function () {
	let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    
    var table = $('#userTable').DataTable({
    	dom: '<"top"lfip>t<"bottom"lp><"clear">',
		columns : [
			{"data" : "userName"},
			{"data" : "fullName"},
			{"data" : "role"},
			{"data" : null,
				defaultContent: '<i class="fas fa-pen-alt" title="edit"></i>',
				orderable : false
			},
			{"data" : null,
				defaultContent: '<i class="fas fa-trash-alt" title="delete"/>',
				orderable : false
			}

			],
		searching : true    
    
    });
    


	$("#addUser").submit(function(e) {
	
	    e.preventDefault(); // avoid to execute the actual submit of the form.
	    
	    var form = $(this);
	    var userName = $('#userName').val();
	    var fullName = $('#fullName').val();
	    var role = $('#role').val();
	    element = document.getElementById('userAlert');
	    
	    if(userName && fullName && role){
	    	 $.ajax({
			        type: "POST",
			        url: "/superadmin/users/add",
			        headers: {[header]: token},
			        data: form.serialize(), 
			        success: function(data){
			           if(data=="exists"){
			        	   element.classList.add("alert-warning");
			        	   element.classList.remove("alert-info");
			        	   element.innerHTML = userName + " already exists!";
			        	   element.setAttribute('style', 'display:block');
			        	  
			           }
			           if(data=="added"){
			        	   element.setAttribute('style', 'display:none');
			        	   $('.toast').toast('show');
							  
							$('.toast-title').text("Notification!");
							$('.toast-body').text("User created successfully!");
							
			        	   document.getElementById('addUser').reset();
			        	   
			        	   table.row.add({"userName":userName, "fullName":fullName, "role":role}).draw();
			           }
			        }
			    });
	    }
	    
	});
	
	$('#userTable tbody').on('click','.fa-pen-alt',function(event) {
		var row = table.row($(this).parents('tr'));
		var data = row.data();
		
		console.log(row);
		
		$('#editUserModal').modal("show");
		$("#updateuserName").val(data.userName);
		$("#updateuserName").attr("disabled", "disabled");
		$("#updatefullName").val(data.fullName);
		$("#updaterole").val(data.role);
		
		$('#editUserModal').find("#submitChange").off('click');
		$('#editUserModal').find("#submitChange").click(function(e){
			$.ajax({
				type: 'PUT',
				url:'/superadmin/users/update',
		        headers: {[header]: token},
				data:{"userName":$("#updateuserName").val(), "fullName":$("#updatefullName").val(), "role":$("#updaterole").val()},
				success:function(response){
					if(response=="updated"){
						
						$('.toast').toast('show');  
						$('.toast-title').text("Notification!");
						$('.toast-body').text("User updated successfully!");
						row.data({"userName":$("#updateuserName").val(), "fullName":$("#updatefullName").val(), "role":$("#updaterole").val()});
						$('#editUserModal').modal('hide');
	
					}
				}
			});
		});	
			
	});
	 
	
	$('#userTable tbody').on('click','.fa-trash-alt',function() {
		var data = table.row($(this).parents('tr')).data();
		var row = table.row($(this).parents('tr'));
		$('#deleteUser').modal("show");
		$('#deleteUser').find('#information').remove();
		$('#deleteUser').find('.modal-title').text("Notification");
		
		$('#deleteUser').find('.modal-body').text("Are you sure you want to delete the user: " + data.userName + " : " + data.fullName);
			
		$('#deleteUser').find("#confirm").off('click');
		$('#deleteUser').find('#confirm').click(function(){
			$.ajax({
		        type: "DELETE",
		        url: "/superadmin/users/delete",
		        headers: {[header]: token},
		        data: {"userName":data.userName},
		        success: function(data){
		        	
		           if(data=="deleted"){
		        	   $('.toast').toast('show');
						  
						$('.toast-title').text("Notification!");
						$('.toast-body').text("User deleted successfully!");
						
			        	$('#deleteUser').modal('hide')

						row.remove().draw();
		        	   
		           }
		        }
		    });
		});
		  
	});

});
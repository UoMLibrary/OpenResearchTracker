 function formatDate(dateString){
 	if (dateString == null){
 		return "";
 	}
 
 	var d = new Date(dateString);
    var formattedDate = [String(d.getDate()).padStart(2, "0"),
    			(String(d.getMonth()+1)).padStart(2, "0"),
               d.getFullYear()].join('/');
	 
	 return formattedDate;
 }

$(window).on("load", function () {
	let token = $("meta[name='_csrf']").attr("content");
	console.log("ready to rumble:" + $('#OARouteFilter').val());
    let header = $("meta[name='_csrf_header']").attr("content");
	if (isAdmin && !userview){// admin user who needs more advanced server side features, getting data from local DB copy of OACP records
		var table = $("#publicationTable").DataTable({
	    	dom: '<"top"rlip>t<"bottom"lp><"clear">',
			processing: true,
			serverSide: true,
			ajax: {
		        type: "GET",
		        headers: {[header]: token},
		        url : "/getPublications",
	            data: function (d) {
	                d.title = $('#titleFilter').val()
	                d.pureStatus = $('#pureStatusFilter').val();
	                d.complianceStatus = $('#complianceStatusFilter').val();
	                d.oa_route = $('#OARouteFilter').val();
	                d.Wellcome = $('#WellcomeFilter').val();
	                d.CRUK = $('#CRUKFilter').val();
	                d.BHF = $('#BHFFilter').val();
	                d.NIHR = $('#NIHRFilter').val();
	                d.ERC = $('#ERCFilter').val();
	                d.UKRI = $('#UKRIFilter').val();
	                if ($('#organisationFilter').val() == ""){
	                	d.organisationLevel = "-1";
	                	d.organisation = "";
	                }
	                else{
	                	d.organisationLevel = $('#organisationFilter option:selected').closest('optgroup').attr('value');
	                	d.organisation = $('#organisationFilter option:selected').val();
	                }               
	            }
			},
			columnDefs: [{
			    "targets": '_all',
			    "defaultContent": "-"
			}],
			columns : [
				{"data" : "pureId"},
				{"data" : "pure_status"},
				{"data" : "title"},
				{"data" : "compliance_status"},
				{"data" : "publisher"},
				{"data" : "publication_date", render:formatDate},
				{"data" : "oa_route"},
				{"data" : "status"},
				{"data" : "accepted_date", render:formatDate},
				{"data" : "oacp_createdDate", render:formatDate},
				],
				order : [ 0, "desc" ],
				fnDrawCallback: function () {

				  $('#publicationTable tbody tr').click(function () {
								
				    // return if no data
				    if (!table.row(this).data()){return;}
				    
				    // get pureId of row
				    var id = table.row(this).data()['pureId'];
					
				    // redirect
				    window.location.href = '/admin/publication/' + id
				  })
				
				}
		});
		
		$(".btnReset").bind("click", function(event) {
			 
			$('#titleFilter').val("");
	        $('#pureStatusFilter').val("");
			$('#complianceStatusFilter').val("");
			$('#OARouteFilter').val("");
			$('#WellcomeFilter').val("");
			$('#CRUKFilter').val("");
			$('#BHFFilter').val("");
			$('#NIHRFilter').val("");
			$('#ERCFilter').val("");
			$('#UKRIFilter').val("");
			$('#acceptedDateFilter').val("");
			$('#publicationDateFilter').val("");
			$('#organisationFilter').val("");
			table.ajax.reload();
			
		}); 			
		
		$(".column_filter").bind("change keyup",function(event) {
			table.ajax.reload();
		});
		
	}else{// local user wanting data from OACPv2
	    var table = $('#publicationTable').DataTable({
	    	dom: '<"top"lip>t<"bottom"lp><"clear">',
	    	columnDefs: [{
			    "targets": '_all',
			    "defaultContent": "-"
			}],
			columns : [
				{"data" : "pureId"},
				{"data" : "pure_status"},
				{"data" : "title"},
				{"data" : "compliance_status"},
				{"data" : "publisher"},
				{"data" : "publication_date"},
				{"data" : "oa_route"},
				{"data" : "status"},
				{"data" : "accepted_date"},
				{"data" : "oacp_createdDate"},
				
				],
				fnDrawCallback: function () {

				  $('#publicationTable tbody tr').click(function () {
								
				    // value of the first column (can be hidden)
				    var id = table.row(this).data()['pureId'];
					
					console.log(id);
				    // redirect
				    document.location.href = '/publication/' + id
				  })
				
				}
	    });
	}
	
	
    
});
$(function() {

	// init date tables
	var jobTable = $("#job_list").dataTable({
		"deferRender": true,
		"processing" : true,
	    "serverSide": true,
		"ajax": {
			url: base_url + "/jobinfo/pageList",
			type:"post",
	        data : function ( d ) {
	        	var obj = {};
	        	obj.jobGroup = $('#jobGroup').val();
                obj.status = $('#status').val();
                obj.jobName = $('#jobName').val();
	        	obj.executorHandler = $('#executorHandler').val();
	        	obj.pageNo = d.pageNo;
	        	obj.pageSize = d.pageSize;
                return obj;
            }
	    },
	    "searching": false,
	    "ordering": false,
	    //"scrollX": true,	// scroll x，close self-adaption
	    "columns": [
	                {
	                	"data": 'id',
						"bSortable": false,
						"visible" : true,
						"width":'5%'
					},
	                {
	                	"data": 'executorHandler',
	                	"visible" : true
	                	// "render": function ( data, type, row ) {
	            		// 	var groupMenu = $("#jobGroup").find("option");
						// 	if (groupMenu.length > 0) {
						// 		for ( var index in $("#jobGroup").find("option")) {
						// 			if ($(groupMenu[index]).attr('value') == data) {
						// 				return $(groupMenu[index]).html();
						// 			}
						// 		}
						// 	}
	            		// 	return data;
	            		// }
            		},
	                {
	                	"data": 'jobName',
						"visible" : true,
						"width":'25%'
					},
	                {
	                	"data": 'cronExpression',
						"visible" : true,
						"width":'10%'
					},

					{
						"data": 'status',
						"width":'10%',
						"visible" : true,
						"render": function ( data, type, row ) {
							console.log('data: ', data);

							// status
							if (1 == data) {
								return "<div class=\"material-switch pull-left\">\n" +
									"                                <input id='switch-status"+row.id+"' onclick=\"selectCheckbox(this)\" type=\"checkbox\" checked />\n" +
									"                                <label for='switch-status"+row.id+"' class=\"label-success\"></label>\n" +
									"                            </div>";
							} else {
								return  "<div class=\"material-switch pull-left\">\n" +
									"                                <input id='switch-status"+row.id+"' onclick=\"selectCheckbox(this)\" type=\"checkbox\" />\n" +
									"                                <label for='switch-status"+row.id+"' class=\"label-success\"></label>\n" +
									"                            </div>";
							}
							return data;
						}
					},
					{
						"data": 'concurrent',
						"visible" : true,
						"width":'5%',
						"render": function ( data, type, row ) {
							if (1 == data) {
								return "是";
							} else {
								return "否";
							}
						}
					},
					{
						"data": 'misfirePolicy',
						"visible" : true,
						"width":'5%'
					},


					{ "data": 'executorParam', "visible" : true},


					{
						"data": 'createTime',
						"visible" : true,
						"render": function ( data, type, row ) {
							return data?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
						}
					},

	                {
	                	"data": 'updateTime',
	                	"visible" : true,
	                	"render": function ( data, type, row ) {
	                		return data?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
	                	}
	                },


	                {
						"data": I18n.system_opt ,
						"width":'10%',
	                	"render": function ( data, type, row ) {
	                		return function(){

                                // status
                                var start_stop_div = "";
                                if (1 == row.status ) {
                                    start_stop_div = '<li><a href="javascript:void(0);" class="job_operate" _type="job_pause" >'+ I18n.jobinfo_opt_stop +'</a></li>\n';
                                } else {
                                    start_stop_div = '<li><a href="javascript:void(0);" class="job_operate" _type="job_resume" >'+ I18n.jobinfo_opt_start +'</a></li>\n';
                                }
								console.log('let me see: ' + row.status + ' ', start_stop_div);
								// job_next_time_html
								var job_next_time_html = '';


                                // log url
                                var logHref = base_url +'/joblog?jobId='+ row.id;

                                // code url
                                var codeBtn = "";
                                // if ('BEAN' != row.glueType) {
                                //     var codeUrl = base_url +'/jobcode?jobId='+ row.id;
                                //     codeBtn = '<li><a href="'+ codeUrl +'" target="_blank" >GLUE IDE</a></li>\n';
                                //     codeBtn += '<li class="divider"></li>\n';
                                // }

                                // data
                                tableData['key'+row.id] = row;

                                // opt
                                // var html = '<div class="btn-group">\n' +
                                //     '     <button type="button" class="btn btn-primary btn-sm">'+ I18n.system_opt +'</button>\n' +
                                //     '     <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">\n' +
                                //     '       <span class="caret"></span>\n' +
                                //     '       <span class="sr-only">Toggle Dropdown</span>\n' +
                                //     '     </button>\n' +
                                //     '     <ul class="dropdown-menu" role="menu" _id="'+ row.id +'" >\n' +
                                //     '       <li><a href="javascript:void(0);" class="job_trigger" >'+ I18n.jobinfo_opt_run +'</a></li>\n' +
                                //     '       <li><a href="javascript:void(0);" class="job_pause" >'+ I18n.jobinfo_opt_pause +'</a></li>\n' +
                                //     '       <li><a href="javascript:void(0);" class="job_resume" >'+ I18n.jobinfo_opt_resume +'</a></li>\n' +
                                //     // '       <li><a href="'+ logHref +'">'+ I18n.jobinfo_opt_log +'</a></li>\n' +
                                //     // '       <li><a href="javascript:void(0);" class="job_registryinfo" >' + I18n.jobinfo_opt_registryinfo + '</a></li>\n' +
								// 	job_next_time_html +
                                //     '       <li class="divider"></li>\n' +
                                //     codeBtn +
                                //     start_stop_div +
                                //     '       <li><a href="javascript:void(0);" class="update" >'+ I18n.system_opt_edit +'</a></li>\n' +
                                //     '       <li><a href="javascript:void(0);" class="job_operate" _type="job_del" >'+ I18n.system_opt_del +'</a></li>\n' +
								// 	// '       <li><a href="javascript:void(0);" class="job_copy" >'+ I18n.system_opt_copy +'</a></li>\n' +
                                //     '     </ul>\n' +
                                //     '   </div>';
								var html = '<div class="btn-group">\n' +
								    '     <button type="button" class="btn btn-danger btn-sm">'+ "删除" +'</button>\n' +
								    '     <button type="button" class="btn btn-primary btn-sm">'+ "编辑" +'</button>\n' +
								    ' 	  <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">\n' +
									'    ... '+
									'     </button>\n' +
								    '     <ul class="dropdown-menu" role="menu" _id="'+ row.id +'" >\n' +
								    '       <li><a class="dropdown-item" href="javascript:void(0);" class="job_trigger" >'+ I18n.jobinfo_opt_run +'</a></li>\n' +
								    '       <li><a class="dropdown-item" href="javascript:void(0);" class="job_pause" >'+ I18n.jobinfo_opt_pause +'</a></li>\n' +
								    '     </ul>\n' +
									'   </div>';
	                			return html;
							};
	                	}
	                }
	            ],
		"language" : {
			"sProcessing" : I18n.dataTable_sProcessing ,
			"sLengthMenu" : I18n.dataTable_sLengthMenu ,
			"sZeroRecords" : I18n.dataTable_sZeroRecords ,
			"sInfo" : I18n.dataTable_sInfo ,
			"sInfoEmpty" : I18n.dataTable_sInfoEmpty ,
			"sInfoFiltered" : I18n.dataTable_sInfoFiltered ,
			"sInfoPostFix" : "",
			"sSearch" : I18n.dataTable_sSearch ,
			"sUrl" : "",
			"sEmptyTable" : I18n.dataTable_sEmptyTable ,
			"sLoadingRecords" : I18n.dataTable_sLoadingRecords ,
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : I18n.dataTable_sFirst ,
				"sPrevious" : I18n.dataTable_sPrevious ,
				"sNext" : I18n.dataTable_sNext ,
				"sLast" : I18n.dataTable_sLast
			},
			"oAria" : {
				"sSortAscending" : I18n.dataTable_sSortAscending ,
				"sSortDescending" : I18n.dataTable_sSortDescending
			}
		}
	});

    // table data
    var tableData = {};

	// search btn
	$('#searchBtn').on('click', function(){
		jobTable.fnDraw();
	});

	// jobGroup change
	$('#jobGroup').on('change', function(){
        //reload
        var jobGroup = $('#jobGroup').val();
        window.location.href = base_url + "/jobinfo?jobGroup=" + jobGroup;
    });

	// job operate
	$("#job_list").on('click', '.job_operate',function() {
		var typeName;
		var url;
		var needFresh = false;

		var type = $(this).attr("_type");
		if ("job_pause" == type) {
			typeName = I18n.jobinfo_opt_stop ;
			url = base_url + "/jobinfo/stop";
			needFresh = true;
		} else if ("job_resume" == type) {
			typeName = I18n.jobinfo_opt_start ;
			url = base_url + "/jobinfo/start";
			needFresh = true;
		} else if ("job_del" == type) {
			typeName = I18n.system_opt_del ;
			url = base_url + "/jobinfo/remove";
			needFresh = true;
		} else {
			return;
		}

		var id = $(this).parents('ul').attr("_id");

		layer.confirm( I18n.system_ok + typeName + '?', {
			icon: 3,
			title: I18n.system_tips ,
            btn: [ I18n.system_ok, I18n.system_cancel ]
		}, function(index){
			layer.close(index);

			$.ajax({
				type : 'POST',
				url : url,
				data : {
					"id" : id
				},
				dataType : "json",
				success : function(data){
					if (data.code == 200) {
                        layer.msg( typeName + I18n.system_success );
                        if (needFresh) {
                            //window.location.reload();
                            jobTable.fnDraw(false);
                        }
					} else {
                        layer.msg( data.msg || typeName + I18n.system_fail );
					}
				}
			});
		});
	});

    // job trigger
    $("#job_list").on('click', '.job_trigger',function() {
        var id = $(this).parents('ul').attr("_id");
        var row = tableData['key'+id];

        $("#jobTriggerModal .form input[name='id']").val( row.id );
        $("#jobTriggerModal .form textarea[name='executorParam']").val( row.executorParam );

        $('#jobTriggerModal').modal({backdrop: false, keyboard: false}).modal('show');
    });


    $("#jobTriggerModal .ok").on('click',function() {
        $.ajax({
            type : 'POST',
            url : base_url + "/jobinfo/trigger",
            data : {
                "id" : $("#jobTriggerModal .form input[name='id']").val(),
                "executorParam" : $("#jobTriggerModal .textarea[name='executorParam']").val(),
				"addressList" : $("#jobTriggerModal .textarea[name='addressList']").val()
            },
            dataType : "json",
            success : function(data){
                if (data.code == 200) {
                    $('#jobTriggerModal').modal('hide');

                    layer.msg( I18n.jobinfo_opt_run + I18n.system_success );
                } else {
                    layer.msg( data.msg || I18n.jobinfo_opt_run + I18n.system_fail );
                }
            }
        });
    });
    $("#jobTriggerModal").on('hide.bs.modal', function () {
        $("#jobTriggerModal .form")[0].reset();
    });


    // job registryinfo
    $("#job_list").on('click', '.job_registryinfo',function() {
        var id = $(this).parents('ul').attr("_id");
        var row = tableData['key'+id];

        var jobGroup = row.jobGroup;

        $.ajax({
            type : 'POST',
            url : base_url + "/jobgroup/loadById",
            data : {
                "id" : jobGroup
            },
            dataType : "json",
            success : function(data){

                var html = '<div>';
                if (data.code == 200 && data.content.addressList) {
                    // for (var index in data.content.addressList) {
                        html += (parseInt(1) + '. <span class="badge bg-green" >' + data.content.addressList + '</span><br>');
                    // }
                }
                html += '</div>';

                layer.open({
                    title: I18n.jobinfo_opt_registryinfo ,
                    btn: [ I18n.system_ok ],
                    content: html
                });

            }
        });

    });

    // job_next_time
    $("#job_list").on('click', '.job_next_time',function() {
        var id = $(this).parents('ul').attr("_id");
        var row = tableData['key'+id];

        $.ajax({
            type : 'POST',
            url : base_url + "/jobinfo/nextTriggerTime",
            data : {
                "scheduleType" : row.scheduleType,
				"scheduleConf" : row.scheduleConf
            },
            dataType : "json",
            success : function(data){

            	if (data.code != 200) {
                    layer.open({
                        title: I18n.jobinfo_opt_next_time ,
                        btn: [ I18n.system_ok ],
                        content: data.msg
                    });
				} else {
                    var html = '<center>';
                    if (data.code == 200 && data.content) {
                        for (var index in data.content) {
                            html += '<span>' + data.content[index] + '</span><br>';
                        }
                    }
                    html += '</center>';

                    layer.open({
                        title: I18n.jobinfo_opt_next_time ,
                        btn: [ I18n.system_ok ],
                        content: html
                    });
				}

            }
        });

    });

	// add
	$(".add").click(function(){

		// init-cronGen
        $("#addModal .form input[name='schedule_conf_CRON']").show().siblings().remove();
        $("#addModal .form input[name='schedule_conf_CRON']").cronGen({});

		// 》init scheduleType
		$("#updateModal .form select[name=scheduleType]").change();

		// 》init glueType
		$("#updateModal .form select[name=glueType]").change();

		$('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var addModalValidate = $("#addModal .form").validate({
		errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,
        rules : {
			jobDesc : {
				required : true,
				maxlength: 50
			},
			author : {
				required : true
			}/*,
            executorTimeout : {
                digits:true
            },
            executorFailRetryCount : {
                digits:true
            }*/
        },
        messages : {
            jobDesc : {
            	required : I18n.system_please_input + I18n.jobinfo_field_jobdesc
            },
            author : {
            	required : I18n.system_please_input + I18n.jobinfo_field_author
            }/*,
            executorTimeout : {
                digits: I18n.system_please_input + I18n.system_digits
            },
            executorFailRetryCount : {
                digits: I18n.system_please_input + I18n.system_digits
            }*/
        },
		highlight : function(element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        success : function(label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },
        errorPlacement : function(error, element) {
            element.parent('div').append(error);
        },
        submitHandler : function(form) {

			// process executorTimeout+executorFailRetryCount
            var executorTimeout = $("#addModal .form input[name='executorTimeout']").val();
            if(!/^\d+$/.test(executorTimeout)) {
                executorTimeout = 0;
			}
            $("#addModal .form input[name='executorTimeout']").val(executorTimeout);
            var executorFailRetryCount = $("#addModal .form input[name='executorFailRetryCount']").val();
            if(!/^\d+$/.test(executorFailRetryCount)) {
                executorFailRetryCount = 0;
            }
            $("#addModal .form input[name='executorFailRetryCount']").val(executorFailRetryCount);

            // process schedule_conf
			var scheduleType = $("#addModal .form select[name='scheduleType']").val();
			var scheduleConf;
			if (scheduleType == 'CRON') {
				scheduleConf = $("#addModal .form input[name='cronGen_display']").val();
			} else if (scheduleType == 'FIX_RATE') {
				scheduleConf = $("#addModal .form input[name='schedule_conf_FIX_RATE']").val();
			} else if (scheduleType == 'FIX_DELAY') {
				scheduleConf = $("#addModal .form input[name='schedule_conf_FIX_DELAY']").val();
			}
			$("#addModal .form input[name='scheduleConf']").val( scheduleConf );

        	$.post(base_url + "/jobinfo/add",  $("#addModal .form").serialize(), function(data, status) {
    			if (data.code == "200") {
					$('#addModal').modal('hide');
					layer.open({
						title: I18n.system_tips ,
                        btn: [ I18n.system_ok ],
						content: I18n.system_add_suc ,
						icon: '1',
						end: function(layero, index){
							jobTable.fnDraw();
							//window.location.reload();
						}
					});
    			} else {
					layer.open({
						title: I18n.system_tips ,
                        btn: [ I18n.system_ok ],
						content: (data.msg || I18n.system_add_fail),
						icon: '2'
					});
    			}
    		});
		}
	});
	$("#addModal").on('hide.bs.modal', function () {
        addModalValidate.resetForm();
		$("#addModal .form")[0].reset();
		$("#addModal .form .form-group").removeClass("has-error");
		$(".remote_panel").show();	// remote

		$("#addModal .form input[name='executorHandler']").removeAttr("readonly");
	});

	// scheduleType change
	// $(".scheduleType").change(function(){
	// 	var scheduleType = $(this).val();
	// 	$(this).parents("form").find(".schedule_conf").hide();
	// 	$(this).parents("form").find(".schedule_conf_" + scheduleType).show();
	//
	// });

    // glueType change
    // $(".glueType").change(function(){
	// 	// executorHandler
    //     var $executorHandler = $(this).parents("form").find("input[name='executorHandler']");
    //     var glueType = $(this).val();
    //     if ('BEAN' != glueType) {
    //         $executorHandler.val("");
    //         $executorHandler.attr("readonly","readonly");
    //     } else {
    //         $executorHandler.removeAttr("readonly");
    //     }
    // });

	$("#addModal .glueType").change(function(){
		// glueSource
		var glueType = $(this).val();
		if ('GLUE_GROOVY'==glueType){
			$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_java").val() );
		} else if ('GLUE_SHELL'==glueType){
			$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_shell").val() );
		} else if ('GLUE_PYTHON'==glueType){
			$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_python").val() );
		} else if ('GLUE_PHP'==glueType){
            $("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_php").val() );
        } else if ('GLUE_NODEJS'==glueType){
			$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_nodejs").val() );
		} else if ('GLUE_POWERSHELL'==glueType){
            $("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_powershell").val() );
        } else {
            $("#addModal .form textarea[name='glueSource']").val("");
		}
	});

	// update
	$("#job_list").on('click', '.update', function() {

        var id = $(this).parents('ul').attr("_id");
        var row = tableData['key'+id];
		console.log('日志：', row);
		// fill base
		$("#updateModal .form input[name='id']").val( row.id );
		// $('#updateModal .form select[name=jobGroup] option[value='+ row.jobGroup +']').prop('selected', true);
		$("#updateModal .form input[name='jobGroup']").val( row.jobGroup );
		$("#updateModal .form input[name='jobName']").val( row.jobName );
		$("#updateModal .form input[name='cronExpression']").val( row.cronExpression );
		$("#updateModal .form input[name='executorHandler']").val( row.executorHandler );
		console.log('status: ', row.status);
		console.log('statusT: ', typeof row.status);
		$("#updateModal .form input[id='status']").val(row.status)
		$("#updateModal .form input[id='switch-status']").prop('checked', parseInt(row.status));
		$("#updateModal .form input[name='jobServerId']").val( row.jobServerId );
		$("#updateModal .form input[name='concurrent']").val( row.concurrent );
		$("#updateModal .form input[name='misfirePolicy']").val( row.misfirePolicy );

		$("#updateModal .form textarea[name='executorParam']").val( row.executorParam );


		// 》init-cronGen
		$("#updateModal .form input[name='cronExpression']").show().siblings().remove();
		$("#updateModal .form input[name='cronExpression']").cronGen({});

		// show
		$('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var updateModalValidate = $("#updateModal .form").validate({
		errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,

		rules : {
			jobDesc : {
				required : true,
				maxlength: 50
			},
			author : {
				required : true
			}
		},
		messages : {
			jobDesc : {
                required : I18n.system_please_input + I18n.jobinfo_field_jobdesc
			},
			author : {
				required : I18n.system_please_input + I18n.jobinfo_field_author
			}
		},
		highlight : function(element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        success : function(label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },
        errorPlacement : function(error, element) {
            element.parent('div').append(error);
        },
        submitHandler : function(form) {

            // process executorTimeout + executorFailRetryCount
            var executorTimeout = $("#updateModal .form input[name='executorTimeout']").val();
            if(!/^\d+$/.test(executorTimeout)) {
                executorTimeout = 0;
            }
            $("#updateModal .form input[name='executorTimeout']").val(executorTimeout);
            var executorFailRetryCount = $("#updateModal .form input[name='executorFailRetryCount']").val();
            if(!/^\d+$/.test(executorFailRetryCount)) {
                executorFailRetryCount = 0;
            }
            $("#updateModal .form input[name='executorFailRetryCount']").val(executorFailRetryCount);


			// process schedule_conf
			var scheduleType = $("#updateModal .form select[name='scheduleType']").val();
			var scheduleConf;
			if (scheduleType == 'CRON') {
				scheduleConf = $("#updateModal .form input[name='cronGen_display']").val();
			} else if (scheduleType == 'FIX_RATE') {
				scheduleConf = $("#updateModal .form input[name='schedule_conf_FIX_RATE']").val();
			} else if (scheduleType == 'FIX_DELAY') {
				scheduleConf = $("#updateModal .form input[name='schedule_conf_FIX_DELAY']").val();
			}
			$("#updateModal .form input[name='scheduleConf']").val( scheduleConf );

			// post
    		$.post(base_url + "/jobinfo/update", $("#updateModal .form").serialize(), function(data, status) {
    			if (data.code == "200") {
					$('#updateModal').modal('hide');
					layer.open({
						title: I18n.system_tips ,
                        btn: [ I18n.system_ok ],
						content: I18n.system_update_suc ,
						icon: '1',
						end: function(layero, index){
							//window.location.reload();
							jobTable.fnDraw();
						}
					});
    			} else {
					layer.open({
						title: I18n.system_tips ,
                        btn: [ I18n.system_ok ],
						content: (data.msg || I18n.system_update_fail ),
						icon: '2'
					});
    			}
    		});
		}
	});
	$("#updateModal").on('hide.bs.modal', function () {
        updateModalValidate.resetForm();
        $("#updateModal .form")[0].reset();
        $("#updateModal .form .form-group").removeClass("has-error");
	});



    /**
	 * find title by name, GlueType
     */
	function findGlueTypeTitle(glueType) {
		var glueTypeTitle;
        $("#addModal .form select[name=glueType] option").each(function () {
            var name = $(this).val();
            var title = $(this).text();
            if (glueType == name) {
                glueTypeTitle = title;
                return false
            }
        });
        return glueTypeTitle;
    }

    // job_copy
	$("#job_list").on('click', '.job_copy',function() {

		var id = $(this).parents('ul').attr("_id");
		var row = tableData['key'+id];

		// fill base
		$('#addModal .form select[name=jobGroup] option[value='+ row.jobGroup +']').prop('selected', true);
		$("#addModal .form input[name='jobDesc']").val( row.jobDesc );
		$("#addModal .form input[name='author']").val( row.author );
		$("#addModal .form input[name='alarmEmail']").val( row.alarmEmail );

		// fill trigger
		$('#addModal .form select[name=scheduleType] option[value='+ row.scheduleType +']').prop('selected', true);
		$("#addModal .form input[name='scheduleConf']").val( row.scheduleConf );
		if (row.scheduleType == 'CRON') {
			$("#addModal .form input[name='schedule_conf_CRON']").val( row.scheduleConf );
		} else if (row.scheduleType == 'FIX_RATE') {
			$("#addModal .form input[name='schedule_conf_FIX_RATE']").val( row.scheduleConf );
		} else if (row.scheduleType == 'FIX_DELAY') {
			$("#addModal .form input[name='schedule_conf_FIX_DELAY']").val( row.scheduleConf );
		}

		// 》init scheduleType
		$("#addModal .form select[name=scheduleType]").change();

		// fill job
		$('#addModal .form select[name=glueType] option[value='+ row.glueType +']').prop('selected', true);
		$("#addModal .form input[name='executorHandler']").val( row.executorHandler );
		$("#addModal .form textarea[name='executorParam']").val( row.executorParam );

		// 》init glueType
		$("#addModal .form select[name=glueType]").change();

		// 》init-cronGen
		$("#addModal .form input[name='schedule_conf_CRON']").show().siblings().remove();
		$("#addModal .form input[name='schedule_conf_CRON']").cronGen({});

		// fill advanced
		$('#addModal .form select[name=executorRouteStrategy] option[value='+ row.executorRouteStrategy +']').prop('selected', true);
		$("#addModal .form input[name='childJobId']").val( row.childJobId );
		$('#addModal .form select[name=misfireStrategy] option[value='+ row.misfireStrategy +']').prop('selected', true);
		$('#addModal .form select[name=executorBlockStrategy] option[value='+ row.executorBlockStrategy +']').prop('selected', true);
		$("#addModal .form input[name='executorTimeout']").val( row.executorTimeout );
		$("#addModal .form input[name='executorFailRetryCount']").val( row.executorFailRetryCount );

		// show
		$('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
	});

});


function selectCheckbox(e){
	if ( e.checked == true){
		$('#status').val("1")
	}else{
		$('#status').val("0")
	}
	console.log($('#status').val());
}
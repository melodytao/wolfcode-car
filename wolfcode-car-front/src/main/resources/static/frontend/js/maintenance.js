$(function () {
    $('.header').load('header.html');
    $('.footer').load('footer.html');
    $('#appointmentTime').datetimepicker({
        language: 'zh-CN',
        format: 'yyyy-mm-dd hh:ii',
        startDate: new Date(),
        autoclose: true
    })
    $('#maintenanceForm').bootstrapValidator({
        live: 'disabled',
        fields: {
            phoneNum: {
                validators: {
                    notEmpty: {
                        message: '请填写手机号码'
                    },
                    regexp: {
                        regexp: /^1\d{10}$/,
                        message: '请输入正确的手机号'
                    },
                }
            },
            verification: {
                validators: {
                    notEmpty: {
                        message: '请填写验证码'
                    }
                }
            },
            customerName: {
                validators: {
                    notEmpty: {
                        message: '请填写您的姓名'
                    }
                }
            },
            carSeries: {
                validators: {
                    notEmpty: {
                        message: '请填写您的姓名'
                    }
                }
            },
            dateVal: {
                validators: {
                    notEmpty: {
                        message: '请选择预约时间'
                    }
                }
            },
            "agree[]": {
                validators: {
                    notEmpty: {
                        message: '请先阅读同意隐私政策'
                    }
                }
            },
        }
    });
    $('#sendCode').on('click', function () {
        if ($('#phoneNumber').val()) {
            $.ajax({
                type : "POST",
                url : baseUrl + "/business/appointment/smsCode?phone="+$('#phoneNumber').val(),
                success : function(result) {
                    if(result.code === 200){
                        layer.msg('发送成功', {icon: 1, time: 2000});
                    }else{
                        layer.msg(result.msg, {icon: 2, time: 2000});
                    }
                }
            });
        } else {
            layer.msg('请先输入手机号', {icon: 2, time: 2000});
        }
    })
    $('#submitBtn').on('click', function () {
        $('#maintenanceForm').bootstrapValidator('validate');
        if ($("#maintenanceForm").data('bootstrapValidator').isValid()) {//获取验证结果，如果成功，执行下面代码
            var formData = {
                customerPhone: $('#phoneNumber').val(),
                customerName: $('#customerName').val() + ( $('input[name="sex"]:checked').val() === '1' ? '先生' : '女士'),
                code: $('#verificationCode').val(),
                carSeries: $('#carSeries').val(),
                appointmentTime: $('#appointmentTime').val(),
                serviceType:$('input[name="serviceType"]:checked').val(),
                info: $('#problemDescription').val(),
            }
            layer.confirm('确定提交吗?', {title: '提示'}, function (index) {
                 $.ajax({
                     type : "POST",
                     contentType: "application/json;charset=UTF-8",
                     url : baseUrl + "/business/appointment/reservation",
                     data : JSON.stringify(formData),
                     success : function(result) {
                         if(result.code === 200){
                             $("#maintenanceForm input").val("");
                             $('#problemDescription').val("")
                             layer.msg('提交成功', {icon: 1, time: 2000});
                             layer.close(index);
                         }else{
                             layer.msg(result.msg, {icon: 2, time: 2000});
                             layer.close(index);
                         }
                     }
                 });

            });

        }
    });
    // 发送验证码
    var count = 60, timer = null;
    $(".send-btn").click(function () {
        var phone = $('#phoneNumber').val();
        if (!(/^1(3|4|5|6|7|8|9)\d{9}$/.test(phone))) {
            alert("手机号码有误，请重填");
            return false;
        }
        /*防止重复点击按钮*/
        if (timer == null) {
            $.ajax({
                url:"/business/appointment/smsCode",
                data:{phone:phone},
                success:function(result){
                    if(result.success){
                        $(".send-btn").text("60s");
                        timer = setInterval(function () {
                            count--;
                            $(".send-btn").text(count + "s");
                            if (count <= 0) {
                                clearInterval(timer);
                                $(".send-btn").text("发送验证码");
                                timer = null;
                            }
                        }, 1000);
                    }else{
                        alert(data.msg);
                    }
                }
            });

        }
    });
});

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>宇环软件</title>
    <link rel="icon" href="/assets/images/biao.png">
    <link href="./assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <style>
        * {
            margin: 0;
            padding: 0;
            font-family: 微软雅黑,'Microsoft Yahei',黑体;
        }

        .content {
            padding: 20px;
            height: auto;
            overflow: hidden;
        }

        .hideData {
            display: none;
        }

        .btn {
            border-radius: 3px;
        }

        .btn-primary {
            background-color: #39ABD2;
            border: 1px solid #39ABD2
        }

        .btn-success {
            background-color: #5BB85C;
        }

        h1 {
            background: #FFFAE5;
            margin: 0;
            padding: 15px 20px;
            font-size: 18px;
            text-align: center;
            border-bottom: 1px solid #FFDC00;
            color: #FE6600
        }

        .box {
            background: #EFFFFE;
            border: 1px solid #39ABD2;
            padding: 15px 20px;
            border-radius: 3px
        }

        .box .title {
            color: #FE6600;
            font-size: 12px
        }

        .box .title strong {
            font-size: 14px;
            color: #337AB7;
        }

        .box .body ol {
            margin: 0;
            padding: 0;
            padding-left: 12px
        }

        .box .body ol li {
            line-height: 20px
        }

        .box .body ol li,.box .body ol li a {
            color: #666;
            font-size: 12px
        }

        .mt20 {
            margin-top: 20px
        }

        ul {
            list-style: none
        }

        ul li {
            line-height: 30px
        }

        ul li strong {
            color: #C3403A
        }

        .info {
            background: url(./assets/images/security.png) 90% 30% no-repeat;
            background-size: 84px 84px
        }

        @media all and (max-width: 500px) {
            h1 {
                font-size:12px
            }

            .box {
                display: none
            }

            .mt20 {
                margin-top: 0px
            }

            .content {
                padding: 10px;
                padding-top: 10px
            }

            .info {
                background-position: 100% 30%;
                background-size: 48px 48px
            }
            ul li img {
                width: 100px
            }
        }
    </style>
    <script src="./assets/js/jquery.min.js" type="text/javascript"></script>
</head>
<body>
<h1>请保存好订单号，方便日后查询交易记录。</h1>
<div class="content">
    <input type="hidden" name="userGame" value="${userGame}">
    <input type="hidden" name="phone" value="${phone}">
    <input type="hidden" name="ids" value="${ids}">
    <input type="hidden" name="quantity" value="${quantity}">
    <input type="hidden" name="serviceRange" value="${serviceRange}">
    <input type="hidden" name="orderId" value="${orderId}">
    <input type="hidden" name="refresh" value="1">
    <div class="container">
        <div class="row">
            <div class="col-xs-12 info">
                <ul>
                    <li>
                        订单编号：<strong>${orderId}</strong>
                    </li>
                    <li>
                        所在大区：<strong>${serviceRange}</strong>
                    </li>
                    <li>
                        支付金额：<strong>${amount}</strong>
                        元
                    </li>
                    <li>
                        支付方式：<img src="./assets/images/wxscan.gif" align="absmiddle">
                    </li>
                    <li>
                        商品名称：<strong>${productName}</strong>
                    </li>
                    <li>
                        购买数量：<strong>${quantity}</strong>
                    </li>
                </ul>
            </div>
        </div>
        <div style="text-align: center;display: none;" id="wxPayCode">
            <img src="" id="imaPath">
            <ul>
            <li>
                <strong style="color: red;">提示:请使用微信扫码支付</strong>
            </li>
            </ul>
        </div>

        <div class="box">
            <div class="title">
                <strong>免责声明</strong>
                &nbsp;向陌生人付款，请注意交易风险！
            </div>
            <div class="body">
                <ol>
                    <li>由此产生的交易纠纷由双方自行处理，与宇环软件无关</li>
                    <li>
                        <a href="">支付超时、支付失败、或已扣费显示“未付款”的说明</a>
                    </li>
                    <li>
                        <a href="">使用购买时留下的“联系方式”可以查出所有相关的交易记录</a>
                    </li>
                </ol>
            </div>
        </div>
        <div class="row mt20">
            <div class="col-xs-6">
                <a class="btn btn-primary btn-block back" href="javascript:;" onclick="gotoIndex()">
                    <span class="glyphicon glyphicon-refresh"></span>
                    &nbsp;<span class="t">返回重选</span>
                </a>
            </div>
            <div class="col-xs-6" >
                <a class="btn btn-success btn-block submit" href="javascript:;" onclick="gotoAlipayMent()">
                    <span class="glyphicon glyphicon-shopping-cart"></span>
                    &nbsp;<span class="t">立即付款</span>
                </a>
            </div>
        </div>
    </div>
</div>
<script>

    function gotoIndex() {
        var userGame = $("input[name='userGame']").val();
        window.location.href = "/log/index";
    }

    function gotoAlipayMent() {
        $('.submit').addClass('hideData');
        $('.col-xs-6').css('width', '100%');
        //alert("我知道你很急,但是急不来，一步一步的来!")
        var quantity = $("input[name='quantity']").val();
        var userGame = $("input[name='userGame']").val();
        var phone = $("input[name='phone']").val();
        var ids = $("input[name='ids']").val();
        var orderId = $("input[name='orderId']").val();
        var serviceRange = $("input[name='serviceRange']").val();

        $.ajax({
            type : 'POST',
            url : "wxpay/generateWxOrder",
            data: "orderId="+orderId+"&serviceRange="+serviceRange+"&ids="+ids+"&phone="+phone+"&userGame="+userGame+"&quantity="+quantity,
            async : true,
            success : function(data) {
                if (data == "0") {
                    alert("系统异常请稍后重试!");
                } else {
                    var path = "images/"+data;
                    $('#imaPath').attr('src', path);
                    $('#wxPayCode').show();
                    $("input[name='refresh']").val(0);

                }
            },
            error : function(data) {
            },
            dataType : "html"
        });

    }


    setInterval(function() {
        var orderId = $("input[name='orderId']").val();
        var refresh = $("input[name='refresh']").val();
        //alert(refresh);
        if (refresh == '0') {
            // 做到了这里
            $.post('wxpay/queryOrderStatus', {
                    orderId: orderId,
                },
                function (ret) {
                    if (ret == 0) {
                        window.location.href = "/log/gotoWxSuccess";
                    } else {

                    }
                },'json');
        }
    }, 3000);

    $(function() {
        $('.submit').click(function() {
            setTimeout(translation, 2000);
        });
    });
    function translation() {
        $('.submit span.t').text('查看结果');
        $('.back span.t').text('继续购买');
        //$('.submit').attr('href', '/orderquery?orderid=1462A1604755E70F');
    }

    function queryOrderStatus() {
        var orderId = $("input[name='orderId']").val();
       alert("查询订单="+orderId);
    }

</script>
</body>
</html>

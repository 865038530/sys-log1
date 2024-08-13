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
    <input type="hidden" name="orderId" value="${orderId}">
    <div class="container">
        <div class="row">
            <div class="col-xs-12 info">
                <ul>
                    <li>
                        订单编号：<strong>${orderId}</strong>
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
       <div style="text-align: center;">
            <img src="assets/images/zfsuss.jpg" >
       </div>
        <div class="row mt20">
            <div class="col-xs-6" style="width: 100%;">
                <a class="btn btn-primary btn-block back" href="javascript:;" onclick="gotoIndex()">
                    <span class="glyphicon glyphicon-refresh"></span>
                    &nbsp;<span class="t">返回首页</span>
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


</script>
</body>
</html>

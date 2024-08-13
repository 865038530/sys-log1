layui.use('layer',function(){
    var layer=layui.layer;
    window.popupTips=function(is_user_popup){
        $.get('/report/usertips?is_user_popup='+is_user_popup,function(ret){
            layer.open({
                title:'温馨提示',
                btn:['是的，我已了解'],
                maxWidth:'550',
                btnAlign: 'c',
                content:ret
            });
        });
    };

    $('body').on('click','#good_discount',function(){
        layer.open({
            title:'批发价格',
            area:'300px',
            btnAlign: 'c',
            content:$('#good_discount_list').html()
        });
    });

    $('body').on('click','.is_good_detail',function(){
        layer.open({
            title:'商品详情',
            area:'320px',
            btnAlign: 'c',
            content:$('.good_detail_content').html()
        });
    });

    $('body').on('click','.cardwhy',function(){
        $.get('/report/cardwhy',function(ret){
            layer.open({
                title:'点卡注意事项',
                maxWidth:'550',
                btn:['我已明白'],
                btnAlign: 'c',
                content:ret
            });
        });
    });

    window.getPwdforbuy=function(gid){
        layer.open({
            type:2,
            title:'购买验证',
            area:['350px','240px'],
            btnAlign: 'c',
            closeBtn: 0,
            content:'/report/pwdforbuy?gid='+gid
        });
    };

    window.contactus=function(qq,sitename,siteurl){
        layer.open({
            type:2,
            title:'联系客服',
            area:['550px','360px'],
            btnAlign: 'c',
            content:'/report/contactus?qq='+qq+'&sitename='+sitename+'&siteurl='+siteurl
        });
    };

    if(is_user_popup){
        popupTips(is_user_popup);
    }

    if(is_pwdforbuy){
        getPwdforbuy(goodid);
    }
});
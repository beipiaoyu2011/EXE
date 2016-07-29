/**
 * Created by wz on 2015/8/13.
 */
$(function(){
    var myDialog=function(){
        var con='<div class="dialog">'+
            '<div class="mod-dialog-bg"></div>'+
            '<div class="mod-dialog">'+
            '<div class="dialog-nav">'+
            '<span class="dialog-title">提示</span>'+
            '<a class="dialog-close" href="javascript:;"></a>'+
            '</div>'+
            '<div class="dialog-main">'+
            '<div class="dialog-content">确定删除？</div>'+
            '<div class="dialog-console">'+
            '<a class="console-btn-confirm" href="javascript:;">确定</a>'+
            '<a class="console-btn-cancel" href="javascript:;">取消</a>'+
            '</div>'+
            '</div>'+
            '</div>'+
            '</div>';
        if($('.dialog').length==0){
            $('body').append(con);
            var left=($(window).width()-300)/ 2,//$(window).width() 可视化窗口宽度大小
                top=($(window).height()-150)/2;//$(window).width() 可视化窗口高度大小
            $('.mod-dialog').css({
                left:left,
                top:top
            })
        }
    }

    $('#btn').click(function(){
        myDialog();
        $('.dialog').fadeIn(300);

    })
    $(document).on('click','.dialog-close',function(){
        $('.dialog').fadeOut(300);
    })
    $(document).on('click','.console-btn-confirm',function(){
        $('.dialog').fadeOut(300);
    })
    $(document).on('click','.console-btn-cancel',function(){
        $('.dialog').fadeOut(300);
    })

    //返回顶部
    var goBackTop=function(){
        var str='<div class="goTop"></div>'
        $('body').append(str)
        $(window).on('scroll',function(){
            var $this=$(this);
            if($this.scrollTop()>=$(this).height()/2){
                $('.goTop').fadeIn();
            }else if($this.scrollTop()<$(this).height()/2){
                $('.goTop').fadeOut();
            }
        })
        $(document).on('click','.goTop',function(){
            //$(window).scrollTop(0);//直接返回顶部
            $('body,html').animate({scrollTop:0},500);//带有动画的返回顶部
        })


    }
    goBackTop()
})
//jquery index 1.0
//create by zengrong (zrongs@vip.qq.com)

$(document).ready(function (e) {
    var dom = $(".banner-cont");
    dom.sly({
        itemNav: "forceCentered",
        easing: "easeOutExpo",
        cycleBy: 'items',
        pagesBar: ".pages",
        cycleInterval: 2000,
        horizontal: 1
    });

    $(".tab ul li").click(function () {
        $(this).addClass("active").siblings().removeClass('active');
        $(".detail").eq($(".tab ul li").index(this)).addClass("on").siblings().removeClass('on');
    })

    $(".venue-list ul li").mouseover(function(){
        $(this).addClass("active").siblings().removeClass('active');
    })

    $('.union-cont').sly({
        itemNav: "basic",
        easing: "easeOutExpo",
        pagesBar: ".u-nav",
        pageBuilder: function (dom) {
            return "<span></span>";
        },
        horizontal: 1
    });

    $('.venue-list').sly({
        horizontal: 1,
        itemNav: 'basic',
        cycleInterval: 2000,
        startAt: 1
    });

});
function renderMyList() {
    $('.container .list li div.img').mouseover(function () {
        $(this).children('.title').stop(1, 0).fadeOut(1000);
        $(this).children('.detail').stop(1, 0).fadeIn(500);
    });
    $('.container .list li div.img').mouseout(function () {
        $(this).children('.title').stop(1, 0).fadeIn(500);
        $(this).children('.detail').stop(1, 0).fadeOut(1000);
    });

    $('.container .detail li div.img').mouseover(function () {
        $(this).children('.detail').stop(1, 0).animate({height:"185px"});
        $(this).find('.mask').stop(1, 0).fadeIn(1000);
        $(this).find('p').stop(1, 0).css("height","72px");
        // $(this).find('h2').stop(1, 0).css("color","#fff");
        // $(this).find('p').stop(1, 0).css("color","#fff");
        $(this).find('i').stop(1, 0).css("display","block");
    });
    $('.container .detail li div.img').mouseout(function () {
        $(this).children('.detail').stop(1, 0).animate({height:"84px"});
        $(this).find('.mask').stop(1, 0).fadeOut(1000);
        $(this).find('p').stop(1, 0).css("height","24px");
        // $(this).find('h2').stop(1, 0).css("color","#333");
        // $(this).find('p').stop(1, 0).css("color","#666");
        $(this).find('i').stop(1, 0).css("display","none");
    });

    $('.container .venue-list .lists ul li div.img').mouseover(function () {
        $(this).children('.title').stop(1, 0).fadeOut(1000);
        $(this).children('.detail').stop(1, 0).fadeIn(500);
    });
    $('.container .venue-list .lists ul li div.img').mouseout(function () {
        $(this).children('.title').stop(1, 0).fadeIn(500);
        $(this).children('.detail').stop(1, 0).fadeOut(1000);
    });
}

$(document).ready(function(e) {
    // $('.container .list li div.img').mouseover(function () {
    //     $(this).children('.title').stop(1, 0).fadeOut(1000);
    //     $(this).children('.detail').stop(1, 0).fadeIn(500);
    // });
    // $('.container .list li div.img').mouseout(function () {
    //     $(this).children('.title').stop(1, 0).fadeIn(500);
    //     $(this).children('.detail').stop(1, 0).fadeOut(1000);
    // });
    //
    // $('.container .detail li div.img').mouseover(function () {
    //     $(this).children('.detail').stop(1, 0).animate({height:"185px"});
    //     $(this).find('.mask').stop(1, 0).fadeIn(1000);
    //     $(this).find('p').stop(1, 0).css("height","72px");
    //     $(this).find('h2').stop(1, 0).css("color","#fff");
    //     $(this).find('p').stop(1, 0).css("color","#fff");
    //     $(this).find('i').stop(1, 0).css("display","block");
    // });
    // $('.container .detail li div.img').mouseout(function () {
    //     $(this).children('.detail').stop(1, 0).animate({height:"84px"});
    //     $(this).find('.mask').stop(1, 0).fadeOut(1000);
    //     $(this).find('p').stop(1, 0).css("height","24px");
    //     $(this).find('h2').stop(1, 0).css("color","#333");
    //     $(this).find('p').stop(1, 0).css("color","#666");
    //     $(this).find('i').stop(1, 0).css("display","none");
    // });
    //
    // $('.container .venue-list .lists ul li div.img').mouseover(function () {
    //     $(this).children('.title').stop(1, 0).fadeOut(1000);
    //     $(this).children('.detail').stop(1, 0).fadeIn(500);
    // });
    // $('.container .venue-list .lists ul li div.img').mouseout(function () {
    //     $(this).children('.title').stop(1, 0).fadeIn(500);
    //     $(this).children('.detail').stop(1, 0).fadeOut(1000);
    // });




    $('.lists').sly({
        itemNav: "basic",
        easing: "easeOutExpo",
        prevPage: ".left-arrow",
        nextPage: ".right-arrow",
        horizontal: 1,
        touchDragging: 1,
        scrollBy: 1

    });
    $('.index-banner-content').sly({
        itemNav: "forceCentered",
        easing: "easeOutExpo",
        cycleBy: 'items',
        pagesBar: ".pages",
        cycleInterval: 2000,
        horizontal: 1

    });
});
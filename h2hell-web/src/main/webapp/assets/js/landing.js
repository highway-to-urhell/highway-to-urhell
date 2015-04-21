$( document ).ready(function() {
    
    // Slimscroll
    $('.slimscroll').slimscroll({
        allowPageScroll: true
    });
    
    // Wow
    new WOW().init();
    
    // Smooth scroll
    $('a[href^="#"]').on('click',function (e) {
        e.preventDefault();

        var target = this.hash;
        var $target = $(target);
        var scrollTo = $target.offset().top

        $('html, body').stop().animate({
            'scrollTop': scrollTo
        }, 1000, 'easeInOutExpo');
    });
    
    $(window).scroll(function() {    
        var scroll = $(window).scrollTop();
        
        if (scroll >= 60) {
            $(".navbar").addClass("whiteHeader");
        } else {
            $(".navbar").removeClass("whiteHeader");
        }
    });
    
    // Tabs
    [].slice.call( document.querySelectorAll( '.tabs' ) ).forEach( function( el ) {
        new CBPFWTabs( el );
    });
    
});
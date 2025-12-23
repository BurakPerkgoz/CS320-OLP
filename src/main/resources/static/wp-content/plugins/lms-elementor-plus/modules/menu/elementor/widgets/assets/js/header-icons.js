(function ($) {

    var dtHeaderIconsWidgetHandler = function($scope, $) {

        var $search = $scope.find("div.search-overlay");

        // Disable cloning logic inside the sticky clone
        if ($search.length) {
            if ($search.parents('elementor-element').hasClass('sticky-header-active')) {
                $search.find(".wdt-search-form-container").remove();
            }
        }

        /** -----------------------------------------------------
         * CLOSE SEARCH FORM
         * ----------------------------------------------------- */
        $(document)
            .off('click.wdtSearchClose')
            .on('click.wdtSearchClose', '.wdt-search-form-close', function(e) {
                e.preventDefault();
                $(this)
                    .closest('.wdt-search-form-container')
                    .removeClass('show')
                    .hide();
            });


        /** -----------------------------------------------------
         * OPEN SEARCH FORM (ALL CASES)
         * Normal + Sticky + Fallback
         * ----------------------------------------------------- */
        $(document)
            .off('click.wdtSearchOpen')
            .on('click.wdtSearchOpen', '.wdt-search-icon', function(e) {

                e.preventDefault();

                var $searchItem = null;

                // 1. Try same menu item first (best case)
                $searchItem = $(this)
                    .closest('.wdt-header-icons-list-item')
                    .find('.wdt-search-form-container');

                // 2. Try sticky header wrapper
                if (!$searchItem.length) {
                    $searchItem = $(this)
                        .closest('.sticky-header-active')
                        .find('.wdt-search-form-container');
                }

                // 3. Visible forms (global)
                if (!$searchItem.length) {
                    $searchItem = $('.wdt-search-form-container:visible').first();
                }

                // 4. Last fallback: ANY form
                if (!$searchItem.length) {
                    $searchItem = $('.wdt-search-form-container').first();
                }

                if ($searchItem.length) {

                    // close all others
                    $('.wdt-search-form-container')
                        .not($searchItem)
                        .removeClass('show')
                        .hide();

                    // 🔥 Always show the clicked one
                    $searchItem
                        .addClass('show')
                        .show();

                    console.debug('Search opened → ', $searchItem.get(0));
                }
            });


        /** -----------------------------------------------------
         * STICKY HEADER SEARCH ONLY
         * (Keeps sticky section isolated)
         * ----------------------------------------------------- */
        $(document)
            .off('click.wdtStickySearch')
            .on('click.wdtStickySearch', '.sticky-header-active .wdt-search-icon', function(e) {

                e.preventDefault();

                var $searchItem = $(this)
                    .closest('.sticky-header-active')
                    .find('.wdt-search-form-container');

                if ($searchItem.length) {

                    $('.sticky-header-active .wdt-search-form-container')
                        .not($searchItem)
                        .removeClass('show')
                        .hide();

                    $searchItem
                        .addClass('show')
                        .show();
                }
            });



        /** -----------------------------------------------------
         * CART + WISHLIST (unchanged)
         * ----------------------------------------------------- */
        $scope.find('.wdt-shop-menu-cart-icon').on('click', function(e) {
            if($('.wdt-shop-cart-widget').hasClass('activate-sidebar-widget')) {
                $('.wdt-shop-cart-widget').addClass('wdt-shop-cart-widget-active');
                $('.wdt-shop-cart-widget-overlay').addClass('wdt-shop-cart-widget-active');
                var winHeight = $(window).height();
                var headerHeight = $('.wdt-shop-cart-widget-header').height();
                var footerHeight = $('.woocommerce-mini-cart-footer').height();
                var height = parseInt((winHeight - headerHeight - footerHeight), 10);
                $('.wdt-shop-cart-widget-content')
                    .height(height)
                    .niceScroll({
                        cursorcolor:"#000",
                        cursorwidth: "5px",
                        background:"rgba(20,20,20,0.3)",
                        cursorborder:"none"
                    });
                e.preventDefault();
            }
        });


    };


    /** Elementor Initialization */
    $(window).on('elementor/frontend/init', function() {
        elementorFrontend.hooks.addAction(
            'frontend/element_ready/wdt-header-icons.default',
            dtHeaderIconsWidgetHandler
        );
    });

})(jQuery);

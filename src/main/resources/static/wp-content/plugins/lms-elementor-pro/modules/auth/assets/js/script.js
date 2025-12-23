(function ($) {

    var lmselementorAuthValidation = {

        init : function() {
            jQuery( 'body' ).delegate( '.lmselementor-pro-login-link', 'click', function(e){

                jQuery.ajax({
                    type: "POST",
                    url: lmselementor_urls.ajaxurl,
                    data:
                    {
                        action: 'lmselementor_pro_show_login_form_popup',
                        nonce: lmselementor_urls.nonce
                    },
                    success: function (response) {
    
                        jQuery('body').find('.lmselementor-pro-login-form-container').remove();
                        jQuery('body').find('.lmselementor-pro-login-form-overlay').remove();
                        jQuery('body').append(response);
    
                        jQuery('#user_login').focus();

                        lmselementorAuthValidation.addPlaceholder();
    
                    }
                });
    
                e.preventDefault();
    
            });
    
            jQuery( 'body' ).delegate( '.lmselementor-pro-login-form-overlay', 'click', function(e){
    
                jQuery('body').find('.lmselementor-pro-login-form-container').fadeOut();
                jQuery('body').find('.lmselementor-pro-login-form-overlay').fadeOut();
    
                e.preventDefault;
    
            });

        },

        addPlaceholder : function() {

            // Login Form Scripts
            $('#loginform input[id="user_login"]').attr('placeholder', 'Username');
            $('#loginform input[id="user_pass"]').attr('placeholder', 'Password');
            
            $('#loginform label[for="user_login"]').contents().filter(function() {
                return this.nodeType === 3;
            }).remove();
            $('#loginform label[for="user_pass"]').contents().filter(function() {
                return this.nodeType === 3;
            }).remove();
            
            $('input[type="checkbox"]').click(function() {
                $(this+':checked').parent('label').css("background-position","0px -20px");
                $(this).not(':checked').parent('label').css("background-position","0px 0px");
            });
        }

    }

    "use strict";
    $(document).ready(function () {   
        lmselementorAuthValidation.init();

        // Custom register page
        if( ($('#signup-content').length) || ($('#signup-content').length) > 1 ) {
            $('body').addClass('wdt-custom-auth-form');
            $('.wrapper').addClass('wdt-custom-auth-form');
        }
    });

})(jQuery);
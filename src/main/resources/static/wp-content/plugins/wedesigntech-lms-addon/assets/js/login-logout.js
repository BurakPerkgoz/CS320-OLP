var dtLMSLoginLogout = {

	dtInit : function() {


	}

};

jQuery(document).ready(function() {

	if(!lmsfrontendobject.elementorPreviewMode) {
		dtLMSLoginLogout.dtInit();
	}

});

( function( $ ) {

	var dtLMSLoginLogoutJs = function($scope, $){
		dtLMSLoginLogout.dtInit();
	};

    $(window).on('elementor/frontend/init', function(){
		if(lmsfrontendobject.elementorPreviewMode) {
			elementorFrontend.hooks.addAction('frontend/element_ready/dtlms-widget-default-login-logout-links.default', dtLMSLoginLogoutJs);
		}
	});

} )( jQuery );
jQuery(document).ready(function() {

    if(jQuery('.dtwb-classroom-item-access-code').length) {
        jQuery('body').on('click', '.dtwb-classroom-item-access-code', function() {

            var temp = jQuery('<input>');
            jQuery('body').append(temp);
            temp.val(jQuery(this).find('span').html()).select();
            document.execCommand('copy');
            temp.remove();

            jQuery('<span class="dtwb-classroom-item-access-code-copied">'+dtwebinarfrontendobject.textCopied+'</span>').insertAfter(jQuery(this).find('span'));
            setTimeout(function() {
                jQuery('.dtwb-classroom-item-access-code-copied').remove();
            }, 1200);

        });
    }

});
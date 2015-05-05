/*
 * (c) 2015 Mike Chaberski <mike10004@users.noreply.github.com>
 *
 * Distributed under MIT License.
 */

function FormResponseHandler(formSelector, responseSpanSelector, postUrl, responseMessageCreator) {
    var form = formSelector;
    var response = responseSpanSelector;
    var statusIndicatorElements = $('.status-indicator');
    function setStatusIndicatorShowing(showing, elements) {
        var visibilityValue = showing ? 'visible' : 'hidden';
        elements = elements || statusIndicatorElements;
        elements.css({
            'visibility': visibilityValue
        });
    }
    this.activate = function() {
        var formControls = form.find('input');
        form.on('submit', function(event) {
            event.preventDefault();
            var formData = form.serialize();
            formControls.attr('disabled', 'disabled');
            setStatusIndicatorShowing(true);
            $.ajax({
                url: postUrl,
                method: 'POST',
                data: formData,
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                success: function(data, textStatus, xhr) {
                    var message = responseMessageCreator(data);
                    response.text(message);
                },
                error: function(xhr, status, errorThrown) {
                    console.log('error', xhr.status, errorThrown, xhr.responseJSON);
                    var message;
                    if (xhr.responseJSON) {
                        message = (xhr.responseJSON.message);
                    } else {
                        message = "Error code " + xhr.status + " " + errorThrown;
                    }
                    response.text(message);
                },
                complete: function() {
                    setStatusIndicatorShowing(false);
                }
            });
        });
        
    };
}
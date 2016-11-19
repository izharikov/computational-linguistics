define(['knockout', 'jquery', 'postbox'], function(ko, $){
    function SuccessMessageViewModel() {
        var $successMessage = $("#successMessage");
        var self = this;
        self.message = ko.observable('');

        ko.postbox.subscribe('successMessage', function (message) {
            self.message(message);
            $successMessage.modal('show');
        });
    };

    return SuccessMessageViewModel;
});
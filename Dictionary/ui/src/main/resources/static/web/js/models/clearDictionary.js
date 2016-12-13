define(['knockout', 'jquery', 'utils/RestClient'], function (ko, $, RestClient) {
    function ClearDictViewModel() {
        var self = this;
        self.clearDictionary = function () {
            RestClient.send('/rest/clear-all', RestClient.GET, {}, function () {
                location.reload();
            }, function () {
            });
        }
    }

    return ClearDictViewModel;
});
define(['jquery', 'knockout', 'postbox'], function (jquery, ko) {
    function AnnotateView() {
        var self = this;
        self.mode = ko.observable('text');

        self.showAnnotated = function () {
            self.mode('text');
        };

        self.showStatistic = function () {
            self.mode('statistic');
        };

        self.inputText = ko.observable('');
    }

    return AnnotateView;
});
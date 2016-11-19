define(['knockout', 'postbox'], function (ko) {
    function HeaderViewModel() {
        var self = this;

        self.searchTerm = ko.observable('');

        self.doSearch = function () {
            console.warn(self.searchTerm())
            ko.postbox.publish('reload',{page : 0, term : self.searchTerm()})
        }
    }

    return HeaderViewModel;
});
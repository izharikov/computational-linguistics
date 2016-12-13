define(['knockout', 'jquery', 'utils/DictionaryLoader', 'postbox'], function (ko, $, DictionaryLoader) {
    function AppViewModel() {
        var self = this;
        self.dictionary = ko.observableArray([]);
        self.empty = ko.observable(false);
        var allData = {};

        DictionaryLoader.loadDictionary(/*{term : 'done'},*/ {},initDictionary);

        function initDictionary(data) {
            if (data) {
                self.dictionary(data.content);
                var pageData = {pageCount: data.totalPages, pageNo: data.number + 1};
                ko.postbox.publish('initPagination', pageData);
            }
            if ( data.numberOfElements == 0) {
                self.empty(true);
            } else {
                self.empty(false);
            }
        }

        ko.postbox.subscribe('reload', function (data) {
            if (data) {
                allData = data;
            } else {
                data = {}
            }
            DictionaryLoader.loadDictionary(data, initDictionary)
            if (!data.pos) {
                ko.postbox.publish('emptySelectedPos');
            }
        });

        ko.postbox.subscribe('goToPage', function (data) {
            $.extend(allData, data);
            DictionaryLoader.loadDictionary(data, initDictionary)
        });


        self.menu = ko.observableArray([{text: "Update", action: self.updateItem}]);

        self.updateItem = function (element, event) {
            console.warn(element);
        };


        self.sort = function (sortBy) {
            var data = {};
            if ( allData.term){
                data.term = allData.term;
            }
            var prevSort = allData.sortBy;
            if (!prevSort) {
                data.sortBy = sortBy + "DESC";
            } else {
                data.sortBy = sortBy + (allData.sortBy.endsWith("ASC") ? "DESC" : "ASC");
            }
            data.pageNo = 0;
            ko.postbox.publish('goToPage', data);
        }

    };
    return AppViewModel;
});
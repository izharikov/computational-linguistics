define(['knockout', 'jquery', 'postbox'], function (ko, $) {
    function PaginationViewModel() {
        var self = this;
        var pageNo = 1;
        self.pageCount = ko.observable(0);

        self.pagination = ko.observableArray([]);

        self.goToPage = function (element, event) {
            console.log(element);
            var reload = true;
            if (element.id == "prev"){
                if ( pageNo == 1){
                    reload = false;
                }
                else {
                    pageNo--;
                }
            } else if (element.id == 'next'){
                if (pageNo == self.pageCount()){
                    reload = false;
                } else {
                    pageNo++;
                }
            } else {
                if ( pageNo == element.id){
                    reload = false;
                }
                else {
                    pageNo = element.id;
                }
            }
            if ( reload) {
                ko.postbox.publish('goToPage', {page: pageNo - 1})
            }
        }

        self.initPagination = function (pageCount, currentPageNo) {
            pageNo = currentPageNo;
            self.pageCount(pageCount);
            self.pagination([]);
            self.pagination.push({text: '<<', id: 'prev', active: false});
            if ( pageCount > 10) {
                if (currentPageNo < 4) {
                    for (var i = 1; i < currentPageNo; i++) {
                        self.pagination.push({text: i, id: i, active: false});
                    }
                    self.pagination.push({text: currentPageNo, id: currentPageNo, active: true});
                    for (var i = currentPageNo + 1; i < 4; i++) {
                        self.pagination.push({text: i, id: i, active: false});
                    }
                    self.pagination.push({text: '..', id: -1, active: false});
                    for (var i = pageCount - 2; i < pageCount + 1; i++) {
                        self.pagination.push({text: i, id: i, active: false});
                    }
                } else if (currentPageNo >= 4 && currentPageNo + 3 < pageCount) {
                    for (var i = 1; i < 4; i++) {
                        self.pagination.push({text: i, id: i, active: false});
                    }
                    self.pagination.push({text: '..', id: -1, active: false});
                    self.pagination.push({text: currentPageNo, id: currentPageNo, active: true});
                    self.pagination.push({text: '..', id: -1, active: false});
                    for (var i = pageCount - 2; i < pageCount + 1; i++) {
                        self.pagination.push({text: i, id: i, active: false});
                    }
                } else {
                    for (var i = 1; i < 4; i++) {
                        self.pagination.push({text: i, id: i, active: false});
                    }
                    self.pagination.push({text: '..', id: -1, active: false});
                    for (var i = pageCount - 2; i < currentPageNo; i++) {
                        self.pagination.push({text: i, id: i, active: false});
                    }
                    self.pagination.push({text: currentPageNo, id: currentPageNo, active: true});
                    for (var i = currentPageNo + 1; i < pageCount + 1; i++) {
                        self.pagination.push({text: i, id: i, active: false});
                    }
                }
            }
            else{
                for (var i = 1; i < currentPageNo; i++) {
                    self.pagination.push({text: i, id: i, active: false});
                }
                self.pagination.push({text: currentPageNo, id: currentPageNo, active: true});
                for (var i = currentPageNo + 1; i < pageCount + 1; i++) {
                    self.pagination.push({text: i, id: i, active: false});
                }
            }
            self.pagination.push({text: '>>', id: 'next', active: false});
        }

        ko.postbox.subscribe('initPagination', function (data) {
            self.initPagination(data.pageCount, data.pageNo);
        })
    };
    return PaginationViewModel;
});
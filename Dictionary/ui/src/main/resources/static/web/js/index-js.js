function AppViewModel() {
    var SORT_WORD_ASC = "wordASC";
    var SORT_WORD_DESC = "wordDESC";
    var SORT_COUNT_ASC = "countASC";
    var SORT_COUNT_DESC = "countDESC";
    var self = this;

    self.posTags = ko.observableArray([]);

    RestClient('/rest/pos/all', GET, {},
        function (data) {
            self.posTags(data);
        },
        function (data) {

        }
    );

    self.tableBody = ko.observableArray([])
    self.pageCount = ko.observable(0)

    self.currentPage = ko.observable(1)
    self.searchTerm = ko.observable("")

    self.previousPage = ko.observable(null)
    self.searchResultCount = ko.observable(0)

    self.filters = ko.observableArray([])
    self.wordAsc = ko.observable(true)
    self.countAsc = ko.observable(true)
    self.sortBy = ko.observable('')
    self.errorMessage = ko.observable('')
    /**
     * from 1 to self.pageCount()
     */
    self.pageNo = ko.observable(1)

    self.modifiedId = ko.observable(-1)
    self.modifiedWord = ko.observable('')
    self.modifiedCount = ko.observable(-1)

    self.goToPage = function (element, evt) {
        if (Number.isInteger(element.text)) {
            self.pageNo(element.id + 1)
            self.loadPage(element.id)
            self.resetPagination()
        } else if (element.text === '>>' && self.pageNo() < self.pageCount()) {
            self.pageNo(self.pageNo() + 1)
            self.loadPage(self.pageNo() - 1)
            self.resetPagination()
        } else if (element.text === '<<' && self.pageNo() > 1) {
            self.pageNo(self.pageNo() - 1)
            self.loadPage(self.pageNo() - 1)
            self.resetPagination()
        }
    }


    self.toPage = function () {
        if (isNaN(self.pageNo()) || self.pageNo() < 1 || self.pageNo() > self.pageCount()) {
            self.pageNo(1)
        }
        self.loadPage(self.pageNo() - 1)
        self.resetPagination()
    }

    /**
     *
     * @param activeNo : from 1 to self.pageCount()
     */
    self.resetPagination = function () {
        self.previousPage().removeClass('active')
        var activeNo = self.pageNo();
        if (activeNo > 0 && activeNo < self.pageCount() + 1) {
            var curActive = $('#' + (activeNo - 1))
            var centerNone = $('#none1');
            if (activeNo < 4 || activeNo > self.pageCount() - 3) {
                centerNone.css('display', 'block')
            }
            else {
                centerNone.css('display', 'none')
            }
            var id = parseInt(self.previousPage().attr('id'));
            if (id > 2 && id < self.pageCount() - 3) {
                self.previousPage().css('display', 'none')
            }
            curActive.css('display', 'block')
            curActive.addClass('active')
            self.previousPage(curActive)
        }
    }

    self.loadPage = function (pageNo, searchTerm, sortBy) {
        if ( !pageNo){
            pageNo = 0
        }
        if (!searchTerm) {
            searchTerm = self.searchTerm();
        }
        if (!sortBy) {
            sortBy = self.sortBy();
        }
        if (pageNo > -1 && pageNo < self.pageCount()) {
            RestClient('/rest/dictionary/results', GET, {page: pageNo, term: searchTerm, sortBy: sortBy},
                function (data) {
                    self.pageCount(data.pageCount)
                    initTableBody(data.result)
                },
                function (data) {
                    console.log(data);
                }
            );
        }
    }

    function initTableBody(words) {
        self.tableBody([])
        for (var i = 0; i < words.length; i++) {
            self.tableBody.push({
                id: words[i].id,
                count: words[i].wordCount,
                word: words[i].word,
                posTags : words[i].posTags
            })
        }
    }

    function initPagination(pageCount) {
        if ( !pageCount){
            pageCount = self.pageCount();
        }
        else {
            self.pageCount(pageCount)
        }
        self.pagination([])
        if (pageCount > 7) {
            var nA = ' pointer-events: none; cursor: default;';
            self.pagination.push({text: '<<', style: '', class: '', id: 'previous'})
            self.pagination.push({text: 1, style: '', class: 'active', id: 0})
            self.previousPage($('.active'))
            var leftBorder = 3
            for (var i = 1; i < leftBorder; i++) {
                self.pagination.push({
                    text: i + 1,
                    style: '',
                    class: '',
                    id: i
                })
            }
            self.pagination.push({text: '..', style: nA, class: '', id: 'none0'})
            for (var i = 3; i < pageCount - 3; i++) {
                self.pagination.push({
                    text: i + 1,
                    style: 'display : none;',
                    class: '',
                    id: i
                })
            }
            self.pagination.push({text: '..', style: nA, class: '', id: 'none1'})
            self.pagination.push({text: '..', style: nA, class: '', id: 'none2'})
            for (var i = 0; i < 3; i++) {
                self.pagination.push({
                    text: self.pageCount() - 2 + i,
                    style: '',
                    class: '',
                    id: self.pageCount() - 3 + i
                })
            }
            self.pagination.push({
                text: '>>', style: '', class: '', id: 'next'
            })
        }
        else if (pageCount) {
            self.pagination.push({text: '<<', style: '', class: '', id: 'previous'})
            for (var i = 0; i < pageCount; i++) {
                self.pagination.push({
                    text: i + 1,
                    style: '',
                    class: '',
                    id: i
                })
            }
            self.pagination.push({text: '>>', style: '', class: '', id: 'next'})
            var first = $('#0')
            first.addClass('active')
            self.previousPage(first)
        }
    }

    RestClient('/rest/dictionary/all-words', GET, {page: 0},
        function (data) {
            initTableBody(data.words)
            initPagination(data.pageCount)
            $('.canvas-holder').removeClass("show").addClass("hide");
        },
        function (data) {
            console.log('error!')
            $('.canvas-holder').removeClass("show").addClass("hide");
        });

    self.pagination = ko.observableArray([])
    self.empty = function () {
        self.tableBody([])
    }

    self.doSearch = function () {
        if (self.searchTerm()) {
            RestClient('/rest/dictionary/search', GET, {term: self.searchTerm(), page: 0},
                function (data) {
                    if (data) {
                        if (data.pageCount) {
                            self.pageCount(data.pageCount)
                            self.pageNo(1)
                            self.searchResultCount(data.allCount)
                            initTableBody(data.searchResult)
                            initPagination(data.pageCount)
                        }
                    }
                },
                function (data) {
                    console.log("Search error!!!")
                })
        }
    }

    self.paginationVisible = ko.observable(false)

    self.pageCount.subscribe(function (newValue) {
        if (newValue > 1) {
            self.paginationVisible(true)
        }
        else {
            self.paginationVisible(false)
        }

    })


    self.sortByWord = function () {
        var sortBy = self.wordAsc() ? SORT_WORD_ASC : SORT_WORD_DESC;
        self.sortBy(sortBy)
        self.loadPage(0, self.searchTerm(), sortBy);
        initPagination(self.pageCount())
        self.wordAsc(!self.wordAsc())
    }


    self.sortByCount = function () {
        var sortBy = self.countAsc() ? SORT_COUNT_ASC : SORT_COUNT_DESC;
        self.sortBy(sortBy)
        self.loadPage(0, self.searchTerm(), sortBy);
        initPagination(self.pageCount())
        self.countAsc(!self.countAsc())
    }

    self.openModifyModal = function (element) {
        self.errorMessage('')
        self.modifiedId(element.id)
        self.modifiedWord(element.word)
        self.modifiedCount(element.count)
        $.mobile.changePage("#updateDialog", {role: "dialog"});
    }

    self.openDeleteModal = function(element){
        self.errorMessage('')
        self.modifiedId(element.id)
        self.modifiedWord(element.word)
        // $.mobile.changePage("#deleteDialog", {role : "dialog"})
        $('#deleteDialog').modal('show');
    }

    self.updateItem = function () {
        if (self.modifiedWord() && self.modifiedCount() > 0) {
            RestClient('/rest/dictionary/update', POST,
                {
                    id: self.modifiedId(),
                    word: self.modifiedWord(),
                    count: self.modifiedCount()
                }, function (data) {
                    if (data) {
                        self.loadPage()
                        initPagination()
                        $('#updateDialog').dialog('close')
                        $.mobile.changePage("#success", {role: "dialog"});
                    }
                    else{
                        self.errorMessage('Word with the same name already exist!');
                    }
                }, function () {
                    self.errorMessage('Server Error!')
                })
        }
    }

    self.deleteItem = function(){
        if (self.modifiedId()) {
            RestClient('/rest/dictionary/remove', POST,
                {
                    id: self.modifiedId()
                }, function (data) {
                    if (data) {
                        self.loadPage()
                        initPagination()
                        $('#deleteDialog').dialog('close')
                        $.mobile.changePage("#success", {role: "dialog"});
                    }
                    else{
                        self.errorMessage('Unable to remove item!');
                    }
                }, function () {
                    self.errorMessage('Server Error!')
                })
        }
    }

    self.closeDeleteModal = function(){
        $("#deleteDialog").dialog('close')
    }
    
    self.openAddDialog = function(){
        self.errorMessage('')
        self.modifiedCount('');
        self.modifiedWord('');
        $("#addDialog").modal("show");
    }


    self.addItem = function(){
        if (self.modifiedWord() && self.modifiedCount() > 0) {
            RestClient('/rest/dictionary/add', POST,
                {
                    word: self.modifiedWord(),
                    count: self.modifiedCount()
                }, function (data) {
                    if (data) {
                        self.loadPage()
                        initPagination()
                        $('#addDialog').dialog('close')
                        $.mobile.changePage("#success", {role: "dialog"});
                    }
                    else{
                        self.errorMessage('Word with the same name already exist!');
                    }
                }, function () {
                    self.errorMessage('Server Error!')
                })
        }
    }
}
ko.applyBindings(new AppViewModel());
define(['knockout', 'utils/GroupUtils', 'utils/DictionaryLoader', 'postbox'], function (ko, GroupUtils, DictionaryLoader) {
    function GroupDialog(word, modify) {
        var self = this;
        self.modify = ko.observable(modify);
        self.data = ko.observable(word);
        self.word = ko.observable(word.word);
        self.initial = ko.observable(word.initial);
        self.forms = ko.observableArray([]);
        self.group = ko.observable(word.groupId);
        self.ids = [];
        self.posTags = ko.observableArray(word.posTags);
        self.equalOnly = ko.observable(true);
        self.selectWords = ko.observableArray([]);
        self.initialWord = ko.observable(null);

        self.searchTerm = ko.observable();
        self.currentInitial = ko.observable(word.initial ? word.word : '');

        self.doSearch = function () {
            var term = self.searchTerm() ? self.searchTerm() : self.word();
            GroupUtils.searchGroupSuggestions(term, self.equalOnly(), function (data) {
                if (data) {
                    for (var i = 0; i < data.length; i++) {
                        if ( data[i]) {
                            if (self.ids.indexOf(data[i].id) == -1) {
                                data[i].save = "1";
                                self.forms.splice(0, 0, data[i]);
                                self.ids.push(data[i].id);
                            }
                        }
                    }
                    for (var i = 0; i < self.forms().length; i++) {
                        var w = self.forms()[i];
                        var s = new select(w.id, w.word);
                        var index = -1;
                        for (var j = 0; j < self.selectWords().length; j++) {
                            if (self.selectWords()[j].id == s.id) {
                                index = j;
                                break;
                            }
                        }
                        if (index == -1) {
                            self.selectWords.push(s);
                        }
                    }
                }
            })
        };
        self.ids.push(word.id);

        self.init = function () {
            self.ids = [];
            self.forms([]);
            self.ids.push(word.id);
            GroupUtils.loadGroupsByWord(self.data().id, function (data) {
                if (data) {
                    self.selectWords([]);
                    var w = self.data();
                    self.selectWords.push(new select(w.id, w.word));
                    for (var i = 0; i < data.length; i++) {
                        if (self.ids.indexOf(data[i].id) == -1) {
                            data.deleteWordFromList = self.deleteWordFromList;
                            self.forms.push(data[i]);
                            self.ids.push(data[i].id);
                        }
                        if (data[i].initial) {
                            self.currentInitial(data[i].word);
                        }
                    }
                    for (var i = 0; i < self.forms().length; i++) {
                        w = self.forms()[i];
                        var s = new select(w.id, w.word);
                        var index = self.selectWords.indexOf(s);
                        if (index == -1) {
                            self.selectWords.push(new select(w.id, w.word));
                        }
                    }
                }
            })
        };

        self.deleteWordFromList = function (element, e) {
            var id = element.id;
            var index = self.ids.indexOf(id);
            if (index != -1) {
                self.ids.splice(index, 1);
            }
            index = -1;
            for (var i = 0; i < self.selectWords().length; i++) {
                if (self.selectWords()[i].id == id) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                self.selectWords.splice(i, 1);
            }
            index = self.forms.indexOf(element);
            if (index != -1) {
                self.forms.splice(index, 1);
            }
        };

        self.apply = function () {
            console.warn(self.forms());
            var gr_ids = [self.data().id];
            for (var i = 0; i < self.forms().length; i++) {
                if (self.forms()[i].save == "1") {
                    gr_ids.push(self.forms()[i].id);
                }
            }
            var initialWord = self.initialWord();
            var id = -1;
            if (initialWord) {
                id = initialWord.id;
            }
            GroupUtils.apply(gr_ids, id, function (data) {
                console.log('applying : ', data);
                self.modify(false);
                self.init();
            })
        };

        self.mod = function () {
            self.modify(true);
        };

        self.show = function () {
            self.modify(false);
        };

        self.init();

        var select = function (id, word) {
            this.id = id;
            this.word = word;
        };

        DictionaryLoader.loadPos(initPosTags);

        function initPosTags(data) {
            ko.postbox.publish('setPos', data);
        }

        ko.postbox.subscribe('reload', function (data) {
            if ( data) {
                self.data(data);
                self.posTags(data.posTags);
                localStorage['word'] = JSON.stringify(data);
            }
        });
    }

    return GroupDialog;
});
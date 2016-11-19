define(['knockout', 'utils/DictionaryModifier', 'postbox'], function (ko, DictionaryModifier) {
    function UpdateViewModel() {
        var self = this;

        self.word = ko.observable('');
        self.id = ko.observable(0);
        self.count = ko.observable(0);
        self.pos = ko.observableArray([]);
        self.selected = ko.observableArray([]);

        self.selected.subscribe(function(newValue){
            console.log("selected : " + newValue);
        });

        var allPos = [];

        ko.postbox.subscribe('updateWord', function (data) {
            self.word(data.word);
            self.id(data.id);
            self.count(data.wordCount);
            var tempPos = allPos;
            var ids = [];
            for (var i = 0; i < data.posTags.length; i++) {
                ids.push(data.posTags[i].id);
            }
            for (var i = 0; i < tempPos.length; i++) {
                tempPos[i].index = i;
                if (ids.indexOf(tempPos[i].id) != -1) {
                    tempPos[i].active = true;
                    self.selected.push(tempPos[i].id);
                }
                else {
                    tempPos[i].active = false;
                }
                self.pos.push(tempPos[i]);
            }
            console.log(self.pos())
        });

        self.checkValue = function (item, event) {
            if (item.active === true) console.log("dissociate item " + item.id);
            else console.log("associate item " + item.id);
            item.active = !item.active;
            return true;
        }

        ko.postbox.subscribe('setPos', function (data) {
            allPos = data;
        });

        self.update = function () {
            DictionaryModifier.updateWord(self.id(), self.word(), self.count(), self.selected());
        }
    };

    return UpdateViewModel;
});
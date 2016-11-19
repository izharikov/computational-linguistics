define(['knockout', 'utils/DictionaryModifier', 'postbox'], function(ko, DictionaryModifier){
    function AddDialogViewModel(){
        var self = this;
        self.pos = ko.observableArray([]);

        self.word = ko.observable('');
        self.wordCount = ko.observable('');
        self.selected = ko.observableArray([]);

        ko.postbox.subscribe('setPos', function (data) {
            self.pos(data);
        });

        self.add = function(){
            DictionaryModifier.addWord(self.word(), self.wordCount(), self.selected());
        };
        
        self.checkValue = function (item, event) {
            if (item.active === true) console.log("dissociate item " + item.id);
            else console.log("associate item " + item.id);
            item.active = !item.active;
            return true;
        }

    }

    return AddDialogViewModel;
});
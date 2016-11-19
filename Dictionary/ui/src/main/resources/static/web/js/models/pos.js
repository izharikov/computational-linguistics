define(['knockout', 'utils/DictionaryLoader', 'postbox'], function (ko, DictionaryLoader) {
    function PosViewModel() {
        var self = this;

        self.posTags = ko.observableArray([]);
        self.selectedTags = ko.observableArray([]);

        self.selectedIds = ko.computed(function () {
            var ids = [];
            for (var i = 0; i < self.selectedTags().length; i++) {
                ids.push(self.selectedTags()[i].id);
            }
            return ids;
        });

        DictionaryLoader.loadPos(initPosTags);

        function initPosTags(data) {
            ko.postbox.publish('setPos', data);
            for (var i = 0; i < data.length; i++) {
                data[i].active = false;
                data[i].index = i;
            }
            self.posTags(data);
        }

        self.select = function (element, event) {
            var index = -1;
            console.warn(element)
            if ((index = self.selectedTags().indexOf(element)) == -1) {
                self.posTags.remove(element);
                element.active = true;
                self.selectedTags.push(element);
            }
            else {
                self.posTags.splice(element.index, 0 ,element);
                self.selectedTags.remove(element);
            }
            ko.postbox.publish('reload', {pos: JSON.stringify(self.selectedIds())})
        }

        ko.postbox.subscribe('emptySelectedPos', function(){
            console.log('emptySelectedPos');
            for(var i = 0; i < self.selectedTags().length; i++){
                var element = self.selectedTags()[i];
                self.posTags.splice(element.index, 0 ,element);
            }
            console.warn(self.posTags());
            self.selectedTags([]);
        });
    }

    return PosViewModel;
});
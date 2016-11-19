define(['knockout', 'utils/DictionaryModifier', 'postbox'], function(ko, DictionaryModifier){
    function RemoveDialogViewModel(){
        var self = this;
        var id;
        self.word = ko.observable('');
        ko.postbox.subscribe('initRemove', function(data){
            self.word(data.word);
            id = data.id;
        });

        self.deleteWord = function(){
            console.warn('Word now will deleted ', self.word(), ' ', id);
            DictionaryModifier.remove(id);
        }
    }

    return RemoveDialogViewModel;
});
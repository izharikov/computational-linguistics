define(['utils/RestClient', 'knockout', 'jquery', 'postbox'], function(RestClient, ko, $){
    var BASE_UPDATE_URL = "/rest/dictionary/jpa/update";
    var BASE_SAVE_URL = "/rest/dictionary/jpa/save";
    var BASE_DELETE_URL = "/rest/dictionary/jpa/delete";

    function DictionaryModifier(){
        this.updateWord = function(id, word, count, posTags){
            var data = {
                id : id,
                word : word,
                count : count,
                pos : JSON.stringify(posTags)
            };
            RestClient.send(BASE_UPDATE_URL, RestClient.POST, data, function(data){
                console.log(data);
                ko.postbox.publish('reload', data);
                ko.postbox.publish('successMessage', 'Success updating word!');
            }, function(error){
                console.warn(error);
            });
        };

        this.addWord = function(word, count, posTags){
            var data = {
                word : word,
                count : count,
                pos : JSON.stringify(posTags)
            };
            RestClient.send(BASE_SAVE_URL, RestClient.POST, data, function(data){
                console.log(data);
                ko.postbox.publish('reload');
                ko.postbox.publish('successMessage', 'Success adding new word!');
            }, function(error){
                console.warn(error);
            });
        };

        this.remove = function(id){
            var data = {
                id : id
            };
            RestClient.send(BASE_DELETE_URL, RestClient.POST, data, function(){
                ko.postbox.publish('reload');
                ko.postbox.publish('successMessage', 'Success removing word!');
            }, function(error){
                console.warn(error);
            });
        }
    }
    return new DictionaryModifier();
});
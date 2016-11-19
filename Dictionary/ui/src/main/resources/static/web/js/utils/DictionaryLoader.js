define(['utils/RestClient', 'knockout', 'jquery', 'postbox'], function(RestClient, ko, $){
    function DictionaryLoader() {
        var BASE_LOAD_URL = '/rest/dictionary/jpa/results';
        var POS_LOAD_URL = '/rest/pos/all';
        
        this.loadDictionary = function(data, callback, errorCallback){
            if ( typeof errorCallback == 'undefined'){
                errorCallback = function(data){
                    console.warn('Error')
                }
            }
            RestClient.send(BASE_LOAD_URL, RestClient.GET, data, callback, errorCallback);
        };


        this.loadPos = function(callback, errorCallback){
            if ( typeof errorCallback == 'undefined'){
                errorCallback = function(data){
                    console.warn('Error')
                }
            }
            RestClient.send(POS_LOAD_URL, RestClient.GET, {}, callback, errorCallback);
        }


    }
    return new DictionaryLoader();
});
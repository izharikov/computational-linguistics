define(['utils/RestClient'], function(RestClient){
    function AnnotateUtils(){
        this.loadLastText = function(callback){
            RestClient.send('/rest/annotate/last', RestClient.POST, {}, callback, function(){});
        };

        this.modify = function(data, callback){
            RestClient.send('/rest/annotate/update-word', RestClient.POST, data, callback, function(){});
        };
    }

    return new AnnotateUtils();
});
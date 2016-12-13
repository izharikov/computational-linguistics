define(['jquery'], function($){
    function RestClient() {
        this.POST = "POST";
        this.GET = "GET";

        this.send = function(url, type, data, success, error) {
            if ( !data){
                data = {};
            }
            data.format = "json";
            window.ajaxCount++;
            $.ajax({
                method : type,
                url: url,
                type: type,
                data: data,
                success: success,
                error: error
            })
        }
    }
    return new RestClient();
});
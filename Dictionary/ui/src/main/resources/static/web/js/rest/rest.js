var POST = "POST";
var GET = "GET";
function RestClient(url, type, data, success, error){
    data.format = "json";
    $.ajax({
        url : url,
        type : type,
        data : data,
        success : success,
        error : error
    })
};
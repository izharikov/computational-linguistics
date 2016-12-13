define(['utils/RestClient'], function (RestClient) {
    function GroupUtils() {
        var BASE_URI = '/rest/dictionary/group/';

        this.searchGroupSuggestions = function (searchTerm, equalOnly, callback) {
            RestClient.send(BASE_URI + 'search', RestClient.GET, {term: searchTerm, equal: equalOnly}, callback);
        };

        this.loadGroupsByWord = function (id, callback) {
            RestClient.send(BASE_URI + 'words-groups', RestClient.GET, {id: id}, callback)
        };

        this.apply = function (ids, initId, callback) {
            RestClient.send(BASE_URI + 'apply', RestClient.POST,
                {ids: JSON.stringify(ids), init: initId}, callback);
        }
    };

    return new GroupUtils();
});
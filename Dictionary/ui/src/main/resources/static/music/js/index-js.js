function AppViewModel() {
    self = this
    self.tableBody = ko.observableArray([])
    
    self.loadBody = function(){
        $('.canvas-holder').removeClass("hide").addClass("show")
        // setTimeout(function() {
        //     var test = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'z', 't']
        //     for (var i = 0; i < 10; i++) {
        //         self.tableBody.push({
        //             count: i,
        //             word: test[i]
        //         })
        //     }
        //     $('.canvas-holder').removeClass("show").addClass("hide")
        // }, 10000);
        var type = ['Rock', 'Rock', 'Classic', 'Metal', 'Pop']
        var name = ['compos 1', 'name of compos', 'wer', 'sdfvds', 'sddsfs']
        var year = ['2010', '1998', '1687', '2012', '1999']
        var time = ['2m 13s', '5m 34s', '6m 45s', '4m', '3m 59s']
        self.tableBody([])
        for(var i = 0; i < type.length; i++){
            self.tableBody.push({
                type : type[i],
                name : name[i],
                year : year[i],
                time : time[i]
            })
        }
    }
    self.empty = function(){
        self.tableBody([])
    }
    
    self.availableRecords = ko.observableArray([])
    self.availableRecords.push({
        value : 1,
        name : "1st available"
    })
}
ko.applyBindings(new AppViewModel());
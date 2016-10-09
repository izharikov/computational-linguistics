function sortByName(){
    console.log('name')
}
function sortByCount(){
    console.log('count')
}
function loadDefault() {
    $('.canvas-holder').removeClass("hide").addClass("show")
    $.get('/web/html/result.html').then(function (responseData) {
        var dictBody = $('#dict-tbody');
        dictBody.append(responseData);
        $('.canvas-holder').removeClass("show").addClass("hide")
        $("#dict-tbl").tablesorter();
    });
}


function clickMe(){
    console.log("clicked")
    $("#tbl").tablesorter({
        // sort on the first column and third column, order asc
        sortList: [[0,0],[2,0]]
    });
}

$(document).ready(function(){
    $('.canvas-holder').addClass("hide")
})
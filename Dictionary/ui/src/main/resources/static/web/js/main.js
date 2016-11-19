requirejs.config({
    basePath: '/web/js/',
    shim: {
        "bootstrap": {"deps": ['jquery', 'tether']}
    },
    paths: {
        jquery: 'lib/jquery.min',
        bootstrap: 'lib/bootstrap',
        tether: 'lib/tether.min',
        knockout: 'lib/knockout-3.4.0',
        postbox: 'lib/knockout-postbox',
        'knockout.contextmenu' : 'lib/knockout-context-menu'
    }
});
require(['tether', 'knockout.contextmenu'], function (Tether) {
    window.Tether = Tether;
});
define(['jquery', 'tether', 'bootstrap', 'knockout', 'models/header',
        'models/app', 'models/pagination', 'models/pos', 'models/updateDialog', 'models/removeDialog', 'models/addDialog',
        'models/successMessage','knockout.contextmenu'],
    function ($, t, b, ko, HeaderViewModel, AppViewModel, PaginationViewModel, PosViewModel, UpdateViewModel,
              RemoveDialogViewModel, AddDialogViewModel, SuccessMessageViewModel) {
        // window.Tether = t;
        var ajaxCount = 1;
        var enableAjaxLoader = true;

        $(document).ajaxStart(function () {
            // show loader on start
            if (enableAjaxLoader && ajaxCount >= 0){
                $pleaseWaitDialog.modal("show");
            }
            ajaxCount++;
            console.warn("AJAX START");
        }).ajaxSuccess(function () {
            // hide loader on success
            console.warn("ajax count", ajaxCount);
            if (ajaxCount > 0){
                ajaxCount--;
                if( ajaxCount == 0) {
                    $pleaseWaitDialog.modal("hide");
                }
            }
            console.warn("AJAX END");
        });


        var $updateDialog = $("#updateDialog");
        var $deleteDialog = $("#deleteDialog");

        var $contextMenu = $("#contextMenu");
        var $body = $("body");
        var $pleaseWaitDialog = $("#pleaseWaitDialog");
        var $selectedItem;

        $body.on("contextmenu", "table", function (e) {
            $contextMenu.css({
                display: "block",
                left: e.pageX,
                top: e.pageY
            });
            $selectedItem = $(e.target).parent()[0];
            return false;
        });

        $body.on('click', function(e){
            $contextMenu.hide();
        });

        $contextMenu.on("click", "a.update", function () {
            $contextMenu.hide();
            ko.postbox.publish('updateWord', $($selectedItem).data('info'));
            $updateDialog.modal('show');
        });

        $contextMenu.on("click", "a.delete", function () {
            $contextMenu.hide();
            ko.postbox.publish('initRemove', $($selectedItem).data('info'));
            $deleteDialog.modal('show');
        });

        ko.postbox.subscribe('successMessage', function(){
            enableAjaxLoader = false;
            setTimeout(function(){
                enableAjaxLoader = true;
            }, 1000);
        });


        ko.applyBindings(new HeaderViewModel(), document.getElementById("header"));
        ko.applyBindings(new AppViewModel(), document.getElementById("main"));
        ko.applyBindings(new PaginationViewModel(), document.getElementById("pagination"));
        ko.applyBindings(new PosViewModel(), document.getElementById("pos"));
        ko.applyBindings(new UpdateViewModel(), document.getElementById("updateDialog"));
        ko.applyBindings(new RemoveDialogViewModel(), document.getElementById("deleteDialog"));
        ko.applyBindings(new AddDialogViewModel(), document.getElementById("addWord"));
        ko.applyBindings(new SuccessMessageViewModel(), document.getElementById("successMessage"));

    });
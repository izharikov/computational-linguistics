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
        'knockout.contextmenu': 'lib/knockout-context-menu'
    }
});
require(['tether', 'knockout.contextmenu'], function (Tether) {
    window.Tether = Tether;
});

define(['jquery', 'tether', 'bootstrap', 'knockout', 'models/groupDialog', 'models/updateDialog', 'models/addDialog'],
    function ($, T, B, ko, GroupDialog, UpdateViewModel, AddDialogViewModel) {
        var word = JSON.parse(localStorage['word']);
        var modify = localStorage['modify'] == true;
        console.warn(word);
        var ajaxCount = 0;
        var enableAjaxLoader = true;

        $(document).ajaxStart(function () {
            // show loader on start
            if (enableAjaxLoader && ajaxCount >= 0) {
                $pleaseWaitDialog.modal("show");
            }
            ajaxCount++;
            console.warn("AJAX START");
        }).ajaxSuccess(function () {
            // hide loader on success
            console.warn("ajax count", ajaxCount);
            if (ajaxCount > 0) {
                ajaxCount--;
                if (ajaxCount == 0) {
                    $pleaseWaitDialog.modal("hide");
                }
            }
            console.warn("AJAX END");
        });
        var $pleaseWaitDialog = $("#pleaseWaitDialog");

        var $selectedItem;
        var $contextMenu = $("#contextMenu");
        var $body = $("body");

        $body.on("contextmenu", "tbody tr", function (e) {
            $contextMenu.css({
                display: "block",
                left: e.pageX,
                top: e.pageY
            });
            $selectedItem = $(e.target).parent()[0];
            if ($selectedItem.tagName == "TD") {
                $selectedItem = $($selectedItem).parent()[0];
            }
            return false;
        });

        $body.on('click', function (e) {
            $contextMenu.hide();
        });

        var $updateDialog = $("#updateDialog");

        $contextMenu.on("click", "a.pos", function () {
            ko.postbox.publish("updateWord", $($selectedItem).data('info'));
            $updateDialog.modal('show');
        });

        ko.applyBindings(new GroupDialog(word, modify), document.getElementById('main-body'));
        ko.applyBindings(new UpdateViewModel(), document.getElementById('updateDialog'));
        ko.applyBindings(new AddDialogViewModel(), document.getElementById("addWord"));
    });
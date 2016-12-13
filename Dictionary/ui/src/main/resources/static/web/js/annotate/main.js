requirejs.config({
    basePath: '/web/js/',
    shim: {
        "bootstrap": {"deps": ['jquery', 'tether']}
    },
    paths: {
        jquery: '/web/js/lib/jquery.min',
        bootstrap: '/web/js/lib/bootstrap',
        tether: '/web/js/lib/tether.min',
        knockout: '/web/js/lib/knockout-3.4.0',
        postbox: '/web/js/lib/knockout-postbox',
        'knockout.contextmenu': '/web/js/lib/knockout-context-menu'
    }
});
require(['tether', 'knockout.contextmenu'], function (Tether) {
    window.Tether = Tether;
});
define(['jquery', 'tether', 'bootstrap', 'knockout', 'annotateView'], function($, T,B,ko,AnnotateView){
    ko.applyBindings(new AnnotateView(), document.getElementById("annotate-view"))
});
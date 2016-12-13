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
        'knockout.contextmenu': 'lib/knockout-context-menu'
    }
});
require(['tether', 'knockout.contextmenu'], function (Tether) {
    window.Tether = Tether;
});
define(['jquery', 'tether', 'bootstrap', 'knockout', 'utils/AnnotateUtils','postbox'], function ($, T, B, ko, AnnotateUtils) {
    function AnnotateModel() {
        var self = this;
        self.outHtml = ko.observable('');
        self.posFreq = ko.observableArray([]);
        self.posPairFreq = ko.observableArray([]);
        self.wordFreq = ko.observableArray([]);
        self.countOfSent = ko.observableArray([]);
        self.countOfWords = ko.observable(1);
        self.wordAndPos = ko.observableArray([]);

        self.poses = ko.observableArray([]);
        self.data = ko.observable({});

        var posTags = {};

        self.init = function (data) {
            posTags = data.posTags;
            self.poses(fromMapToArray(posTags, value));
            self.outHtml(data.htmlOutput);
            self.countOfWords(data.countOfWords);
            $('[title]').tooltip();
            self.posFreq(fromMapToArray(data.posFrequency, value));
            self.posPairFreq(fromPairFreq(data.posPairFrequency));
            addDescriptionToTags(posTags);
            self.wordAndPos(fromMapToArray(data.wordAndPos, valueToArray));
        };

        var oldPos = null;
        self.data_word = ko.observable('');
        self.data_pos = ko.observable('');
        self.data_index = ko.observable(0);

        ko.postbox.subscribe('startUpdatePos', function (data) {
            self.data_word(data.text);
            self.data_pos(pos(data.pos));
            oldPos = data.pos;
            self.data_index(data.index);
        });

        function pos(pos){
            for(var i = 0; i < self.poses(); i++){
                if ( self.poses()[i].key == pos){
                    return self.poses()[i];
                }
            }
        }

        function value(value) {
            return value;
        }

        function valueToArray(arrValue){
            return fromMapToArray(arrValue, value);
        }

        function fromMapToArray(map, f) {
            var array = [];
            $.each(map, function (key, value) {
                array.push({key: key, value: f(value)});
            });
            return array;
        }

        function fromPairFreq(pairFreq) {
            var result = [];
            $.each(pairFreq, function (key, value) {
                var spl = key.split('_');
                var obj = {first: spl[0], second: spl[1], count: value};
                result.push(obj);
            });
            return result;
        }

        self.posDescription = function (posName) {
            return posTags[posName].description;
        };

        var order = 'asc';
        self.sort = function () {
            if (order == 'asc') {
                self.posFreq.sort(sortFuncAsc('value'));
                order = 'desc';
            } else {
                self.posFreq.sort(sortFuncDesc('value'));
                order = 'asc';
            }
        };

        var orderPair = 'asc';

        self.sortPair = function () {
            if (orderPair == 'asc') {
                self.posPairFreq.sort(sortFuncAsc('count'));
                orderPair = 'desc';
            } else {
                self.posPairFreq.sort(sortFuncDesc('count'));
                orderPair = 'asc';
            }
        };

        function sortFuncAsc(objProp) {
            return function sortAsc(left, right) {
                return left[objProp] - right[objProp];
            }
        }

        function sortFuncDesc(objProp) {
            return function sortAsc(left, right) {
                return right[objProp] - left[objProp];
            }
        }

        AnnotateUtils.loadLastText(self.init);

        function addDescriptionToTags(posMap) {
            $('span[data-toggle=tooltip]').each(function (index) {
                var $this = $(this);
                var title = $this.data('original-title');
                if (!title) {
                    title = $this.attr('title');
                }
                if (title) {
                    var descr = title + " : " + posMap[title].description;
                    $this.attr('data-original-title', descr);
                }
                $this.attr('data-index', index);
            });
        }

        self.update = function(){
            var data = {
                oldPos : oldPos,
                word : self.data_word(),
                newPos : self.data_pos().key,
                index : self.data_index()
            }
            AnnotateUtils.modify(data, function(){
                AnnotateUtils.loadLastText(self.init);
            });
        }
    }

    var $contextMenu = $("#contextMenu");
    var $dialog = $("#changePos");
    var $body = $("body");
    var $selectedItem;

    $body.on("contextmenu", "span[data-toggle='tooltip']", function (e) {
        console.warn('clicked');
        $contextMenu.css({
            display: "block",
            left: e.pageX,
            top: e.pageY
        });
        $selectedItem = $(e.target);
        return false;
    });

    $contextMenu.on("click", "a", function () {
        $contextMenu.hide();
        $dialog.modal('show');
        ko.postbox.publish('startUpdatePos', {text : ($selectedItem).text(),
            pos : ($selectedItem).data('original-title'), index : ($selectedItem).data('index')})
    });

    $body.on('click', function (e) {
        $contextMenu.hide();
    });

    ko.applyBindings(new AnnotateModel(), document.getElementById("main"));
});
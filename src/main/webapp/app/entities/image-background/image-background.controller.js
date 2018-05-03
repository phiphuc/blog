(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('ImageBackgroundController', ImageBackgroundController);

    ImageBackgroundController.$inject = ['DataUtils', 'ImageBackground','ewsJavascriptApi'];

    function ImageBackgroundController(DataUtils, ImageBackground,ewsJavascriptApi) {

        var vm = this;

        vm.imageBackgrounds = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            ImageBackground.query(function(result) {
                vm.imageBackgrounds = result;
                vm.searchQuery = null;
            });
        }
    }
})();

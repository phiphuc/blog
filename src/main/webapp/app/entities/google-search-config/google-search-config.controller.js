(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('GoogleSearchConfigController', GoogleSearchConfigController);

    GoogleSearchConfigController.$inject = ['GoogleSearchConfig'];

    function GoogleSearchConfigController(GoogleSearchConfig) {

        var vm = this;

        vm.googleSearchConfigs = [];

        loadAll();

        function loadAll() {
            GoogleSearchConfig.query(function(result) {
                vm.googleSearchConfigs = result;
                vm.searchQuery = null;
            });
        }
    }
})();

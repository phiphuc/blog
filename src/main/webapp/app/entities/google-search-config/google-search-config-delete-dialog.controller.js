(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('GoogleSearchConfigDeleteController',GoogleSearchConfigDeleteController);

    GoogleSearchConfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'GoogleSearchConfig'];

    function GoogleSearchConfigDeleteController($uibModalInstance, entity, GoogleSearchConfig) {
        var vm = this;

        vm.googleSearchConfig = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GoogleSearchConfig.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

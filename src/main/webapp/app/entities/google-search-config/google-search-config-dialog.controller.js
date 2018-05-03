(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('GoogleSearchConfigDialogController', GoogleSearchConfigDialogController);

    GoogleSearchConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GoogleSearchConfig'];

    function GoogleSearchConfigDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GoogleSearchConfig) {
        var vm = this;

        vm.googleSearchConfig = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.googleSearchConfig.id !== null) {
                GoogleSearchConfig.update(vm.googleSearchConfig, onSaveSuccess, onSaveError);
            } else {
                GoogleSearchConfig.save(vm.googleSearchConfig, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('blogApp:googleSearchConfigUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

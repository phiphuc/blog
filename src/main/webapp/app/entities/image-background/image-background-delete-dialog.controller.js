(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('ImageBackgroundDeleteController',ImageBackgroundDeleteController);

    ImageBackgroundDeleteController.$inject = ['$uibModalInstance', 'entity', 'ImageBackground'];

    function ImageBackgroundDeleteController($uibModalInstance, entity, ImageBackground) {
        var vm = this;

        vm.imageBackground = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ImageBackground.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('ImageBackgroundDialogController', ImageBackgroundDialogController);

    ImageBackgroundDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'ImageBackground'];

    function ImageBackgroundDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, ImageBackground) {
        var vm = this;

        vm.imageBackground = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.imageBackground.id !== null) {
                ImageBackground.update(vm.imageBackground, onSaveSuccess, onSaveError);
            } else {
                ImageBackground.save(vm.imageBackground, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('blogApp:imageBackgroundUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImgBlob = function ($file, imageBackground) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        imageBackground.imgBlob = base64Data;
                        imageBackground.imgBlobContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.createdDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('ImageBackgroundDetailController', ImageBackgroundDetailController);

    ImageBackgroundDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ImageBackground'];

    function ImageBackgroundDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ImageBackground) {
        var vm = this;

        vm.imageBackground = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('blogApp:imageBackgroundUpdate', function(event, result) {
            vm.imageBackground = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

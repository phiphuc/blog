(function() {
    'use strict';

    angular
        .module('blogApp')
        .controller('GoogleSearchConfigDetailController', GoogleSearchConfigDetailController);

    GoogleSearchConfigDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GoogleSearchConfig'];

    function GoogleSearchConfigDetailController($scope, $rootScope, $stateParams, previousState, entity, GoogleSearchConfig) {
        var vm = this;

        vm.googleSearchConfig = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('blogApp:googleSearchConfigUpdate', function(event, result) {
            vm.googleSearchConfig = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

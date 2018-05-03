(function() {
    'use strict';

    angular
        .module('blogApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('google-search-config', {
            parent: 'entity',
            url: '/google-search-config',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blogApp.googleSearchConfig.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/google-search-config/google-search-configs.html',
                    controller: 'GoogleSearchConfigController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('googleSearchConfig');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('google-search-config-detail', {
            parent: 'google-search-config',
            url: '/google-search-config/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blogApp.googleSearchConfig.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/google-search-config/google-search-config-detail.html',
                    controller: 'GoogleSearchConfigDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('googleSearchConfig');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GoogleSearchConfig', function($stateParams, GoogleSearchConfig) {
                    return GoogleSearchConfig.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'google-search-config',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('google-search-config-detail.edit', {
            parent: 'google-search-config-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/google-search-config/google-search-config-dialog.html',
                    controller: 'GoogleSearchConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GoogleSearchConfig', function(GoogleSearchConfig) {
                            return GoogleSearchConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('google-search-config.new', {
            parent: 'google-search-config',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/google-search-config/google-search-config-dialog.html',
                    controller: 'GoogleSearchConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                googleKey: null,
                                googleCx: null,
                                googleOpt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('google-search-config', null, { reload: 'google-search-config' });
                }, function() {
                    $state.go('google-search-config');
                });
            }]
        })
        .state('google-search-config.edit', {
            parent: 'google-search-config',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/google-search-config/google-search-config-dialog.html',
                    controller: 'GoogleSearchConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GoogleSearchConfig', function(GoogleSearchConfig) {
                            return GoogleSearchConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('google-search-config', null, { reload: 'google-search-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('google-search-config.delete', {
            parent: 'google-search-config',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/google-search-config/google-search-config-delete-dialog.html',
                    controller: 'GoogleSearchConfigDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GoogleSearchConfig', function(GoogleSearchConfig) {
                            return GoogleSearchConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('google-search-config', null, { reload: 'google-search-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

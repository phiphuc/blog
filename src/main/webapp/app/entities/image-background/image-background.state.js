(function() {
    'use strict';

    angular
        .module('blogApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('image-background', {
            parent: 'entity',
            url: '/image-background',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blogApp.imageBackground.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/image-background/image-backgrounds.html',
                    controller: 'ImageBackgroundController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('imageBackground');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('image-background-detail', {
            parent: 'image-background',
            url: '/image-background/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'blogApp.imageBackground.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/image-background/image-background-detail.html',
                    controller: 'ImageBackgroundDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('imageBackground');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ImageBackground', function($stateParams, ImageBackground) {
                    return ImageBackground.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'image-background',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('image-background-detail.edit', {
            parent: 'image-background-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/image-background/image-background-dialog.html',
                    controller: 'ImageBackgroundDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ImageBackground', function(ImageBackground) {
                            return ImageBackground.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('image-background.new', {
            parent: 'image-background',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/image-background/image-background-dialog.html',
                    controller: 'ImageBackgroundDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                imgBlob: null,
                                imgBlobContentType: null,
                                createdDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('image-background', null, { reload: 'image-background' });
                }, function() {
                    $state.go('image-background');
                });
            }]
        })
        .state('image-background.edit', {
            parent: 'image-background',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/image-background/image-background-dialog.html',
                    controller: 'ImageBackgroundDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ImageBackground', function(ImageBackground) {
                            return ImageBackground.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('image-background', null, { reload: 'image-background' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('image-background.delete', {
            parent: 'image-background',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/image-background/image-background-delete-dialog.html',
                    controller: 'ImageBackgroundDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ImageBackground', function(ImageBackground) {
                            return ImageBackground.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('image-background', null, { reload: 'image-background' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

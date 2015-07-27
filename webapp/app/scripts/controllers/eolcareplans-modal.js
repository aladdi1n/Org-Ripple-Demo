'use strict';

angular.module('openehrPocApp')
  .controller('EolcareplansModalCtrl', function ($scope, $filter, $modalInstance, eolcareplan, patient, modal) {
        
    $scope.eolcareplan = eolcareplan;
    $scope.patient = patient;
    $scope.modal = modal;

    $scope.ok = function (eolcareplanForm, eolcareplan) {
      $scope.formSubmitted = true;
      if (eolcareplanForm.$valid) {
        $modalInstance.close(eolcareplan);
      }
    };
        
    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
    
     $scope.radioModel = 'Tab1';
    
        
    $scope.typesAvaliable = ['Document', 'Document T1', 'Document T2', 'Document T3'];
    $scope.typesChosen = $scope.typesAvaliable[0];

    $scope.openDatepicker = function ($event, name) {
      $event.preventDefault();
     $event.stopPropagation();
        
      $scope[name] = true;
    };
    
    $scope.openDecisionDatepicker = function ($event, name) {
      $event.preventDefault();
     $event.stopPropagation();
        
      $scope[name] = true;
    };
    
       
    $scope.dateofcprdecisionDatepicker = function ($event, name) {
      $event.preventDefault();
     $event.stopPropagation();
        
      $scope[name] = true;
    };
    
    $scope.validate = function(form, name, index){
      var errorToCheckFor = name + index;

      for(var error in form.$error.required){
        var errorName = form.$error.required[error].$name;

        if (errorName === errorToCheckFor){
          return true;
        }
      }
    };
    
      $scope.validateDirty = function(form, name, index){
      var errorToCheckFor = name + index;

      if(form[errorToCheckFor].$dirty && form[errorToCheckFor].$invalid){
        return true;
      } else {
        return false;
      }
    };

    $scope.validateClean = function(form, name, index){
      var errorToCheckFor = name + index;

      if(form[errorToCheckFor].$dirty && form[errorToCheckFor].$valid){
        return true;
      } else {
        return false;
      }
    };

  });

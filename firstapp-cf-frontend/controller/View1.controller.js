sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"sap/ui/model/json/JSONModel",
	"de.adwizor.cloud.sdk.tutorial.firstapp-cf-frontend/service/businesspartners"
], function (Controller, JSONModel, BusinessPartnerService) {
	"use strict";

	return Controller.extend("de.adwizor.cloud.sdk.tutorial.firstapp-cf-frontend.controller.View1", {
		
		onInit: function() {
			var view = this.getView();
			
			BusinessPartnerService.getBusinessPartners()
				.done(function(data) {
					var model = new JSONModel(data);
					view.setModel(model, "businessPartner");
				});
		}
		
	});
});
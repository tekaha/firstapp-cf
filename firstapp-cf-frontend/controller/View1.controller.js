sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"sap/ui/model/json/JSONModel"
], function (Controller, JSONModel) {
	"use strict";

	return Controller.extend("de.adwizor.cloud.sdk.tutorial.firstapp-cf-frontend.controller.View1", {
		
		onInit: function() {
			var view = this.getView();
			
			jQuery.get("/businesspartners")
				.done(function(data) {
					var model = new JSONModel(data);
					view.setModel(model, "businessPartner");
				});
		}
		
	});
});
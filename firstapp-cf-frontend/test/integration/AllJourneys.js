/* global QUnit*/

sap.ui.define([
	"sap/ui/test/Opa5",
	"de/adwizor/cloud/sdk/tutorial/firstapp-cf-frontend/test/integration/pages/Common",
	"sap/ui/test/opaQunit",
	"de/adwizor/cloud/sdk/tutorial/firstapp-cf-frontend/test/integration/pages/View1",
	"de/adwizor/cloud/sdk/tutorial/firstapp-cf-frontend/test/integration/navigationJourney"
], function (Opa5, Common) {
	"use strict";
	Opa5.extendConfig({
		arrangements: new Common(),
		viewNamespace: "de.adwizor.cloud.sdk.tutorial.firstapp-cf-frontend.view.",
		autoWait: true
	});
});
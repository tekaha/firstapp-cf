sap.ui.define([], function () {
    "use strict";

    return {
        getBusinessPartners: function () {
            return jQuery.get("/businesspartners")
        }
    }
});
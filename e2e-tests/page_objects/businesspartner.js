module.exports = {
    url: function () {
        return this.api.launchUrl;
    },
    elements: {
        body: 'body',
        title:
        {
           selector : "//div[contains(@class, 'sapMTitle')]",
           locateStrategy: "xpath"
        }
    }
};
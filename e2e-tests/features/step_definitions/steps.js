const { client } = require('nightwatch-cucumber')
const { defineSupportCode } = require('cucumber')

defineSupportCode(({ Given, Then, When }) => {
  Given(/^I open the Business Partner home page$/, () => {
    const businesspartner = client.page.businesspartner()
    console.info (businesspartner.url())
    return businesspartner
      .navigate()
      .waitForElementVisible('@body')
  })

  Then(/^the title is "(.*?)"$/, (titleValue) => {
    const businesspartner = client.page.businesspartner()
    businesspartner.waitForElementVisible('@title')
    return businesspartner.expect.element('@title').text.to.equal(titleValue)
  })

})
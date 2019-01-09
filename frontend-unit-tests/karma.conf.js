const argv = require("yargs").argv;

module.exports = function (config) {
    config.set({
        basePath: './',

        frameworks: ['openui5', 'jasmine'],

        openui5: {
            path: 'https://sapui5.hana.ondemand.com/1.42.9/resources/sap-ui-cachebuster/sap-ui-core.js',
            useMockServer: false
        },

        client: {
             openui5: {
                 config: {
                     theme: 'sap_bluecrystal',
                     resourceroots: {
                         'de.adwizor.cloud.sdk.tutorial.firstapp-cf-frontend': './base/firstapp-cf-frontend',
                     }
                 }
             }
         },

        files: [{
            pattern: '../firstapp-cf-frontend/**',
            served: true,
            included: false
        },
            './tests/**/*.jasmine.js'
        ],

        browsers: [argv.headless?"ChromeHeadless" :"Chrome"],

        reporters: ['junit', 'progress', 'coverage'],

        preprocessors: {
            './firstapp-cf-frontend/**/*.js': ['coverage'],
        },

        junitReporter: {
            outputDir: 's4hana_pipeline/reports/frontend-unit', // results will be saved as $outputDir/$browserName.xml
            outputFile: 'Test.frontend.unit.xml', // if included, results will be saved as $outputDir/$browserName/$outputFile
            suite: '', // suite will become the package name attribute in xml testsuite element
            useBrowserName: true, // add browser name to report and classes names
            nameFormatter: undefined, // function (browser, result) to customize the name attribute in xml testcase element
            classNameFormatter: undefined // function (browser, result) to customize the classname attribute in xml testcase element
        },

        coverageReporter: {
            // specify a common output directory
            dir: 's4hana_pipeline/reports/frontend-unit/coverage',

            includeAllSources: true,

            reporters: [
                {
                    type: 'html',
                    subdir: 'report-html/ut'
                }, {
                    type: 'lcov',
                    subdir: 'report-lcov/ut'
                }, {
                    type: 'cobertura',
                    subdir: '.',
                    file: 'cobertura.frontend.unit.xml'
                }
            ],
            instrumenterOptions: {
                istanbul: {
                    noCompact: true
                }
            }
        },
    });
};
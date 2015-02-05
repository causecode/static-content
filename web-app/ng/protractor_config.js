// Current Protractor Version 1.6.1

exports.config = {
    seleniumServerJar: 'selenium-server-standalone-2.44.0.jar',

    suites : {
        dev: ['test/spec/e2e/*.spec.js'],
    },

    framework:'mocha',
    seleniumArgs: ['-browserTimeout=60'],
    capabilities: {
        'browserName': 'firefox'
    },

    getPageTimeout: 20000,
    allScriptsTimeout: 40000,

    onPrepare: function() {
        browser.driver.manage().window().maximize();
    },

    rootElement: 'html',
    
    baseUrl: 'http://localhost:8080/ng/app/index.html#/', //locally

    mochaOpts:{
      reporter:'spec',
      baseDirectory: 'test/e2e/test-results',
      takeScreenShotsOnlyForFailedSpecs: true,
      enableTimeouts: false
    }
};
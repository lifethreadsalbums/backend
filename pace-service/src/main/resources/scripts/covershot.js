
var webPage = require('webpage'),
    system = require('system');

var loginUrl = system.args[1],
    pageUrl = system.args[2],
    user = system.args[3],
    pass = system.args[4],
    file = system.args[5];

console.log(loginUrl, pageUrl, user, pass, file);


var loginPage = webPage.create();
var settings = {
    operation: "POST",
    encoding: "utf8",
    headers: {
        "Content-Type": "application/json"
    },
    data: JSON.stringify({
        username: user,
        password: pass
    })
};

loginPage.open(loginUrl, settings, function(status) {
    console.log('Login: ' + status);

    var page = webPage.create();
    page.viewportSize = { width: 1500 + 275, height: 1500 + 115 + 130 };

    page.onResourceRequested = function (request) {
        console.log('Request ' + request.url);
    };

    page.onError = function (msg, trace) {
        console.log(msg);
        trace.forEach(function(item) {
            console.log('  ', item.file, ':', item.line);
        });
    };

    page.open(pageUrl, function (status) {
        console.log('open', status)
        if (status !== 'success') {
            console.log('Unable to load the address!');
        } else {
            window.setTimeout(function () {
                page.clipRect = { left:275, top: 115, width: 1500, height: 1500 };
                page.render(file, {format: 'jpeg', quality: '100'});
                phantom.exit();
            }, 1000 * 10);
        }
    });
  
});





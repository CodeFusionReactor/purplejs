/* global Java */
/* global __ */
var assert = Java.type('org.junit.Assert');

var lib = require('./library');
assert.assertEquals('Hello World', lib.hello());

__.registerMock('/app/mock/library.js', {
    hello: function () {
        return 'Hello Mock';
    }
});

lib = require('./library');
assert.assertEquals('Hello Mock', lib.hello());

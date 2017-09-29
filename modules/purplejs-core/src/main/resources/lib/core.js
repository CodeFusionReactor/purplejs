/* global exports */

/**
 * This module contains core I/O methods that is mainly stream-related.
 *
 * @example
 * // Require the core library.
 * var core = require('/lib/core');
 *
 * @module core
 */

var system = require('/lib/system');

// Create a new instance of helper and get hold of a resource-loader.
var helper = system.newBean('io.purplejs.core.internal.lib.CoreLibHelper');

/**
 * Read text from a stream.
 *
 * @example
 * // Read text from stream.
 * var text = core.readText(stream);
 *
 * @param {*} stream Stream to read text from.
 * @returns {string} Returns the text read from stream.
 */
exports.readText = function (stream) {
    return helper.readText(stream);
};

/**
 * Read lines from a stream.
 *
 * @example
 * // Read text lines from stream.
 * var lines = core.readLines(stream);
 *
 * @param {*} stream Stream to read lines from.
 * @returns {string[]} Returns lines as an array.
 */
exports.readLines = function (stream) {
    return system.toNativeObject(helper.readLines(stream));
};

/**
 * Process lines from a stream.
 *
 * @example
 * // Process lines from stream.
 * core.processLines(stream, function(line) {
 *   log.info('Line -> ' + line);
 * });
 *
 * @param {*} stream Stream to read lines from.
 * @param {function} func Callback function to be called for each line.
 */
exports.processLines = function (stream, func) {
    helper.processLines(stream, func);
};

/**
 * Returns the size of a stream.
 *
 * @example
 * // Get size of the stream.
 * var size = core.getStreamSize(stream);
 *
 * @param {*} stream Stream to get size of.
 * @returns {number} Returns the size of a stream.
 */
exports.getStreamSize = function (stream) {
    return helper.getStreamSize(stream);
};

/**
 * Returns a new stream from a string.
 *
 * @example
 * // Create a new stream using a text string.
 * var stream = core.newStream('stream content');
 *
 * @param {string} text String to create a stream of.
 * @returns {*} A new stream.
 */
exports.newStream = function (text) {
    var clz = Java.type('io.purplejs.core.util.ResourceHelper');
    log.info(clz.currentScript);


    return helper.newStream(text);
};

/**
 * Loads a resource.
 *
 * @example
 * // Load a resource from path.
 * var stream = core.loadResource('/path/to/text.txt');
 *
 * @example
 * // Load a resource from resolved path.
 * var path = resolve('../text.txt');
 * var stream = core.loadResource(path);
 *
 * @param {*} path Resource path to load.
 * @returns {*} A stream for the resource or undefined.
 */
exports.loadResource = function (path) {
    return helper.loadResource(system.getResourceLoader(), path);
};

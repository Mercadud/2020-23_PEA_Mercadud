import {nodeResolve} from "@rollup/plugin-node-resolve";

// noinspection JSUnusedGlobalSymbols
export default {
    // every time you edit the editor.js file, run 'npm start' or 'npm run build' to create the bundle
    input: "./editor.js",
    output: {
        // resulting file
        file: "./editor.bundle.js",
        format: "iife",
        // this is the name of the module when you want to access any of the exports of the editor.js file
        name: "editorConfig"
    },
    plugins: [nodeResolve({
        // prevents an issue related to duplicate dependencies
        dedupe: ["@codemirror/state"]
    })],
};
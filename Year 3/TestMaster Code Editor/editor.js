// The code editor library used is CodeMirror 6
// doc: https://codemirror.net/docs/
import {EditorState} from '@codemirror/state';
import {defaultKeymap, history, historyKeymap, indentWithTab} from "@codemirror/commands";
import {java} from "@codemirror/lang-java";
import {
    EditorView,
    crosshairCursor,
    dropCursor,
    highlightActiveLineGutter,
    highlightSpecialChars, keymap,
    lineNumbers, rectangularSelection,
    highlightTrailingWhitespace, highlightActiveLine,
} from "@codemirror/view";
import {
    bracketMatching,
    defaultHighlightStyle,
    foldGutter, foldKeymap,
    indentOnInput,
    syntaxHighlighting
} from "@codemirror/language";
import {autocompletion, closeBrackets, closeBracketsKeymap, completionKeymap} from "@codemirror/autocomplete";
import {highlightSelectionMatches, searchKeymap, search} from "@codemirror/search";
import {lintKeymap} from "@codemirror/lint";
import {createTheme} from 'thememirror';
import {tags as t} from '@lezer/highlight';

// this is the theme of the code editor
const testMasterCodeTheme = createTheme({
    variant: 'dark',
    settings: {
        background: '#152627',
        foreground: '#eff1f1',
        caret: '#eff1f1',
        selection: '#6986a5',
        lineHighlight: '#6b7f8c',
        gutterBackground: '#152627',
        gutterForeground: '#eff1f1',
    },
    styles: [
        {
            tag: t.comment,
            color: '#22da28',
        },
        {
            tag: t.variableName,
            color: '#eff1f1',
        },
        {
            tag: [t.string, t.special(t.brace)],
            color: '#ffbe6f',
        },
        {
            tag: t.number,
            color: '#8ff0a4',
        },
        {
            tag: t.bool,
            color: '#8ff0a4',
        },
        {
            tag: t.null,
            color: '#8ff0a4',
        },
        {
            tag: t.keyword,
            color: '#cdab8f',
        },
        {
            tag: t.operator,
            color: '#eff1f1',
        },
        {
            tag: t.className,
            color: '#99c1f1',
        },
        {
            tag: t.definition(t.typeName),
            color: '#f9f06b',
        },
        {
            tag: t.typeName,
            color: '#f9f06b',
        },
        {
            tag: t.angleBracket,
            color: '#eff1f1',
        },
        {
            tag: t.tagName,
            color: '#eff1f1',
        },
        {
            tag: t.attributeName,
            color: '#eff1f1',
        },
    ],
});

/**
 * 
 * @param {string} text - the text the code editor will have
 * @param {boolean} readOnly - set the editor state to read only if true
 * @returns {EditorState} - you would place this in the state of the EditorView object
 */
function createState(text = "", readOnly = false) {
    let state = {
        extensions: [
            lineNumbers(),
            highlightSpecialChars(),
            highlightActiveLine(),
            history(),
            foldGutter(),
            dropCursor(),
            search(),
            EditorState.allowMultipleSelections.of(true),
            indentOnInput(),
            syntaxHighlighting(defaultHighlightStyle, {fallback: true}),
            bracketMatching(),
            closeBrackets(),
            autocompletion({activateOnTyping: true}),
            rectangularSelection(),
            EditorState.readOnly.of(readOnly),
            crosshairCursor(),
            highlightSelectionMatches(),
            highlightTrailingWhitespace(),
            // this does not include intellisense for java
            // If that ever becomes a requirement, there is an LSP extension that could work well.
            java(),
            highlightActiveLineGutter(),
            EditorView.lineWrapping,
            keymap.of([
                ...closeBracketsKeymap,
                ...defaultKeymap,
                ...searchKeymap,
                ...historyKeymap,
                ...foldKeymap,
                ...completionKeymap,
                ...lintKeymap,
                // tab losing focus fix
                indentWithTab,
            ]),
            testMasterCodeTheme,
        ],
        doc: text,
    };
    
    return EditorState.create(state);
}

// this is the code editor getting created
let editor = new EditorView({
    state: createState(),
    // at the moment, it only works with 1 code editor per page
    parent: document.querySelector("#code-editor")
});


export function GetCode() {
    return editor.state.doc.toString()
}

export function setCode(code) {
    editor.setState(createState(code))
}

export function setReadOnly() { 
    editor.setState(createState(GetCode(), true));
}



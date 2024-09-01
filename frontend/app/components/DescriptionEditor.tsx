import React from 'react'
import MarkdownEditor from '@uiw/react-markdown-editor';

interface Props {
    defaultValue: string
    template: string
    onChange: (value: string) => void
}

const DescriptionEditor = ({defaultValue, template, onChange}: Props) => {
    const value = defaultValue === "" ? template : defaultValue

    return (
        <MarkdownEditor
            value={value}
            enablePreview={false}
            enableScroll={true}
            onChange={(value) => onChange(value)}
            toolbarsMode={["undo", "redo"]}
            toolbars={["header", "bold", "italic", "underline", "olist", "ulist"]}
        />
    );
}

export default DescriptionEditor
/**
 * @author psarando
 */
import React from 'react';
import ReactDOM from 'react-dom';
import CategoryTree from '../CategoryTree';

const app = {
    hierarchies: [
        {
            "iri": "http://edamontology.org/topic_0003",
            "label": "Topic",
            "subclasses": [
                {
                    "iri": "http://edamontology.org/topic_3068",
                    "label": "Literature and reference",
                    "subclasses": [
                        {
                            "iri": "http://edamontology.org/topic_0218",
                            "label": "Text mining"
                        }
                    ]
                },
                {
                    "iri": "http://edamontology.org/topic_0605",
                    "label": "Informatics",
                    "subclasses": [
                        {
                            "iri": "http://edamontology.org/topic_3071",
                            "label": "Data management"
                        }
                    ]
                }
            ]
        },
        {
            "iri": "http://edamontology.org/operation_0004",
            "label": "Operation",
            "subclasses": [
                {
                    "iri": "http://edamontology.org/operation_2409",
                    "label": "Data handling",
                    "subclasses": [
                        {
                            "iri": "http://edamontology.org/operation_3096",
                            "label": "Editing"
                        }
                    ]
                }
            ]
        }
    ]
};

ReactDOM.render(
    <CategoryTree app={app} />,
    document.getElementById('category-tree')
);

import React, {useEffect} from "react";
import {useParams} from "react-router-dom";
import marked from "marked";
import {Contest} from "../models";
import hljs from "highlight.js";
import "highlight.js/styles/default.css";
// @ts-ignore
import {MathJax} from "mathjax3/mathjax3/mathjax";
// @ts-ignore
import {RegisterHTMLHandler} from "mathjax3/mathjax3/handlers/html";
// @ts-ignore
import {browserAdaptor} from "mathjax3/mathjax3/adaptors/browserAdaptor";
// @ts-ignore
import {CHTML} from "mathjax3/mathjax3/output/chtml";
// @ts-ignore
import {TeX} from "mathjax3/mathjax3/input/tex";

interface Props {
    contest: Contest;
}

RegisterHTMLHandler(browserAdaptor());

const ProblemTab: React.FunctionComponent<Props> = ({contest}) => {
    const { name } = useParams();
    const problem = contest.problemSet.find((problem) => problem.name === name);

    useEffect(() => {
        hljs.initHighlighting();

        const MathJaxDocument = MathJax.document(document, {
            InputJax: new TeX(),
            OutputJax: new CHTML({
                fontURL: 'https://cdn.jsdelivr.net/npm/mathjax@3/es5/output/chtml/fonts/woff-v2'
            })
        });
        MathJaxDocument.findMath().compile().getMetrics().typeset().updateDocument();

        // @ts-ignore
        return () => hljs.initHighlighting.called = false;
    });

    return (
        <React.Fragment>
            <h3 className="mb-4">{problem.title}</h3>
            <div dangerouslySetInnerHTML={{ __html: marked(problem.statement)}}>
            </div>
        </React.Fragment>
    )
};

export default ProblemTab;

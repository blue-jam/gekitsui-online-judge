import React, {useEffect} from "react";
import {useParams} from "react-router-dom";
import marked from "marked";
import {Contest} from "../models";
import hljs from "highlight.js";
import "highlight.js/styles/default.css";

interface Props {
    contest: Contest;
}

const ProblemTab: React.FunctionComponent<Props> = ({contest}) => {
    const { name } = useParams();
    const problem = contest.problemSet.find((problem) => problem.name === name);

    useEffect(() => {
        hljs.initHighlighting();

        // @ts-ignore
        return () => hljs.initHighlighting = false;
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

import React, { ReactElement, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import marked from 'marked';
import { Contest } from '../models';
import hljs from 'highlight.js';
import 'highlight.js/styles/default.css';
// @ts-ignore
import { MathJax } from 'mathjax3/mathjax3/mathjax';
// @ts-ignore
import { RegisterHTMLHandler } from 'mathjax3/mathjax3/handlers/html';
// @ts-ignore
import { browserAdaptor } from 'mathjax3/mathjax3/adaptors/browserAdaptor';
// @ts-ignore
import { CHTML } from 'mathjax3/mathjax3/output/chtml';
// @ts-ignore
import { TeX } from 'mathjax3/mathjax3/input/tex';
import Cookies from 'js-cookie';

interface Props {
  contest: Contest;
}

RegisterHTMLHandler(browserAdaptor());

const ProblemTab: React.FunctionComponent<Props> = ({
  contest
}): ReactElement => {
  const { name } = useParams();
  const problem = contest.problemSet.find(problem => problem.name === name);

  useEffect(() => {
    hljs.initHighlighting();

    const MathJaxDocument = MathJax.document(document, {
      InputJax: new TeX(),
      OutputJax: new CHTML({
        fontURL:
          'https://cdn.jsdelivr.net/npm/mathjax@3/es5/output/chtml/fonts/woff-v2'
      })
    });
    MathJaxDocument.findMath()
      .compile()
      .getMetrics()
      .typeset()
      .updateDocument();

    // @ts-ignore
    return (): void => (hljs.initHighlighting.called = false);
  });

  return (
    <React.Fragment>
      <h3 className="mb-4">{problem.title}</h3>
      <div dangerouslySetInnerHTML={{ __html: marked(problem.statement) }} />
      <hr />
      <form action={`/api/submit?contestName=${contest.name}`} method="post">
        <input type="hidden" name="problemName" value={problem.name} />
        <input type="hidden" name="_csrf" value={Cookies.get('XSRF-TOKEN')} />
        <div className="form-group">
          <label htmlFor="testcase">テストケース</label>
          <textarea className="form-control" name="testcase" id="testcase" />
        </div>
        <button type="submit" className="btn btn-primary">
          Submit
        </button>
      </form>
    </React.Fragment>
  );
};

export default ProblemTab;

import React, { ReactElement, useState } from 'react';
import { Submission } from '../models';
import { NavLink, useParams } from 'react-router-dom';

interface Props {
  contestPath: string;
}

const SubmissionTab: React.FunctionComponent<Props> = ({
  contestPath
}): ReactElement => {
  const { id } = useParams();
  const [submission, setSubmission] = useState<Submission>();

  if (!submission) {
    fetch(`/api/submission?id=${id}`)
      .then<Submission>(response => response.json())
      .then(submission => setSubmission(submission))
      .catch(e => console.log(e));

    return null;
  }

  const { problem, author } = submission;

  return (
    <React.Fragment>
      <h3>{`提出 #${id}`}</h3>
      <table className="table">
        <tbody>
          <tr>
            <th>問題</th>
            <td>
              <NavLink to={`${contestPath}/problem/${problem.name}`}>
                {problem.title}
              </NavLink>
            </td>
          </tr>
          <tr>
            <th>提出者</th>
            <td>{author.username}</td>
          </tr>
          <tr>
            <th>提出時間</th>
            <td>{submission.createdDate}</td>
          </tr>
          <tr>
            <th>ステータス</th>
            <td>{submission.status}</td>
          </tr>
          <tr>
            <th>テストケース</th>
            <td>
              <pre>
                <code className="plaintext">{submission.testcase}</code>
              </pre>
            </td>
          </tr>
        </tbody>
      </table>
    </React.Fragment>
  );
};

export default SubmissionTab;

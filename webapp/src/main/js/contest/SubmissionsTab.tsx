import React, { useState } from 'react';
import { Submission } from '../models';
import { NavLink } from 'react-router-dom';

interface Props {
  username: string;
  contestPath: string;
}

const SubmissionsTab: React.FunctionComponent<Props> = ({
  username,
  contestPath
}) => {
  const [submissionList, setSubmissionList] = useState<
    Submission[] | undefined
  >();

  if (!submissionList) {
    fetch(`/api/submissions?username=${username}`)
      .then<Submission[]>(response => response.json())
      .then(json => setSubmissionList(json))
      .catch(e => console.log(e));

    return null;
  }

  return (
    <React.Fragment>
      <h3>提出履歴</h3>
      <table className="table">
        <thead>
          <tr>
            <th>#</th>
            <th>提出日時</th>
            <th>問題</th>
            <th>提出者</th>
            <th>結果</th>
            <th>詳細</th>
          </tr>
        </thead>
        <tbody>
          {submissionList.map((submission, index) => (
            <tr key={index}>
              <td>{submission.id}</td>
              <td>
                {/*
                  It is not recommended to use Date constructor to parse a String.
                  https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Date
                 */}
                {new Date(submission.createdDate).toLocaleString('ja-JP', {
                  timeZone: 'Asia/Tokyo'
                })}
              </td>
              <td>
                <a>{submission.problem.title}</a>
              </td>
              <td>{submission.author.username}</td>
              <td>{submission.status}</td>
              <td>
                <NavLink to={`${contestPath}/submission/${submission.id}`}>
                  詳細
                </NavLink>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </React.Fragment>
  );
};

export default SubmissionsTab;

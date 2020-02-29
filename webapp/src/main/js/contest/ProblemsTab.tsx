import React from "react";
import { Problem } from "../models";
import { Link } from "react-router-dom";

interface Props {
  problemSet: Problem[];
}

const ProblemsTab: React.FunctionComponent<Props> = ({ problemSet }) => {
  return (
    <div>
      <ul>
        {problemSet.map((problem: Problem, index: number) => (
          <li key={index}>
            <Link to={`/contest/${contest.name}/problem/${problem.name}`}>
              {problem.title}
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ProblemsTab;

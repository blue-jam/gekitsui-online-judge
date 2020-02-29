import React from 'react';
import { NavLink, Route, Switch, useRouteMatch } from 'react-router-dom';
import classNames from 'classnames';
import { Contest } from '../models';
import ProblemsTab from './ProblemsTab';
import ProblemTab from './ProblemTab';
import SubmissionTab from './SubmissionTab';
import SubmissionsTab from './SubmissionsTab';

interface Props {
  contest: Contest;
}

const ContestPage: React.FunctionComponent<Props> = ({ contest }) => {
  const match = useRouteMatch();

  return (
    <React.Fragment>
      <nav
        className={classNames(
          'navbar',
          'navbar-expand-lg',
          'navbar-light',
          'bg-light',
          'mb-4'
        )}
      >
        <span className="navbar-brand">{contest.title}</span>
      </nav>
      <div className="container">
        <ul className="nav nav-tabs mb-4">
          <li className="nav-item">
            <NavLink
              exact
              className="nav-link"
              activeClassName="active"
              to={match.path}
            >
              トップ
            </NavLink>
          </li>
          <li className="nav-item">
            <NavLink
              className="nav-link"
              activeClassName="active"
              to={`${match.path}/problem`}
            >
              問題
            </NavLink>
          </li>
          <li className="nav-item">
            <NavLink
              className="nav-link"
              activeClassName="active"
              to={`${match.path}/submission`}
            >
              提出履歴
            </NavLink>
          </li>
        </ul>
        <Switch>
          <Route exact path={match.path}>
            <h2 className="mb-4">{contest.title}</h2>
          </Route>
          <Route exact path={`${match.path}/problem`}>
            <ProblemsTab problemSet={contest.problemSet} />
          </Route>
          <Route path={`${match.path}/problem/:name`}>
            <ProblemTab contest={contest} />
          </Route>
          <Route path={`${match.path}/submission/:id`}>
            <SubmissionTab contestPath={match.path} />
          </Route>
          <Route path={`${match.path}/submission`}>
            <SubmissionsTab contestPath={match.path} username={username} />
          </Route>
        </Switch>
      </div>
    </React.Fragment>
  );
};

export default ContestPage;

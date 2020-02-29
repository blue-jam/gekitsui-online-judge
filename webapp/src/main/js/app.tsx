import React, { ReactElement } from "react";
import ReactDOM from "react-dom";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import ContestPage from "./contest/ContestPage";
import { Contest } from "./models";

declare global {
  let contest: Contest;
}

class App extends React.Component<{}> {
  render(): ReactElement {
    return (
      <Router>
        <Switch>
          <Route path={`/contest/${contest.name}`}>
            <ContestPage contest={contest} />
          </Route>
        </Switch>
      </Router>
    );
  }
}

ReactDOM.render(<App />, document.getElementById("react"));

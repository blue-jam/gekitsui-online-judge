export interface Problem {
  name: string;
  title: string;
  statement: string;
}

export interface User {
  username: string;
}

export interface Submission {
  problem: Problem;
  author: User;
  testcase: string;
  status: string;
  id: number;
  createdDate: Date;
}

export interface Contest {
  name: string;
  title: string;
  problemSet?: Problem[];
}

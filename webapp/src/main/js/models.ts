export interface Problem {
  name: string;
  title: string;
  statement: string;
}

export interface Contest {
  name: string;
  title: string;
  problemSet?: Problem[];
}

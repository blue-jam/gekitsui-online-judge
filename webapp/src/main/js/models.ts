export interface Problem {
    name: string;
    title: string;
}

export interface Contest {
    name: string;
    title: string;
    problemSet?: Problem[];
}

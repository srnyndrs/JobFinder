export type Job = {
    id?: number,
    title: string,
    description: string,
    location: string,
    created: string,
    jobType: string,
    remote: string,
    company: {
        id: number,
        name: string,
        image: string
    }
}

export const jobSortingFields: (keyof Job)[] = ["id", "title", "location", "created", "jobType", "remote" ]

export const JOB_DESCRIPTION_TEMPLATE = `We are looking for a ...
### Key Responsibilities:
- 

### Required Skills:
- 

### Preferred Qualifications:
- 

### Benefits:
- `
import React from 'react'
import { Job } from '../domain/Job';
import Link from 'next/link';
import { sort } from 'fast-sort';
import JobCreatorModal from './JobCreatorModal';
import { Company } from '../domain/Company';
import JobDeleteModal from './JobDeleteModal';

interface Props {
    searchParams: {
        create?: boolean
        edit?: number
        jobDelete?: number
    }
}

const JobsPage = async ({searchParams: { create, edit, jobDelete }}: Props) => {
    const companies: Company[] | undefined = await fetch('http:localhost:8080/v1/companies', { cache: 'no-store' })
        .then(async (response) => {
            if(response.ok) return response.json();
        })
        .catch(() => { throw new Error("Failed to fetch companies")} );

    const jobs: Job[] | undefined = await fetch('http:localhost:8080/v1/jobs', { cache: 'no-store' })
        .then(async (response) => {
            if(response.ok) return response.json();
        })
        .catch(() => { throw new Error("Failed to fetch jobs")} );

    const selectedJobForEdit = jobs?.find((j) => j.id == edit);
    const selectedJobForDelete = jobs?.find((j)=> j.id == jobDelete);

    return (
        <>
            <div className='mx-6 mb-6'>
                <div className="flex flex-row items-center justify-between pt-4">
                    <h1>Jobs</h1>
                    <Link className="btn btn-primary btn-outline btn-sm" href={"/jobs?create=true"}>
                        <svg className="size-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
                        </svg>
                        <p>New job</p>
                    </Link>
                </div> 
                <div className="overflow-x-auto">
                    <table className='table'>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Title</th>
                                <th>Location</th>
                                <th>Created</th>
                                <th>Job Type</th>
                                <th>Remote</th>
                                <th>Company</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody className="text-md">
                        { jobs && sort(jobs).asc('id').map( job =>
                            <tr key={job.id}>
                                <td>{job.id}</td>
                                <td>
                                    <Link className="link link-primary" href={`/jobs/${job.id}`}>{job.title}</Link>
                                </td>
                                <td>{job.location}</td>
                                <td>{job.created}</td>
                                <td>{job.jobType}</td>
                                <td>{job.remote}</td>
                                <td>
                                    <Link className="link link-secondary" href={`/companies/${job.company.id}`}>{job.company.name}</Link>
                                </td>
                                <td className="join join-horizontal space-x-4">
                                    <Link href={`/jobs?edit=${job.id}`}>
                                        <p className="btn btn-xs btn-primary btn-outline w-max h-fit">Edit</p>
                                    </Link>
                                    <Link href={`/jobs?jobDelete=${job.id}`}>
                                        <p className="btn btn-xs btn-error btn-outline w-max h-fit">Delete</p>
                                    </Link>
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
            { companies && (create && <JobCreatorModal companies={companies}/> ||
                edit && selectedJobForEdit && <JobCreatorModal companies={companies} originalJob={selectedJobForEdit}/> || 
                jobDelete && selectedJobForDelete && <JobDeleteModal job={selectedJobForDelete}/>)
            }
        </>
    );
}

export default JobsPage;
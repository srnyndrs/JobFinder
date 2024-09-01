import React from 'react'
import { Job } from '../../domain/Job'
import { CompanyLogo } from '../../components/CompanyLogo'
import Link from 'next/link'

interface Props {
    job: Job
}

const JobTitle = ({ job }: Props) => {
    return (
        <div className="flex w-full items-center space-x-3 lg:space-x-4">
            <div className="w-1/4 lg:w-fit">
                <CompanyLogo name={job.company.name} image={job.company.image} width={132} height={132} />
            </div>
            <div className="w-3/4 lg:w-fit space-y-2">
                <p className="text-xl lg:text-3xl text-gray-100 font-extrabold">
                    {job.title}
                </p>
                <div className="text-md lg:text-lg text-gray-100 font-semibold">
                    <Link href={`/companies/${job.company.id}`} className='link-hover'>{job.company.name}</Link>
                </div>
                <div className="flex flex-row join-horizontal items-center text-white space-x-2">
                    <div className="rounded-md text-sm lg:text-md bg-primary px-2 py-1">{job.location}</div>
                    <div className="rounded-md text-sm lg:text-md bg-accent px-2 py-1">{job.jobType}</div>
                    <div className="rounded-md text-sm lg:text-md bg-slate-500 px-2 py-1">{job.remote}</div>
                </div>
            </div>
        </div>
    );
}

export default JobTitle;
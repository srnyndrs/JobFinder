'use client'
import React from 'react'
import { CompanyLogo } from './CompanyLogo'
import { Job, jobSortingFields } from '../domain/Job'
import Link from 'next/link'
import { useState } from 'react'
import { sort } from 'fast-sort'
import moment from 'moment'

interface Props {
    jobs: Job[]
}

const JobsTable = ({ jobs }: Props) => {
    const [sortingField, setSortingField] = useState<number>(0);
    const [ascending, setAscending] = useState(true);

    const headers = [
        { name: "Position", sortFunction: (a: Job) => a.title },
        { name: "Location", sortFunction: (a: Job) => a.location },
        { name: "Type", sortFunction: (a: Job) => a.jobType },
        { name: "Remote", sortFunction: (a: Job) => a.remote },
        { name: "Updated", sortFunction: (a: Job) => a.created }
    ];

    const handleClick = (event: React.MouseEvent, sortBy: number) => {
        event.preventDefault();
        const newSortingField = (sortBy !== undefined && sortBy < headers.length) ? sortBy : 0;
        if(newSortingField == sortingField) {
            setAscending(prevState => !prevState)
        } else {
            setAscending(true);
        }
        setSortingField(newSortingField);
    }

    return (
        <div className="overflow-x-auto">
            <table className="table w-full md:border-separate border-spacing-y-0 mb-6">
                <thead className="flex-auto text-left text-sm md:text-xl items-center">
                    <tr>
                        { headers.map( (header,index) =>
                            <th key={index}>
                                <button key={index} className="text-black link-hover font-bold" onClick={(e) => handleClick(e, index)}>
                                    <div className="items-center join join-horizontal space-x-2">
                                        <p>{header.name}</p>
                                        { sortingField === index &&
                                            (ascending ?
                                            <svg className="size-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor">
                                                <path strokeLinecap="round" strokeLinejoin="round" d="M19.5 13.5 12 21m0 0-7.5-7.5M12 21V3" />
                                            </svg>
                                            :
                                            <svg className="size-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor">
                                                <path strokeLinecap="round" strokeLinejoin="round" d="M4.5 10.5 12 3m0 0 7.5 7.5M12 3v18" />
                                            </svg>
                                            )                                   
                                        }
                                    </div>
                                </button>
                            </th>
                        )}
                    </tr>
                </thead>
                <tbody className="text-sm lg:text-lg">
                { (ascending ? sort(jobs).asc(headers[sortingField].sortFunction) : sort(jobs).desc(headers[sortingField].sortFunction)).map( (job, index) =>
                    <tr key={job.id} className="flex-row">
                        <td>
                            <div className="flex items-top gap-0 md:gap-3">
                                <div className="w-0 h-0 invisible md:visible md:h-fit md:w-fit">
                                    <CompanyLogo name={job.company.name!} image={job.company.image!} width={72} height={72}/>
                                </div>
                                <div className="space-y-1">
                                    <div className="w-5/6 md:w-fit text-md md:text-lg lg:text-xl ">
                                        <p className="font-semibold md:font-bold link md:link-hover">
                                            <Link href={`/jobs/${job.id}`}>{job.title}</Link>
                                        </p>
                                    </div>
                                    <div className="w-0 h-0 invisible md:visible md:h-fit md:w-fit">
                                        <p className="link-secondary">
                                            <Link href={`/companies/${job.company.id}`}>{job.company.name}</Link>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td>
                            {job.location}
                        </td>
                        <td>
                            {job.jobType}
                        </td>
                        <td>
                            {job.remote}
                        </td>
                        <td>
                            {moment(job.created).fromNow()}
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
}

export default JobsTable
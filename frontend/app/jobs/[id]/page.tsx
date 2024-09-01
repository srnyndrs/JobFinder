import { Job } from '@/app/domain/Job'
import React from 'react'
import Markdown from 'react-markdown';
import JobTitle from './JobTitle';
import JobBanner from './JobBanner';
import Link from 'next/link';
import { notFound } from 'next/navigation';

interface Props {
    params: {
        id: number
    }
}

const JobPage = async ({ params: { id } }: Props) => {   
    const job: Job | undefined = await fetch(`http://localhost:8080/v1/jobs/${id}`, { cache: 'no-store' })
        .then(response => {
            if(response.ok) return response.json();
        })
        .catch(() => { throw new Error("Failed to fetch job") });

    //
    if(!job) {
        notFound();
    }

    return (
        <div className="w-full">
            <div className="relative">
                <JobBanner/>
                <div className="absolute top-2/3 w-fit md:w-1/2 lg:w-1/2 m-2 md:m-3 lg:m-6 lg:top-2/4">
                    <JobTitle job={job}/>
                </div>
            </div>
            <div className="flex w-full pt-6 pe-3 md:pt-3 justify-end">
                <Link className="btn text-white btn-primary px-4 py-2 md:px-8 md:py-4" href="#">Apply</Link>
            </div>
            <div className="mx-3 lg:mx-6">
                <div className="divider divider-start">
                    <div className="badge rounded badge-outline text-lg font-semibold p-4">
                        Description
                    </div>
                </div>
                <div className="w-fit my-6">
                    <div className="prose text-md md:text-lg text-justify">
                        <Markdown>{job.description}</Markdown>
                        { job.description.length == 0 &&
                            <div className="text-lg italic text-gray-600 my-8">
                                No description provided.
                            </div>
                        }
                    </div>
                </div>
            </div>
        </div>
    );
}

export default JobPage;
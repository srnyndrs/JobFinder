import React from 'react'
import { Company } from '@/app/domain/Company';
import { CompanyLogo } from '@/app/components/CompanyLogo';
import Markdown from 'react-markdown';
import { Job } from '@/app/domain/Job';
import Link from 'next/link';
import CompanyBanner from './CompanyBanner';
import { notFound } from 'next/navigation';

interface Props {
    params: {
        id: number
    }
}

const CompanyPage = async ({params: {id}}: Props) => {
    const company: Company | undefined = await fetch(`http://localhost:8080/v1/companies/${id}`, { cache: "no-store" })
        .then(response => {
            if (response.ok) return response.json();
        })
        .catch(() => { throw new Error("Failed to fetch company")} );

    if(!company) {
        notFound();
    }

    const jobs: Job[] = await fetch(`http://localhost:8080/v1/jobs?companyId=${id}`, { cache: "no-store" })
        .then(async (response) => {
            if (!response.ok) {
                return [];
            }
            return response.json();
        })
        .catch(() => { throw new Error("Failed to fetch jobs")} );

    return (
        <div className="w-full mb-6">
            <div className="relative">
                <CompanyBanner />
                <div className="flex absolute top-2/3 md:top-2/4 m-2 md:m-6 items-center space-x-3">
                    <CompanyLogo name={company!.name} image={company!.image} width={128} height={128} />
                    <p className="text-xl md:text-3xl font-extrabold text-gray-100 drop-shadow-xl">{company!.name}</p>
                </div>
            </div>
            <div className="flex-auto mt-24 md:mt-14 mx-3 md:mx-6 space-y-6">
                <div className="divider divider-start">
                    <div className="badge rounded badge-outline text-lg font-semibold p-4">
                        About
                    </div>
                </div>
                <div className="prose text-md md:text-lg text-justify">
                    <Markdown>{company.description}</Markdown>
                    { company.description.length == 0 &&
                        <div className="text-lg italic text-gray-600 my-8">
                            No description provided.
                        </div>
                    }
                </div>
                <div className="divider divider-start">
                    <div className="badge rounded badge-outline text-lg font-semibold p-4">
                        Positions
                    </div>
                </div>
                <div>
                    { jobs.map( (job, index) =>
                        <div className="flex-1 py-2" key={index}>
                            <div className='join-vertical my-2'>
                                <p className="text-2xl link-primary"><Link href={`/jobs/${job.id}`}>{job.title}</Link></p>
                                <div className='join space-x-4 md:items-center'>
                                    <div className="text-white badge badge-primary rounded-lg p3">{job.location}</div>
                                    <div className="text-white badge badge-accent rounded-lg p3">{job.jobType}</div>
                                    <div className="text-slate-600 badge badge-neutral rounded-lg p3">{job.remote}</div>
                                </div>
                            </div>
                        </div>
                    )}
                    { jobs.length === 0 &&
                        <div className="flex join join-horizontal text-lg items-center gap-x-2 text-gray-600 my-8">
                            {"Currently not hiring."}
                            <svg className="size-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" d="M15.182 16.318A4.486 4.486 0 0 0 12.016 15a4.486 4.486 0 0 0-3.198 1.318M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0ZM9.75 9.75c0 .414-.168.75-.375.75S9 10.164 9 9.75 9.168 9 9.375 9s.375.336.375.75Zm-.375 0h.008v.015h-.008V9.75Zm5.625 0c0 .414-.168.75-.375.75s-.375-.336-.375-.75.168-.75.375-.75.375.336.375.75Zm-.375 0h.008v.015h-.008V9.75Z" />
                            </svg>
                        </div>
                    }
                </div>
            </div>
        </div>
    );
}

export default CompanyPage;
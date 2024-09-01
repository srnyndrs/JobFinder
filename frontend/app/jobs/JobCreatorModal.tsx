'use client'
import React from 'react'
import JobCreatorForm from './JobCreatorForm';
import { useRouter } from 'next/navigation';
import { Job } from '../domain/Job';
import { Company } from '../domain/Company';

interface Props {
    companies: Company[]
    originalJob?: Job
}

const JobCreatorModal = ({companies, originalJob}: Props) => {
    const router = useRouter();

    return (
        <div className="relative z-10" aria-labelledby="modal-title" role="dialog" aria-modal="true">
            <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true"></div>
            <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-2 md:p-4 text-center">
                <div className="relative transform overflow-hidden rounded-lg w-full md:w-11/12 md:max-w-5xl">
                    <div className="bg-white px-4 pb-4 pt-5">
                        <div className="flex justify-between items-center">
                            <p className="text-lg font-bold">
                            { originalJob === undefined ? "Create Job" : "Update Job" }
                            </p>
                            <button onClick={router.back} type="button" className="justify-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 sm:mt-0 sm:w-auto">Cancel</button>
                        </div>
                        <div className="divider my-2"/>
                        <JobCreatorForm companies={companies} originalJob={originalJob}/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default JobCreatorModal;